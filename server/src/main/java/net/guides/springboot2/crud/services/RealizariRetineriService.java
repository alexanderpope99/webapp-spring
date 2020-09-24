package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.ParametriiSalariu;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.repository.OresuplimentareRepository;
import net.guides.springboot2.crud.repository.RealizariRetineriRepository;

@Service
public class RealizariRetineriService {
    RealizariRetineriService() {
    }

    // SERVICES
    @Autowired
    private ZileService zileService;
    @Autowired
    private COService coService;
    @Autowired
    private CMService cmService;
    @Autowired
    private PersoaneIntretinereService persoaneIntretinereService;
    @Autowired
    private DeduceriService deduceriService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private ParametriiSalariuService parametriiSalariuService;
    @Autowired
    private TicheteService ticheteService;
    @Autowired
    private RetineriService retineriService;
    @Autowired
    private OresuplimentareRepository oresuplimentareRepository;

    // REPOSITORIES
    @Autowired
    private RealizariRetineriRepository realizariRetineriRepository;

    private float impozitSalariu = 0;
    private float deducere = 0;

    public long getIdContractByIdPersoana(long idpersoana) throws ResourceNotFoundException {
        Contract contract = contractService.getContractByIdpersoana(idpersoana);
        return contract.getId();
    }

    public RealizariRetineri getRealizariRetineri(int luna, int an, long idcontract) throws ResourceNotFoundException {
        return realizariRetineriRepository.findByLunaAndAnAndIdcontract(luna, an, idcontract);
    }

    public int calcRestplata(long idcontract, int luna, int an, float totalDrepturi, float nrTichete,
            int nrPersoaneIntretinere) throws ResourceNotFoundException {

        Contract contract = contractService.getContractById(idcontract);
        ParametriiSalariu parametriiSalariu = parametriiSalariuService.getParametriiSalariu();

        float casSalariu = Math.round(totalDrepturi * parametriiSalariu.getCas() / 100);
        float cassSalariu = Math.round(totalDrepturi * parametriiSalariu.getCass() / 100);
        int platesteImpozit = contract.isCalculdeduceri() ? 1 : 0;

        int areFunctieDebaza = contract.isFunctiedebaza() ? 1 : 0;
        float impozit = parametriiSalariu.getImpozit() / 100;

        if (totalDrepturi < 3600)
            this.deducere = deduceriService.getDeducereBySalariu(totalDrepturi, nrPersoaneIntretinere)
                    * areFunctieDebaza;
        else
            this.deducere = 0;

        float restPlata = totalDrepturi - casSalariu - cassSalariu;

        float valoareTichete = nrTichete * parametriiSalariu.getValtichet();

        this.impozitSalariu = (restPlata + valoareTichete - deducere) * impozit;

        restPlata -= platesteImpozit * impozitSalariu;

        return Math.round(restPlata);
    } // calcRestPlata

    public RealizariRetineri calcRealizariRetineri(long idcontract, int luna, int an, int primaBruta, int nrTichete,
            float totalOreSuplimentare) throws ResourceNotFoundException {
        Contract contract = contractService.getContractById(idcontract);

        ParametriiSalariu parametriiSalariu = parametriiSalariuService.getParametriiSalariu();

        int zileCO = coService.getZileCOTotal(luna, an, idcontract);
        int zileCOLucratoare = coService.getZileCOLucratoare(luna, an, idcontract);
        int zileCONeplatit = coService.getZileCFP(luna, an, idcontract);
        int zileCONeplatitLucratoare = coService.getZileCFPLucratoare(luna, an, idcontract);
        int zileCM = cmService.getZileCM(luna, an, idcontract);
        // int valCM = cmService.getValCM(luna, an, idcontract);
        int zileCMLucratoare = cmService.getZileCMLucratoare(luna, an, idcontract);

        int norma = zileService.getZileLucratoareInLunaAnul(luna, an);
        int duratazilucru = contract.getNormalucru();

        int zileLucrate = norma - zileCOLucratoare - zileCMLucratoare;
        int oreLucrate = zileLucrate * duratazilucru;

        float totalDrepturi = contract.getSalariutarifar() + primaBruta + totalOreSuplimentare;

        float salariuPeZi = totalDrepturi / norma;
        float salariuPeOra = totalDrepturi / norma / duratazilucru;

        int salariuRealizat = Math.round(salariuPeZi * zileLucrate + primaBruta + totalOreSuplimentare);
        totalDrepturi = salariuRealizat; // + valoare CM

        float cas = Math.round(totalDrepturi * parametriiSalariu.getCas() / 100);
        float cass = Math.round(totalDrepturi * parametriiSalariu.getCass() / 100);
        float cam = Math.round(totalDrepturi * parametriiSalariu.getCam() / 100);
        float valoareTichete = parametriiSalariu.getValtichet() * nrTichete;
        int nrPersoaneIntretinere = persoaneIntretinereService.getNrPerosaneIntretinere(contract.getId());
        int restPlata = calcRestplata(idcontract, luna, an, totalDrepturi, nrTichete, nrPersoaneIntretinere); // rv is
                                                                                                              // rounded
        float impozit = Math.round(this.impozitSalariu);

        RealizariRetineri rr = new RealizariRetineri(idcontract, luna, an, nrTichete, zileCO, zileCOLucratoare, zileCM,
                zileCMLucratoare, zileCONeplatit, zileCONeplatit, duratazilucru, norma, zileLucrate, oreLucrate,
                (int) totalDrepturi, salariuPeZi, salariuPeOra, cas, cass, cam, impozit, valoareTichete, restPlata,
                nrPersoaneIntretinere, (int) this.deducere, primaBruta, totalOreSuplimentare);

        int nrOreSuplimentare = oresuplimentareRepository.countNrOreSuplimentareByIdstat(rr.getId());
        int zilePlatite = norma - zileCONeplatitLucratoare;

        rr.setZileplatite(zilePlatite);
        rr.setNroresuplimentare(nrOreSuplimentare);
        rr.setSalariurealizat(salariuRealizat);

        return rr;
    } // calcRealizariRetineri

    public RealizariRetineri saveRealizariRetineri(int luna, int an, long idcontract) throws ResourceNotFoundException {
        if (realizariRetineriRepository.existsByLunaAndAnAndIdcontract(luna, an, idcontract))
            return getRealizariRetineri(luna, an, idcontract);

        int nrTichete = ticheteService.getNrTichete(luna, an, idcontract);

        RealizariRetineri realizariRetineri = calcRealizariRetineri(idcontract, luna, an, 0, nrTichete, 0);
        // empty retinere
        realizariRetineri = realizariRetineriRepository.save(realizariRetineri);
        retineriService.saveRetinere(realizariRetineri.getId());

        return realizariRetineri;
    }
}
