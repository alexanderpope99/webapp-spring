package net.guides.springboot2.crud.services;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.IstoricContract;
import net.guides.springboot2.crud.payload.request.NewContractRequest;
import net.guides.springboot2.crud.repository.IstoricContractRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IstoricContractService {
    private final IstoricContractRepository repository;
    private final ContractService contractService;
    private final AngajatService angajatService;

    public IstoricContractService(IstoricContractRepository repository,
                                  ContractService contractService,
                                  AngajatService angajatService) {
        this.repository = repository;
        this.contractService = contractService;
        this.angajatService = angajatService;
    }

    public List<IstoricContract> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "dataModificarii"));
    }

    public IstoricContract findById(int id) throws ResourceNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find istoric contract for id :: " + id));
    }

    public List<IstoricContract> findByAngajat_Id(int id) {
        return repository.findByAngajat_Persoana_IdOrderByDataModificariiDesc(id);
    }

    public IstoricContract save(IstoricContract istoricContract) throws ResourceNotFoundException {
        Contract contract = contractService.findById(istoricContract.getContract().getId());
        Angajat angajat = angajatService.findById(istoricContract.getAngajat().getPersoana().getId());

        istoricContract.setContract(contract);
        istoricContract.setAngajat(angajat);

        return repository.save(istoricContract);
    }

    public IstoricContract update(IstoricContract newIstoric) throws ResourceNotFoundException {
        IstoricContract istoricContract = findById(newIstoric.getId());
        return repository.save(istoricContract.update(newIstoric));
    }

    public Contract addNewContract(NewContractRequest newContractRequest) throws ResourceNotFoundException {
        Angajat angajat = angajatService.findById(newContractRequest.getIdangajat());

        Contract oldContract = new Contract(angajat.getContract());
        oldContract = contractService.save(oldContract);


        this.save(new IstoricContract(angajat, oldContract, newContractRequest.getDataModificarii()));

        Contract newContract = newContractRequest.getContract();
        newContract.setId(angajat.getContract().getId());
        return contractService.save(newContract);
    }

    public void delete(int id) throws ResourceNotFoundException {
        IstoricContract istoricContract = findById(id);
        istoricContract.setContract(null);
        istoricContract.setAngajat(null);
        repository.delete(istoricContract);
    }

}
