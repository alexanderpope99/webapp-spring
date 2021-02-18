package net.guides.springboot2.crud.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Activitate;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.ActivitateRepository;

@Service
public class ActivitateService {
	@Autowired
	private ActivitateRepository activitateRepository;

	@Autowired
	private SocietateService societateService;

	public List<Activitate> findAll() {
		return activitateRepository.findAll(Sort.by(Sort.Direction.ASC, "nume"));
	}

	public Activitate findById(int id) throws ResourceNotFoundException {
		return activitateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Activitate not found for this id :: " + id));
	}

	public List<Activitate> findBySocietate_Id(int id) {
		return activitateRepository.findBySocietate_IdOrderByNume(id);
	}

	public Activitate save(Activitate newActivitate, int idsocietate) throws ResourceNotFoundException {
		Societate societate = societateService.findById(idsocietate);
		newActivitate.setSocietate(societate);
		return activitateRepository.save(newActivitate);
	}

	public Activitate update(Activitate newActivitate, int id) throws ResourceNotFoundException {
		Activitate activitate = findById(id);
		return save(activitate.update(newActivitate), activitate.getSocietate().getId());
	}

	public Map<String, Boolean> delete(int id) throws ResourceNotFoundException {
		Activitate activitate = findById(id);

		activitate.detachProiecte();

		activitateRepository.delete(activitate);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
