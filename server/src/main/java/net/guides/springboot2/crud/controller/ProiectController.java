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

import net.guides.springboot2.crud.dto.ProiectDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Proiect;
import net.guides.springboot2.crud.repository.ProiectRepository;
import net.guides.springboot2.crud.services.ProiectService;

@RestController
@RequestMapping("/proiect")
public class ProiectController {
	@Autowired
	private ProiectRepository proiectRepository;
	@Autowired
	private ProiectService proiectService;

	@GetMapping
	public List<Proiect> getAll() {
		return proiectRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<Proiect> getProiectById(@PathVariable("id") int id) throws ResourceNotFoundException {
		Proiect proiect = proiectRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Proiect not found for this id :: " + id));

		return ResponseEntity.ok().body(proiect);
	}

	@PostMapping
	public ProiectDTO createProiect(@RequestBody ProiectDTO proiectDTO) throws ResourceNotFoundException {
		return proiectService.save(proiectDTO);
	}

	@PutMapping("{id}")
	public ResponseEntity<ProiectDTO> updateProiect(@PathVariable("id") int prId, @RequestBody ProiectDTO prDTO)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(proiectService.update(prId, prDTO));
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteProiect(@PathVariable("id") int id) throws ResourceNotFoundException {
		Proiect proiect = proiectRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Proiect not found for this id :: " + id));

		proiectRepository.delete(proiect);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
