package net.guides.springboot2.crud.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import net.guides.springboot2.crud.model.ParametriiSalariu;
import net.guides.springboot2.crud.repository.ParametriiSalariuRepository;
import net.guides.springboot2.crud.services.ParametriiSalariuService;

import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/parametriisalariu")
public class ParametriiSalariuController {
	@Autowired
	private ParametriiSalariuRepository parametriiSalariuRepository;

	@Autowired
	private ParametriiSalariuService parametriiSalariuService;

	@GetMapping
	public List<ParametriiSalariu> getAllPersoane() {
		return parametriiSalariuRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
	}

	@GetMapping("/date/{date}")
	public ParametriiSalariu getParametriiSalariuByDate(
			@PathVariable(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		return parametriiSalariuRepository.findByDate(date);
	}

	@GetMapping("{id}")
	public Optional<ParametriiSalariu> getParametriiSalariuById(@PathVariable(value = "id") int id) {
		return parametriiSalariuRepository.findById(id);
	}

	@PostMapping
	public ParametriiSalariu createParametriiSalariu(@RequestBody ParametriiSalariu parametriiSalariu) {
		return parametriiSalariuRepository.save(parametriiSalariu);
	}

	@PostMapping("init")
	public String init() {
		parametriiSalariuService.init();
		return "Parametrii initializati";
	}

	@PutMapping("{id}")
	public ResponseEntity<ParametriiSalariu> updateParametriiSalariu(@PathVariable(value = "id") int id,
			@RequestBody ParametriiSalariu newParametriiSalariu) throws ResourceNotFoundException {
		ParametriiSalariu parametriiSalariu = parametriiSalariuRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ParametriiSalariu not found for this id :: " + id));

		newParametriiSalariu.setId(parametriiSalariu.getId());
		final ParametriiSalariu updatedParametriiSalariu = parametriiSalariuRepository.save(newParametriiSalariu);
		return ResponseEntity.ok(updatedParametriiSalariu);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteParametriiSalariu(@PathVariable(value = "id") int id)
			throws ResourceNotFoundException {
		ParametriiSalariu parametriiSalariu = parametriiSalariuRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ParametriiSalariu not found for this id :: " + id));

		parametriiSalariuRepository.delete(parametriiSalariu);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
