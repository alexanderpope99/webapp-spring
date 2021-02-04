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
import net.guides.springboot2.crud.model.Prime;
import net.guides.springboot2.crud.repository.PrimeRepository;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/prime")
public class PrimeController {
	@Autowired
	private PrimeRepository primeRepository;

	@GetMapping
	public List<Prime> getAllPersoane() {
		return primeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<Prime> getPrimeById(@PathVariable("id") int id) throws ResourceNotFoundException {
		Prime prime = primeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Prime not found for this id :: " + id));

		return ResponseEntity.ok().body(prime);
	}

	@PostMapping
	public Prime createPrime(@RequestBody Prime prime) {
		return primeRepository.save(prime);
	}

	@PutMapping("{id}")
	public ResponseEntity<Prime> updatePrime(@PathVariable("id") int id, @RequestBody Prime newPrime)
			throws ResourceNotFoundException {
		Prime prime = primeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Prime not found for this id :: " + id));

		newPrime.setId(prime.getId());
		final Prime updatedPrime = primeRepository.save(newPrime);
		return ResponseEntity.ok(updatedPrime);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deletePrime(@PathVariable("id") int id) throws ResourceNotFoundException {
		Prime prime = primeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Prime not found for this id :: " + id));

		primeRepository.delete(prime);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
