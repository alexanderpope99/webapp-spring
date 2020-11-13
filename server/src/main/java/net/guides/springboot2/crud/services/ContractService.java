package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.ContractRepository;

@Service
public class ContractService {
	@Autowired
	ContractRepository contractRepository;
	@Autowired
	AngajatRepository angajatRepository;

	public Contract getContractById(int idcontract) throws ResourceNotFoundException {
		return contractRepository.findById(idcontract)
				.orElseThrow(() -> new ResourceNotFoundException("Contract not found for this id :: " + idcontract));
	}

	public Contract saveForAngajat(Contract contract, int idangajat) throws ResourceNotFoundException {
		contract = contractRepository.save(contract);
		Angajat angajat = angajatRepository.findById(idangajat)
			.orElseThrow(() -> new ResourceNotFoundException("Contract not found for this id :: " + idangajat));
		angajat.setContract(contract);
		angajatRepository.save(angajat);

		return contract;
	}
}
