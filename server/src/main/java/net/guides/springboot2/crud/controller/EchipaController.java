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
import net.guides.springboot2.crud.model.Echipa;
import net.guides.springboot2.crud.repository.EchipaRepository;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/echipa")
public class EchipaController {
	@Autowired
	private EchipaRepository echipaRepository;

	@GetMapping
	public List<Echipa> getAllEchipas() {
		return echipaRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<Echipa> getEchipaById(@PathVariable("id") int echipaId)
			throws ResourceNotFoundException {
		Echipa echipa = echipaRepository.findById(echipaId)
				.orElseThrow(() -> new ResourceNotFoundException("Echipa not found for this id :: " + echipaId));
		return ResponseEntity.ok().body(echipa);
	}

	@PostMapping
	public Echipa createEchipa(@RequestBody Echipa echipa) {
		return echipaRepository.save(echipa);
	}

	@PutMapping("{id}")
	public ResponseEntity<Echipa> updateEchipa(@PathVariable("id") int echipaId,
			@RequestBody Echipa echipaDetails) throws ResourceNotFoundException {
		Echipa echipa = echipaRepository.findById(echipaId)
				.orElseThrow(() -> new ResourceNotFoundException("Echipa not found for this id :: " + echipaId));

		echipaDetails.setId(echipa.getId());
		final Echipa updatedEchipa = echipaRepository.save(echipa);
		return ResponseEntity.ok(updatedEchipa);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteEchipa(@PathVariable("id") int echipaId)
			throws ResourceNotFoundException {
		Echipa echipa = echipaRepository.findById(echipaId)
				.orElseThrow(() -> new ResourceNotFoundException("Echipa not found for this id :: " + echipaId));

		echipaRepository.delete(echipa);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
