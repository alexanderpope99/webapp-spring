package net.guides.springboot2.crud.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Caiet;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.CaietRepository;

@Service
public class CaietService {
	@Autowired
	private CaietRepository caietRepository;

	@Autowired
	private SocietateService societateService;

	public List<Caiet> findAll() {
		return caietRepository.findAll(Sort.by(Sort.Direction.ASC, "serie"));
	}

	public Caiet findById(int id) throws ResourceNotFoundException {
		return caietRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Caiet not found for this id :: " + id));
	}

	public Caiet findBySocietate_Id(int idsocietate) {
		return caietRepository.findBySocietate_Id(idsocietate);
	}

	public Caiet findBySerie(String serie) {
		return caietRepository.findBySerie(serie);
	}

	public Caiet save(Caiet newCaiet, int idsocietate) throws ResourceNotFoundException {
		Societate societate = societateService.findById(idsocietate);
		newCaiet.setSocietate(societate);

		return caietRepository.save(newCaiet);
	}

	public Caiet update(Caiet newCaiet, int id, int idsocietate) throws ResourceNotFoundException {
		Caiet caiet = findById(id);

		return save(caiet.update(newCaiet), idsocietate);
	}

	public Map<String, Boolean> delete(int id) throws ResourceNotFoundException {
		Caiet caiet = findById(id);

		caiet.detachFacturi();

		caietRepository.delete(caiet);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
