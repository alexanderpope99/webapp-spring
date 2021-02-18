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

import net.guides.springboot2.crud.dto.ProdusDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Produs;
import net.guides.springboot2.crud.repository.ProdusRepository;
import net.guides.springboot2.crud.services.ProdusService;

@RestController
@RequestMapping("/produs")
public class ProdusController {
	@Autowired
	private ProdusRepository produsRepository;
	@Autowired
	private ProdusService produsService;

	@GetMapping
	public List<Produs> getAll() {
		return produsRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<Produs> getProdusById(@PathVariable("id") int id) throws ResourceNotFoundException {
		Produs produs = produsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produs not found for this id :: " + id));

		return ResponseEntity.ok().body(produs);
	}

	@PostMapping
	public Produs createProdus(@RequestBody ProdusDTO produsDTO) {
		return produsService.save(produsDTO);
	}

	@PutMapping("{id}")
	public ResponseEntity<Produs> updateProdus(@PathVariable("id") int acId, @RequestBody ProdusDTO acDTO) {
		return ResponseEntity.ok(produsService.update(acId, acDTO));
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteProdus(@PathVariable("id") int id) throws ResourceNotFoundException {
		Produs produs = produsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produs not found for this id :: " + id));

		produsRepository.delete(produs);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
