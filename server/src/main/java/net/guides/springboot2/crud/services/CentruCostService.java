package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.CentruCost;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.CentruCostRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class CentruCostService {
	@Autowired
	private CentruCostRepository centruCostRepository;

	@Autowired
	private SocietateRepository societateRepository;

	public CentruCost save(CentruCost newCentruCost, int idsocietate, boolean adresaSocietatii)
			throws ResourceNotFoundException {
		Societate societate = societateRepository.findById(idsocietate)
				.orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));

		newCentruCost.setSocietate(societate);

		if (adresaSocietatii)
			newCentruCost.setAdresa(societate.getAdresa());

		return centruCostRepository.save(newCentruCost);
	}

	public CentruCost update(CentruCost newCentruCost, int id, boolean adresaSocietatii)
			throws ResourceNotFoundException {
		// get centrucost.societate.id
		CentruCost oldCentruCost = centruCostRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Nu există centru cost cu id: " + id));

		newCentruCost.setId(id);
		return this.save(newCentruCost, oldCentruCost.getSocietate().getId(), adresaSocietatii);
	}
}
