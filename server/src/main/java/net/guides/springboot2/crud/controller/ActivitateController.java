package net.guides.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.dto.ActivitateDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Activitate;
import net.guides.springboot2.crud.repository.ActivitateRepository;
import net.guides.springboot2.crud.services.ActivitateService;

@RestController
@RequestMapping("/activitate")
public class ActivitateController {
	@Autowired
	private ActivitateRepository activitateRepository;
	@Autowired
	private ActivitateService activitateService;

	@GetMapping
	public List<Activitate> getAll() {
		return activitateRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<Activitate> getActivitateById(@PathVariable("id") int id) throws ResourceNotFoundException {
		Activitate activitate = activitateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Activitate not found for this id :: " + id));

		return ResponseEntity.ok().body(activitate);
	}


	@PostMapping
	public ActivitateDTO createActivitate(@RequestBody ActivitateDTO activitateDTO) throws ResourceNotFoundException {
		return activitateService.save(activitateDTO);
	}

	@PutMapping("{id}")
	public ResponseEntity<ActivitateDTO> updateActivitate(@PathVariable("id") int acId, @RequestBody ActivitateDTO acDTO)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(activitateService.update(acId, acDTO));
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteActivitate(@PathVariable("id") int id) throws ResourceNotFoundException {
		Activitate activitate = activitateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Activitate not found for this id :: " + id));

		activitateRepository.delete(activitate);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
