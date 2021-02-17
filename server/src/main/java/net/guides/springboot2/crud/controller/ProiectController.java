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
	public List<Proiect> findAll() {
		return proiectService.findAll();
	}

	@GetMapping("{id}")
	public Proiect findById(@PathVariable("id") int id) throws ResourceNotFoundException {
		return proiectService.findById(id);
	}

	@GetMapping("ids={ids}")
	public List<Proiect> findBySocietate_Id(@PathVariable("ids") int id) {
		return proiectService.findBySocietate_Id(id);
	}

	@PostMapping("ida={ida}")
	public Proiect create(@PathVariable("ida") int idactivitate, @RequestBody Proiect proiect) throws ResourceNotFoundException {
		return proiectService.save(proiect, idactivitate);
	}

	@PutMapping("ida={ida}/{id}")
	public Proiect update(@PathVariable("ida") int idactivitate, @PathVariable("id") int id, @RequestBody Proiect proiect)
			throws ResourceNotFoundException {
		return proiectService.update(proiect, id, idactivitate);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> delete(@PathVariable("id") int id) throws ResourceNotFoundException {
		return proiectService.delete(id);
	}
}
