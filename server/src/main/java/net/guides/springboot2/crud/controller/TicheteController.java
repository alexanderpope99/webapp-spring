package net.guides.springboot2.crud.controller;

import java.io.IOException;
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
import net.guides.springboot2.crud.model.Tichete;
import net.guides.springboot2.crud.repository.TicheteRepository;
import net.guides.springboot2.crud.services.TicheteService;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/tichete")
public class TicheteController {
	@Autowired
	private TicheteRepository ticheteRepository;
	@Autowired
	private TicheteService ticheteService;

	@GetMapping
	public List<Tichete> getAllTichete() {
		return ticheteRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<Tichete> getTicheteById(@PathVariable("id") int id) throws ResourceNotFoundException {
		Tichete tichete = ticheteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tichete not found for this id :: " + id));

		return ResponseEntity.ok().body(tichete);
	}

	@GetMapping("nr/idc={idc}&mo={luna}&y={an}")
	public int getNrTicheteByLunaAnIdcontract(@PathVariable("idc") int idcontract, @PathVariable("luna") int luna, @PathVariable("an") int an) throws ResourceNotFoundException {
		return ticheteService.getNrTichete(luna, an, idcontract);
	}

	@GetMapping("/raport/{ids}/mo={luna}&y={an}/{uid}")
	public boolean createNotaContabila(@PathVariable("ids") int ids, @PathVariable("luna") int luna, @PathVariable("an") int an, @PathVariable("uid") int uid) throws IOException, ResourceNotFoundException {
		return ticheteService.createRaportTichete(luna, an, ids, uid);
	}

	@PostMapping
	public Tichete createTichete(@RequestBody Tichete tichete) {
		return ticheteRepository.save(tichete);
	}

	@PutMapping("{id}")
	public ResponseEntity<Tichete> updateTichete(@PathVariable("id") int id, @RequestBody Tichete newTichete) throws ResourceNotFoundException {
		Tichete tichete = ticheteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tichete not found for this id :: " + id));

		newTichete.setId(tichete.getId());
		final Tichete updatedTichete = ticheteRepository.save(newTichete);
		return ResponseEntity.ok(updatedTichete);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteTichete(@PathVariable("id") int id) throws ResourceNotFoundException {
		Tichete tichete = ticheteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tichete not found for this id :: " + id));

		ticheteRepository.delete(tichete);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
