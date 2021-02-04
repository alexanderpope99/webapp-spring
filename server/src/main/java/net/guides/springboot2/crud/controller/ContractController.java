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
import net.guides.springboot2.crud.services.ContractService;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/contract")
public class ContractController {
	@Autowired
	private ContractRepository contractRepository;
	@Autowired
	private ContractService contractService;

	@GetMapping
	public List<Contract> getAllContracts() {
		return contractRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<Contract> findById(@PathVariable("id") int contractId)
			throws ResourceNotFoundException {
		Contract contract = contractRepository.findById(contractId)
				.orElseThrow(() -> new ResourceNotFoundException("Contract not found for this id :: " + contractId));
		return ResponseEntity.ok().body(contract);
	}

	@GetMapping("idp={id}")
	public ResponseEntity<Contract> findByIdPersoana(@PathVariable("id") int idpersoana)
			throws ResourceNotFoundException {
		Contract contract = contractRepository.findByIdPersoana(idpersoana).orElseThrow(
				() -> new ResourceNotFoundException("Contract not found for this idpersoana :: " + idpersoana));
		return ResponseEntity.ok().body(contract);
	}

	@GetMapping("/fix-missing-values")
	public boolean fixDefaultMissingValues() {
		return contractService.fixDefaultValuesMissing();
	}

	@PostMapping
	public Contract createContract(@RequestBody Contract contract) {
		return contractRepository.save(contract);
	}

	@PostMapping("{ida}")
	public Contract createContractForAngajat(@RequestBody Contract contract, @PathVariable("ida") int ida)
			throws ResourceNotFoundException {
		return contractService.saveForAngajat(contract, ida);
	}

	@PutMapping("{id}")
	public ResponseEntity<Contract> updateContract(@PathVariable("id") int contractId,
			@RequestBody Contract contractDetails) throws ResourceNotFoundException {
		Contract contract = contractRepository.findById(contractId)
				.orElseThrow(() -> new ResourceNotFoundException("Contract not found for this id :: " + contractId));

		contractDetails.setId(contract.getId());
		final Contract updatedContract = contractRepository.save(contractDetails);
		return ResponseEntity.ok(updatedContract);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteContract(@PathVariable("id") int contractId)
			throws ResourceNotFoundException {
		Contract contract = contractRepository.findById(contractId)
				.orElseThrow(() -> new ResourceNotFoundException("Contract not found for this id :: " + contractId));

		contractRepository.delete(contract);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
