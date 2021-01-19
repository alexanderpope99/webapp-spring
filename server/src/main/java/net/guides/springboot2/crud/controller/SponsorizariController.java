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
import net.guides.springboot2.crud.model.Sponsorizari;
import net.guides.springboot2.crud.repository.SponsorizariRepository;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/sponsorizari")
public class SponsorizariController {
	@Autowired
	private SponsorizariRepository sponsorizariRepository;

	@GetMapping
	public List<Sponsorizari> getAllPersoane() {
		return sponsorizariRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<Sponsorizari> getSponsorizariById(@PathVariable(value = "id") int id)
			throws ResourceNotFoundException {
		Sponsorizari sponsorizari = sponsorizariRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sponsorizari not found for this id :: " + id));

		return ResponseEntity.ok().body(sponsorizari);
	}

	@PostMapping
	public Sponsorizari createSponsorizari(@RequestBody Sponsorizari sponsorizari) {
		return sponsorizariRepository.save(sponsorizari);
	}

	@PutMapping("{id}")
	public ResponseEntity<Sponsorizari> updateSponsorizari(@PathVariable(value = "id") int id,
			@RequestBody Sponsorizari newSponsorizari) throws ResourceNotFoundException {
		Sponsorizari sponsorizari = sponsorizariRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sponsorizari not found for this id :: " + id));

		newSponsorizari.setId(sponsorizari.getId());
		final Sponsorizari updatedSponsorizari = sponsorizariRepository.save(newSponsorizari);
		return ResponseEntity.ok(updatedSponsorizari);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteSponsorizari(@PathVariable(value = "id") int id)
			throws ResourceNotFoundException {
		Sponsorizari sponsorizari = sponsorizariRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sponsorizari not found for this id :: " + id));

		sponsorizariRepository.delete(sponsorizari);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
