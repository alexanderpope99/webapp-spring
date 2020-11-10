package net.guides.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
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

import net.guides.springboot2.crud.dto.PersoanaIntretinereDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.PersoanaIntretinere;
import net.guides.springboot2.crud.repository.PersoanaIntretinereRepository;
import net.guides.springboot2.crud.services.PersoanaIntretinereService;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/persoanaintretinere")
public class PersoanaIntretinereController {
	@Autowired
	private PersoanaIntretinereRepository persoanaIntretinereRepository;
	@Autowired
	private PersoanaIntretinereService persoanaintretinereService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	public List<PersoanaIntretinere> getAllPersoane() {
		return persoanaIntretinereRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<PersoanaIntretinere> getPersoanaIntretinereById(@PathVariable(value = "id") int id)
			throws ResourceNotFoundException {
		PersoanaIntretinere persoanaIntretinere = persoanaIntretinereRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("PersoanaIntretinere not found for this id :: " + id));

		return ResponseEntity.ok().body(persoanaIntretinere);
	}

	@GetMapping("ida={id}")
	public List<PersoanaIntretinere> getPersoanaIntretinereByIdangajat(@PathVariable(value = "id") int id) {
		return persoanaIntretinereRepository.findByIdangajatOrderByNumeAscPrenumeAsc(id);
	}

	@PostMapping
	public PersoanaIntretinereDTO createPersoanaIntretinere(@RequestBody PersoanaIntretinereDTO persoanaIntretinereDTO)
			throws ResourceNotFoundException {
		return persoanaintretinereService.save(persoanaIntretinereDTO);
	}

	@PutMapping("{id}")
	public ResponseEntity<PersoanaIntretinereDTO> updatePersoanaIntretinere(@PathVariable(value = "id") int id,
			@RequestBody PersoanaIntretinereDTO piDTO) throws ResourceNotFoundException {
		return ResponseEntity.ok().body(persoanaintretinereService.update(id, piDTO));
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deletePersoanaIntretinere(@PathVariable(value = "id") int id)
			throws ResourceNotFoundException {
		PersoanaIntretinere persoanaIntretinere = persoanaIntretinereRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("PersoanaIntretinere not found for this id :: " + id));

		persoanaIntretinereRepository.delete(persoanaIntretinere);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
