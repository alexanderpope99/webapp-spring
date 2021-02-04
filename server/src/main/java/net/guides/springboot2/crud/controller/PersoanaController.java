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

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.repository.PersoanaRepository;

@RestController
@RequestMapping("/persoana")
public class PersoanaController {
	@Autowired
	private PersoanaRepository persoanaRepository;

	@GetMapping("/sortbyid")
	public List<Persoana> getAllPersoaneAlphabetically() {
		return persoanaRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping
	public List<Persoana> getAllPersoane() {
		return persoanaRepository.findAll(Sort.by(Sort.Order.asc("nume"), Sort.Order.asc("prenume")));

	}

	@GetMapping("{id}")
	public ResponseEntity<Persoana> getPersoanaById(@PathVariable("id") int id) throws ResourceNotFoundException {
		Persoana persoana = persoanaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Persoana not found for this id :: " + id));

		return ResponseEntity.ok().body(persoana);
	}

	@GetMapping("ids={id}&c")
	public List<Persoana> getPersoanaByIdsocietate(@PathVariable("id") int idsocietate) {
		return persoanaRepository.findByIdsocietateWithContract(idsocietate);
	}

	@GetMapping("ids={id}")
	public List<Persoana> getPersoanaByIdsocietateNoC(@PathVariable("id") int idsocietate) {
		return persoanaRepository.findByIdsocietate(idsocietate);
	}

	@PostMapping
	public Persoana createPersoana(@RequestBody Persoana persoana) {
		return persoanaRepository.save(persoana);
	}

	@PutMapping("{id}")
	public ResponseEntity<Persoana> updatePersoana(@PathVariable("id") int id, @RequestBody Persoana newPersoana)
			throws ResourceNotFoundException {
		Persoana persoana = persoanaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Persoana not found for this id :: " + id));

		newPersoana.setId(persoana.getId());
		final Persoana updatedPersoana = persoanaRepository.save(newPersoana);
		return ResponseEntity.ok(updatedPersoana);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deletePersoana(@PathVariable("id") int id) throws ResourceNotFoundException {
		Persoana persoana = persoanaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Persoana not found for this id :: " + id));

		persoanaRepository.delete(persoana);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
