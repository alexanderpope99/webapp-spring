package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class SocietateService {
	@Autowired
	private SocietateRepository societateRepository;

	public void delete(int id) throws ResourceNotFoundException {
		// conturi bancare, angajati, facturi, centrecost, useri
		Societate societate = societateRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + id));
		
		societate.setAngajati(null);
		societate.setFacturi(null);
		societate.setUseri(null);
		
		societateRepository.delete(societate);
	}
}
