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
import net.guides.springboot2.crud.model.CentruCost;
import net.guides.springboot2.crud.repository.CentruCostRepository;
import net.guides.springboot2.crud.services.CentruCostService;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/centrucost")
public class CentruCostController {
	@Autowired
	private CentruCostRepository centruCostRepository;

	@Autowired
	private CentruCostService ccService;

	@GetMapping
	public List<CentruCost> getAllCentruCosts() {
		return centruCostRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<CentruCost> getCentruCostById(@PathVariable(value = "id") int centruCostId)
			throws ResourceNotFoundException {
		CentruCost centruCost = centruCostRepository.findById(centruCostId)
				.orElseThrow(() -> new ResourceNotFoundException("CentruCost not found for this id :: " + centruCostId));
		return ResponseEntity.ok().body(centruCost);
	}

	@GetMapping("/ids={id}")
	public List<CentruCost> getCentruCostByIdsocietate(@PathVariable(value = "id") int societateId) {
		return centruCostRepository.findCentreCostByIdsocietate(societateId);
	}

	@PostMapping("/ids={ids}")
	public ResponseEntity<CentruCost> createCentruCost(@PathVariable("ids") int ids,
			@RequestBody CentruCost centruCost) throws ResourceNotFoundException {
		return ResponseEntity.ok(ccService.save(centruCost, ids, false));
	}

	@PostMapping("/ids={ids}/adrsoc")
	public ResponseEntity<CentruCost> createCentruCostAdresaSocietate(@PathVariable("ids") int ids,
			@RequestBody CentruCost centruCost) throws ResourceNotFoundException {
		return ResponseEntity.ok(ccService.save(centruCost, ids, true));
	}

	@PutMapping("{id}")
	public ResponseEntity<CentruCost> updateCentruCost(@PathVariable("id") int id, @RequestBody CentruCost newCentruCost)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(ccService.update(newCentruCost, id, false));
	}

	@PutMapping("{id}/adrsoc")
	public ResponseEntity<CentruCost> updateCentruCostAdresaSocietate(@PathVariable("id") int id, @RequestBody CentruCost newCentruCost)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(ccService.update(newCentruCost, id, true));
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteCentruCost(@PathVariable(value = "id") int centruCostId)
			throws ResourceNotFoundException {
		CentruCost centruCost = centruCostRepository.findById(centruCostId)
				.orElseThrow(() -> new ResourceNotFoundException("CentruCost not found for this id :: " + centruCostId));

		centruCostRepository.delete(centruCost);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
