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

    // REPOSITORIES
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ParametriiSalariuRepository parametriiSalariuRepository;

    public long getIdContractByIdPersoana(long idpersoana)
            throws ResourceNotFoundException {
        Contract contract = contractRepository.findByIdPersoana(idpersoana)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found for this idpersoana :: " + idpersoana));
        return contract.getId();
    }

    public int getRestplata(Contract contract, float valoareTichete, ParametriiSalariu parametriiSalariu, int nrPersoaneIntretinere) {
        float salariuTarifar = contract.getSalariutarifar();
        float casSalariu = Math.round(salariuTarifar * parametriiSalariu.getCas() / 100);
        float cassSalariu = Math.round(salariuTarifar * parametriiSalariu.getCass() / 100);
        int platesteImpozit = contract.isCalculdeduceri() ? 1 : 0;
        
        int areFunctieDebaza = contract.isFunctiedebaza() ? 1 : 0;
        float impozit = parametriiSalariu.getImpozit() / 100;
        float deducere = (float) 0;
        if(salariuTarifar < 3600)
            deducere = deduceriService.getDeducereBySalariu(salariuTarifar, nrPersoaneIntretinere);

        float restPlata = salariuTarifar - casSalariu - cassSalariu;
        
        restPlata -= platesteImpozit * 
            Math.round(
                (restPlata + valoareTichete - deducere * areFunctieDebaza) * impozit
            );

        return Math.round(restPlata);
    }

    public RealizariRetineri getRealizariRetineri(int luna, int an, long idcontract) throws ResourceNotFoundException {
        Contract contract = contractRepository.findById(idcontract).orElseThrow(() -> new ResourceNotFoundException("Contract not found for this idcontract :: " + idcontract));

        ParametriiSalariu parametriiSalariu = parametriiSalariuRepository.findById((long)1).orElseThrow(() -> new ResourceNotFoundException("ParametriiSalariu not found for this id :: "));

        int nrTichete = ticheteService.getNrTichete(luna, an, idcontract);
        int zileCO = coService.getZileCO(luna, an, idcontract);
        int zileCM = cmService.getZileCM(luna, an, idcontract);
        int zileCONeplatit = coService.getZileCONeplatite(luna, an, idcontract);
        int duratazilucru = contract.getNormalucru();
        int zileLucratoare = zileService.getZileLucratoareInLunaAnul(luna, an);
        float salariuTarifar = contract.getSalariutarifar();
        float cas = Math.round(salariuTarifar * parametriiSalariu.getCas() / 100);
        float cass = Math.round(salariuTarifar * parametriiSalariu.getCass() / 100);
        float cam = Math.round(salariuTarifar * parametriiSalariu.getCam() / 100);
        float impozit = Math.round(salariuTarifar * parametriiSalariu.getImpozit() / 100);
        float valoareTichete = Math.round(parametriiSalariu.getValtichet() * nrTichete);
        int nrPersoaneIntretinere = persoaneIntretinereService.getNrPerosaneIntretinere(contract.getId());
        int restPlata = getRestplata(contract, valoareTichete, parametriiSalariu, nrPersoaneIntretinere); // already rounded


        return new RealizariRetineri( nrTichete, zileCO, zileCM, zileCONeplatit, duratazilucru, zileLucratoare, cas, cass, cam, impozit, valoareTichete, restPlata, nrPersoaneIntretinere );
    }

}
