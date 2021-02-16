package net.guides.springboot2.crud.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Proiect;
import net.guides.springboot2.crud.services.ProiectService;

@RestController
@RequestMapping("/proiect")
public class ProiectController {
	@Autowired
	private ProiectService proiectService;

	@GetMapping
	public List<Proiect> getAll() {
		return proiectService.findAll();
	}

	@GetMapping("{id}")
	public Proiect getProiectById(@PathVariable("id") int id) throws ResourceNotFoundException {
		return proiectService.findById(id);
	}

	@PostMapping
	public Proiect createProiect(@RequestBody Proiect proiect) throws ResourceNotFoundException {
		return proiectService.save(proiect);
	}

	@PutMapping("{id}")
	public Proiect updateProiect(@PathVariable("id") int id, @RequestBody Proiect proiect)
			throws ResourceNotFoundException {
		return proiectService.update(proiect, id);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteProiect(@PathVariable("id") int id) throws ResourceNotFoundException {
		return proiectService.delete(id);
	}
}
