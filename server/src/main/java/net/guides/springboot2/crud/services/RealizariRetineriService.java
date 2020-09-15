package net.guides.springboot2.crud.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.controller.ParametriiSalariuController;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.ParametriiSalariu;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.repository.ContractRepository;
import net.guides.springboot2.crud.repository.DeduceriRepository;
import net.guides.springboot2.crud.repository.ParametriiSalariuRepository;
import net.guides.springboot2.crud.repository.PersoanaIntretinereRepository;

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
    @Autowired
    private DeduceriRepository deduceriRepository;

    public long getIdContractByIdPersoana(long idpersoana)
            throws ResourceNotFoundException {
        Contract contract = contractRepository.findByIdPersoana(idpersoana)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found for this idpersoana :: " + idpersoana));
        return contract.getId();
    }

    public void getTotaldrepturi(Contract contract, int nrTichete) throws ResourceNotFoundException {
        ParametriiSalariu parametriiSalariu = parametriiSalariuRepository.findById((long)1).orElseThrow(() -> new ResourceNotFoundException("ParametriiSalariu not found for this id :: "));

        float salariuTarifar = contract.getSalariutarifar();
        float casSalariu = salariuTarifar * parametriiSalariu.getCas() / 100;
        float cassSalariu = salariuTarifar * parametriiSalariu.getCass() / 100;
        boolean platesteImpozit = contract.isCalculdeduceri();
        float impozit = parametriiSalariu.getImpozit() / 100;
        float impozitSalariu = salariuTarifar * impozit;
        float cam = salariuTarifar * parametriiSalariu.getCam() / 100;
        boolean areFunctieDebaza = contract.isFunctiedebaza();
        float valoareTichete = parametriiSalariu.getValtichet() * nrTichete;
        String strPersoaneIntretinere = persoaneIntretinereService.getStrPersoaneIntretinere(contract.getId());
        
        Float deducere = (float) 0;
        if(salariuTarifar < 3600)
            deducere = deduceriService.getDeducereBySalariu(salariuTarifar, strPersoaneIntretinere);
        deducere = deducere == null ? 0 : deducere;
        
        float totalDrepturi = salariuTarifar - casSalariu - cassSalariu;
        if(platesteImpozit) {
            
            if(areFunctieDebaza) {
                impozitSalariu = (totalDrepturi + valoareTichete - deducere) * impozit;
            }
        }
    }

    public RealizariRetineri getRealizariRetineri(int luna, int an, long idcontract) throws ResourceNotFoundException {
      Contract contract = contractRepository.findById(idcontract).orElseThrow(() -> new ResourceNotFoundException("Contract not found for this idcontract :: " + idcontract));

      int nrTichete = ticheteService.getNrTichete(luna, an, idcontract);
      int zileCO = coService.getZileCO(luna, an, idcontract);
      int zileCM = cmService.getZileCM(luna, an, idcontract);
      int zileCONeplatit = coService.getZileCONeplatite(luna, an, idcontract);
      int duratazilucru = contract.getNormalucru();
      int zileLucratoare = zileService.getZileLucratoareInLunaAnul(luna, an);

      getTotaldrepturi(contract, nrTichete);
    
      return new RealizariRetineri(nrTichete, zileCO, zileCM, zileCONeplatit, duratazilucru, zileLucratoare);
    }

}
