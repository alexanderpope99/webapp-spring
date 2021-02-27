package net.guides.springboot2.crud.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.Suspendare;
import net.guides.springboot2.crud.repository.SuspendareRepository;

@Service
public class SuspendareService {
	@Autowired
	private SuspendareRepository suspendareRepository;

	@Autowired
	private ContractService contractService;

	public List<Suspendare> findAll(){
		return suspendareRepository.findAll(Sort.by(Sort.Direction.ASC,"dela"));
	} 

	public Suspendare findById(int id) throws ResourceNotFoundException{
		return suspendareRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Suspendare not found for this id: "+id));
	} 

	public List<Suspendare> findByContractId(int id) throws ResourceNotFoundException{
		Contract contract=contractService.findById(id);
		return contract.getSuspendari();
	}

	public Suspendare save(Suspendare suspendare, int idcontract) throws ResourceNotFoundException {
		Contract contract=contractService.findById(idcontract);
		suspendare.setContract(contract);
		return suspendareRepository.save(suspendare);
	}

	public Suspendare update(int id, Suspendare newSuspendare) throws ResourceNotFoundException {
		Suspendare suspendare=findById(id);
		suspendare.update(newSuspendare);
		return suspendareRepository.save(suspendare);
	}

	public Map<String,Boolean> delete(int id) throws ResourceNotFoundException {
		Suspendare suspendare=findById(id);
		suspendareRepository.delete(suspendare);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
