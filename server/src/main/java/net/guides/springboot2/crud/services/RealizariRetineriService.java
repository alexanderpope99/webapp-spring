package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.repository.ContractRepository;

@Service
public class RealizariRetineriService {
    RealizariRetineriService() {}

    @Autowired
    private TicheteService ticheteService;
    @Autowired
    private COService coService;
    @Autowired
    private CMService cmService;
    @Autowired
    private ContractRepository contractRepository;

    public long getIdContractByIdPersoana(long idpersoana)
            throws ResourceNotFoundException {
        Contract contract = contractRepository.findByIdPersoana(idpersoana)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found for this idpersoana :: " + idpersoana));
        return contract.getId();
    }

    public int getNrTichete(int luna, int an, long idcontract) {
        return ticheteService.getNrTichete(luna, an, idcontract);
    }

    public RealizariRetineri getRealizariRetineri(int luna, int an, long idcontract) {
      int nrTichete = getNrTichete(luna, an, idcontract);
      int zileCO = coService.getZileCO(luna, an, idcontract);
      int zileCM = cmService.getZileCM(luna, an, idcontract);
      return new RealizariRetineri(nrTichete);
    }

}
