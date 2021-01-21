package net.guides.springboot2.crud.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.ContBancar;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.ContBancarRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class ContBancarService {
	@Autowired
	private ContBancarRepository contBancarRepository;

	@Autowired
	private SocietateRepository societateRepository;

	public ContBancar findById(int id) throws ResourceNotFoundException {
		return contBancarRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Nu există cont bancar cu id: " + id));
	}

	public ContBancar save(ContBancar newCont, int idsocietate) throws ResourceNotFoundException {
		Societate societate = societateRepository.findById(idsocietate)
				.orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));

		newCont.setSocietate(societate);

		return contBancarRepository.save(newCont);
	}

	public ContBancar update(ContBancar newCont, int idsocietate, int idcont) throws ResourceNotFoundException {
		newCont.setId(findById(idcont).getId());

		return save(newCont, idsocietate);
	}

	public Map<String, Boolean> delete(int id) throws ResourceNotFoundException {
		ContBancar contBancar = contBancarRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ContBancar not found for this id :: " + id));
		contBancar.setSocietate(null);
		contBancarRepository.delete(contBancar);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
