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
import net.guides.springboot2.crud.model.CereriConcediu;
import net.guides.springboot2.crud.repository.CereriConcediuRepository;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/cerericoncediu")
public class CereriConcediuController {
	@Autowired
	private CereriConcediuRepository cereriConcediuRepository;

	@GetMapping
	public List<CereriConcediu> getAllCereriConcediu() {
		return cereriConcediuRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<CereriConcediu> getCentruCostById(@PathVariable(value = "id") Long cereriConcediuId)
			throws ResourceNotFoundException {
		CereriConcediu cereriConcediu = cereriConcediuRepository.findById(cereriConcediuId).orElseThrow(
				() -> new ResourceNotFoundException("CereriConcediu not found for this id :: " + cereriConcediuId));
		return ResponseEntity.ok().body(cereriConcediu);
	}

	@PostMapping
	public CereriConcediu createCereriConcediu(@RequestBody CereriConcediu cereriConcediu) {
		return cereriConcediuRepository.save(cereriConcediu);
	}

	@PutMapping("{id}")
	public ResponseEntity<CereriConcediu> updateCereriConcediu(@PathVariable(value = "id") Long cereriConcediuId,
			@RequestBody CereriConcediu cereriConcediuDetails) throws ResourceNotFoundException {
		CereriConcediu cereriConcediu = cereriConcediuRepository.findById(cereriConcediuId).orElseThrow(
				() -> new ResourceNotFoundException("CentruCost not found for this id :: " + cereriConcediuId));

		cereriConcediuDetails.setId(cereriConcediu.getId());
		final CereriConcediu updatedCereriConcediu = cereriConcediuRepository.save(cereriConcediu);
		return ResponseEntity.ok(updatedCereriConcediu);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteCereriConcediu(@PathVariable(value = "id") Long cereriConcediuId)
			throws ResourceNotFoundException {
		CereriConcediu cereriConcediu = cereriConcediuRepository.findById(cereriConcediuId).orElseThrow(
				() -> new ResourceNotFoundException("CereriConcediu not found for this id :: " + cereriConcediuId));

		cereriConcediuRepository.delete(cereriConcediu);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
