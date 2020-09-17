package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.repository.ContractRepository;

@Service
public class ContractService {
    @Autowired
    ContractRepository contractRepository;

    public Contract getContractById(long idcontract) throws ResourceNotFoundException {
        return contractRepository.findById(idcontract).orElseThrow(
            () -> new ResourceNotFoundException("Contract not found for this id :: " + idcontract));
    }

    public Contract getContractByIdpersoana(long idpersoana) throws ResourceNotFoundException {
        return contractRepository.findByIdPersoana(idpersoana).orElseThrow(
            () -> new ResourceNotFoundException("Contract not found for this idpersoana :: " + idpersoana));
    }
}
