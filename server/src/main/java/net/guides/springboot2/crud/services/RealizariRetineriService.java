package net.guides.springboot2.crud.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.ParametriiSalariu;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.repository.ContractRepository;
import net.guides.springboot2.crud.repository.ParametriiSalariuRepository;

@Service
public class RealizariRetineriService {
    RealizariRetineriService() {}

    // SERVICES
    @Autowired
    private TicheteService ticheteService;
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

    // REPOSITORIES

    private float impozitSalariu = 0;
    private float deducere = 0;

    public long getIdContractByIdPersoana(long idpersoana)
            throws ResourceNotFoundException {
        Contract contract = contractService.getContractByIdpersoana(idpersoana);
        return contract.getId();
    }

    public int getRestplata(long idcontract, int luna, int an, float totalDrepturi, float nrTichete, int nrPersoaneIntretinere) throws ResourceNotFoundException {

        Contract contract = contractService.getContractById(idcontract);
        ParametriiSalariu parametriiSalariu = parametriiSalariuService.getParametriiSalariu();

        float casSalariu = Math.round(totalDrepturi * parametriiSalariu.getCas() / 100);
        float cassSalariu = Math.round(totalDrepturi * parametriiSalariu.getCass() / 100);
        int platesteImpozit = contract.isCalculdeduceri() ? 1 : 0;
        
        int areFunctieDebaza = contract.isFunctiedebaza() ? 1 : 0;
        float impozit = parametriiSalariu.getImpozit() / 100;

        if(totalDrepturi < 3600)
            this.deducere = deduceriService.getDeducereBySalariu(totalDrepturi, nrPersoaneIntretinere);
        else this.deducere = 0;

        float restPlata = totalDrepturi - casSalariu - cassSalariu;

        float valoareTichete = nrTichete * parametriiSalariu.getValtichet();

        this.impozitSalariu = (restPlata + valoareTichete - deducere * areFunctieDebaza) * impozit;
        
        restPlata -= platesteImpozit * impozitSalariu;

        return Math.round(restPlata);
    }

    public RealizariRetineri getRealizariRetineri(int luna, int an, long idcontract) throws ResourceNotFoundException {
        Contract contract = contractService.getContractById(idcontract);

        ParametriiSalariu parametriiSalariu = parametriiSalariuService.getParametriiSalariu();

        int nrTichete = ticheteService.getNrTichete(luna, an, idcontract);

        int zileCO = coService.getZileCO(luna, an, idcontract);
        int zileCONeplatit = coService.getZileCONeplatite(luna, an, idcontract);
        int zileCM = cmService.getZileCM(luna, an, idcontract);

        int norma = zileService.getZileLucratoareInLunaAnul(luna, an);
        int duratazilucru = contract.getNormalucru();

        int zileLucrate = norma - zileCO - zileCM;
        int oreLucrate = zileLucrate * duratazilucru;
        int zilePlatite = norma - zileCONeplatit;
        // int orePlatite = zilePlatite * duratazilucru;
        
        float salariuTarifar = contract.getSalariutarifar();
        
        float salariuPeZi = salariuTarifar / norma;
        float salariuPeOra = salariuTarifar / norma / duratazilucru;

        float totalDrepturi = salariuPeZi * zilePlatite;

        float cas = Math.round(totalDrepturi * parametriiSalariu.getCas() / 100);
        float cass = Math.round(totalDrepturi * parametriiSalariu.getCass() / 100);
        float cam = Math.round(totalDrepturi * parametriiSalariu.getCam() / 100);
        float valoareTichete = parametriiSalariu.getValtichet() * nrTichete;
        int nrPersoaneIntretinere = persoaneIntretinereService.getNrPerosaneIntretinere(contract.getId());
        int restPlata = getRestplata(idcontract, luna, an, totalDrepturi, nrTichete, nrPersoaneIntretinere); // already rounded
        float impozit = Math.round(this.impozitSalariu);


        return new RealizariRetineri( nrTichete, zileCO, zileCM, zileCONeplatit, duratazilucru, norma, zileLucrate, oreLucrate, totalDrepturi, salariuPeZi, salariuPeOra, cas, cass, cam, impozit, valoareTichete, restPlata, nrPersoaneIntretinere, (int)this.deducere );
    }

    public RealizariRetineri calcRealizariRetineri(long idcontract, int luna, int an, float totalDrepturi, int nrTichete) throws ResourceNotFoundException {
        Contract contract = contractService.getContractById(idcontract);

        ParametriiSalariu parametriiSalariu = parametriiSalariuService.getParametriiSalariu();

        int zileCO = coService.getZileCO(luna, an, idcontract);
        int zileCONeplatit = coService.getZileCONeplatite(luna, an, idcontract);
        int zileCM = cmService.getZileCM(luna, an, idcontract);

        int norma = zileService.getZileLucratoareInLunaAnul(luna, an);
        int duratazilucru = contract.getNormalucru();

        int zileLucrate = norma - zileCO - zileCM;
        int oreLucrate = zileLucrate * duratazilucru;
        int zilePlatite = norma - zileCONeplatit;

        float salariuPeZi = totalDrepturi / norma;
        float salariuPeOra = totalDrepturi / norma / duratazilucru;

        totalDrepturi = salariuPeZi * zilePlatite;

        float cas = Math.round(totalDrepturi * parametriiSalariu.getCas() / 100);
        float cass = Math.round(totalDrepturi * parametriiSalariu.getCass() / 100);
        float cam = Math.round(totalDrepturi * parametriiSalariu.getCam() / 100);
        float valoareTichete = parametriiSalariu.getValtichet() * nrTichete;
        int nrPersoaneIntretinere = persoaneIntretinereService.getNrPerosaneIntretinere(contract.getId());
        int restPlata = getRestplata(idcontract, luna, an, totalDrepturi, nrTichete, nrPersoaneIntretinere); // already rounded
        float impozit = Math.round(this.impozitSalariu);


        return new RealizariRetineri( nrTichete, zileCO, zileCM, zileCONeplatit, duratazilucru, norma, zileLucrate, oreLucrate, totalDrepturi, salariuPeZi, salariuPeOra, cas, cass, cam, impozit, valoareTichete, restPlata, nrPersoaneIntretinere, (int)this.deducere );
    }
}
