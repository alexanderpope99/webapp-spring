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
import net.guides.springboot2.crud.model.Sarbatori;
import net.guides.springboot2.crud.repository.SarbatoriRepository;
import net.guides.springboot2.crud.services.SarbatoriService;

@RestController
@RequestMapping("/sarbatori")
public class SarbatoriController {
	@Autowired
	private SarbatoriService sarbatoriService;
	@Autowired
	private SarbatoriRepository sarbatoriRepository;

	@GetMapping
	public List<Sarbatori> getAllSarbatori() {
		return sarbatoriRepository.findAllByOrderByDelaAsc();
	}

	@GetMapping("{id}")
	public ResponseEntity<Sarbatori> getSarbatoriById(@PathVariable(value = "id") Long id)
			throws ResourceNotFoundException {
		Sarbatori sarbatori = sarbatoriRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sarbatori not found for this id :: " + id));

		return ResponseEntity.ok().body(sarbatori);
	}

	@PostMapping
	public Sarbatori createSarbatori(@RequestBody Sarbatori sarbatori) {
		return sarbatoriRepository.save(sarbatori);
	}

	@PostMapping("init")
	public List<Sarbatori> initializeSarbatori() {
		sarbatoriService.initializeKnown();

		return sarbatoriRepository.findAllByOrderByDelaAsc();
	}

	@PostMapping("init/{an}")
	public void initializeAn(@PathVariable("id") int an) {
		sarbatoriService.initialize(an);
	}

	@PutMapping("{id}")
	public ResponseEntity<Sarbatori> updateSarbatori(@PathVariable(value = "id") Long id,
			@RequestBody Sarbatori newSarbatori) throws ResourceNotFoundException {
		Sarbatori sarbatori = sarbatoriRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sarbatori not found for this id :: " + id));

		newSarbatori.setId(sarbatori.getId());
		final Sarbatori updatedSarbatori = sarbatoriRepository.save(newSarbatori);
		return ResponseEntity.ok(updatedSarbatori);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteSarbatori(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
		Sarbatori sarbatori = sarbatoriRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sarbatori not found for this id :: " + id));

		sarbatoriRepository.delete(sarbatori);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
