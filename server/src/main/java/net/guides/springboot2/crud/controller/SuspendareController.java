package net.guides.springboot2.crud.controller;

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
import net.guides.springboot2.crud.model.Suspendare;
import net.guides.springboot2.crud.repository.SuspendareRepository;
import net.guides.springboot2.crud.services.SuspendareService;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/suspendare")
public class SuspendareController {
	@Autowired
	private SuspendareRepository suspendareRepository;

	@Autowired
	private SuspendareService suspendareService;

	@GetMapping
	public List<Suspendare> getAllSuspendari() {
		return suspendareRepository.findAll(Sort.by(Sort.Direction.DESC, "dela"));
	}

	@GetMapping("{id}")
	public ResponseEntity<Suspendare> getSuspendareById(@PathVariable("id") int suspendareId) throws ResourceNotFoundException {
		Suspendare suspendare = suspendareRepository.findById(suspendareId)
				.orElseThrow(() -> new ResourceNotFoundException("Suspendare not found for this id :: " + suspendareId));
		return ResponseEntity.ok().body(suspendare);
	}

	@PostMapping("/idc={id}")
	public Suspendare createSuspendare(@RequestBody Suspendare suspendare,@PathVariable("id") int id) throws ResourceNotFoundException {
		return suspendareService.save(suspendare,id);
	}

	@PutMapping("{id}")
	public Suspendare updateSuspendare(@PathVariable("id") int suspendareId, @RequestBody Suspendare suspendare)
			throws ResourceNotFoundException {
		return suspendareService.update(suspendareId, suspendare);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteSuspendare(@PathVariable("id") int suspendareId) throws ResourceNotFoundException {
		return suspendareService.delete(suspendareId);
	}
}
