package net.guides.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.repository.ContractRepository;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/contract")
public class ContractController {
    @Autowired
    private ContractRepository contractRepository;

    @GetMapping
    public List<Contract> getAllContracts() {
        return contractRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @GetMapping("{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable(value = "id") Long contractId)
            throws ResourceNotFoundException {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found for this id :: " + contractId));
        return ResponseEntity.ok().body(contract);
    }

    @PostMapping
    public Contract createContract(@RequestBody Contract contract) {
        return contractRepository.save(contract);
    }

    @PutMapping("{id}")
    public ResponseEntity<Contract> updateContract(@PathVariable(value = "id") Long contractId,
            @RequestBody Contract contractDetails) throws ResourceNotFoundException {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found for this id :: " + contractId));

        contractDetails.setId(contract.getId());
        final Contract updatedContract = contractRepository.save(contract);
        return ResponseEntity.ok(updatedContract);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteContract(@PathVariable(value = "id") Long contractId)
            throws ResourceNotFoundException {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found for this id :: " + contractId));

        contractRepository.delete(contract);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
