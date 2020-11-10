package net.guides.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.model.User;
import net.guides.springboot2.crud.repository.SocietateRepository;
import net.guides.springboot2.crud.repository.UserRepository;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/societate")
public class SocietateController {
	@Autowired
	private SocietateRepository societateRepository;
	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public List<Societate> getAll() {
		List<Societate> societati = societateRepository.findAll(Sort.by(Sort.Direction.ASC, "nume"));
		return societati;
	}

	@GetMapping("{id}")
	public ResponseEntity<Societate> getSocietateById(@PathVariable(value = "id") int id)
			throws ResourceNotFoundException {
		Societate societate = societateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + id));

		return ResponseEntity.ok().body(societate);
	}

	@GetMapping("/user/{id}")
	public List<Societate> getSocietateByUserId(@PathVariable(value = "id") Integer id)
			throws ResourceNotFoundException {
		return societateRepository.findByUserId(id);
	}

	@PostMapping
	public Societate createSocietate(@RequestBody Societate societate) {
		return societateRepository.save(societate);
	}

	@PostMapping("/{uid}")
	public Societate createSocietate(@RequestBody Societate societate, @PathVariable("uid") Long uid) {
		Societate newSoc = societateRepository.save(societate);
		User user = userRepository.findById(uid).orElseThrow(() -> new RuntimeException("Error"));
		Set<Societate> societati = user.getSocietati();
		societati.add(newSoc);
		user.setSocietati(societati);
		userRepository.save(user);

		return newSoc;
	}

	@PutMapping("{id}")
	public ResponseEntity<Societate> updateSocietate(@PathVariable(value = "id") int id,
			@RequestBody Societate newSocietate) throws ResourceNotFoundException {
		Societate societate = societateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + id));
		newSocietate.setId(societate.getId());
		final Societate updatedSocietate = societateRepository.save(newSocietate);

		return ResponseEntity.ok().body(updatedSocietate);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteSocietate(@PathVariable(value = "id") int id) throws ResourceNotFoundException {
		Societate societate = societateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + id));

		societateRepository.delete(societate);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
