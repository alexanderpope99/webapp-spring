package net.guides.springboot2.crud.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Activitate;
import net.guides.springboot2.crud.model.Proiect;
import net.guides.springboot2.crud.repository.ProiectRepository;

@Service
public class ProiectService {

	@Autowired
	private ProiectRepository proiectRepository;

	@Autowired
	private ActivitateService activitateService;

	public List<Proiect> findAll() {
		return proiectRepository.findAll(Sort.by(Sort.Direction.ASC, "nume"));
	}

	public Proiect findById(int id) throws ResourceNotFoundException {
		return proiectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Proiect not found for id :: " + id));
	}

	public List<Proiect> findBySocietate_Id(int idsocietate) {
		return proiectRepository.findByActivitate_Societate_Id(idsocietate);
	}

	public Proiect save(Proiect newProiect, int idactivitate) throws ResourceNotFoundException {
		Activitate activitate = activitateService.findById(idactivitate);
		newProiect.setActivitate(activitate);
		return proiectRepository.save(newProiect);
	}

	public Proiect update(Proiect newProiect, int id, int idactivitate) throws ResourceNotFoundException {
		Proiect proiect = findById(id);
		return save(proiect.update(newProiect), idactivitate);
	}

	public Map<String, Boolean> delete(int id) throws ResourceNotFoundException {
		Proiect proiect = findById(id);
		proiect.getFacturi().forEach(factura -> factura.setProiect(null));

		proiectRepository.delete(proiect);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
