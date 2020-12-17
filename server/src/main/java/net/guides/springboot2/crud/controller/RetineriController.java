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

import net.guides.springboot2.crud.dto.RetineriDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Retineri;
import net.guides.springboot2.crud.repository.RetineriRepository;
import net.guides.springboot2.crud.services.RetineriService;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/retineri")
public class RetineriController {
	@Autowired
	private RetineriRepository retineriRepository;

	@Autowired
	private RetineriService retineriService;

	// @Autowired
	// private ModelMapper modelMapper;

	@GetMapping
	public List<Retineri> getAllPersoane() {
		return retineriRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("ids={ids}")
	public Retineri getRetineriByIdstat(@PathVariable(value = "ids") int id) {
		return retineriRepository.findByStat_Id(id);
	}

	@GetMapping("{id}")
	public ResponseEntity<Retineri> getRetineriById(@PathVariable(value = "id") int id)
			throws ResourceNotFoundException {
		Retineri retineri = retineriRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Retineri not found for this id :: " + id));

		return ResponseEntity.ok().body(retineri);
	}

	@PostMapping
	public Retineri createRetineri(@RequestBody Retineri retineri) {
		return retineriRepository.save(retineri);
	}

	@PutMapping("{id}")
	public Retineri updateRetineri(@PathVariable(value = "id") int id, @RequestBody RetineriDTO newRetineriDTO)
			throws ResourceNotFoundException {
		newRetineriDTO.setId(id);
		return retineriService.updateRetinere(id, newRetineriDTO);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteRetineri(@PathVariable(value = "id") int id) throws ResourceNotFoundException {
		Retineri retineri = retineriRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Retineri not found for this id :: " + id));

		retineriRepository.delete(retineri);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
