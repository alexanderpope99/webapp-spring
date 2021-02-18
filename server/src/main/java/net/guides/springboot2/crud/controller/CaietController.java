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
import net.guides.springboot2.crud.model.Caiet;
import net.guides.springboot2.crud.services.CaietService;

@RestController
@RequestMapping("/caiet")
public class CaietController {
	@Autowired
	private CaietService caietService;

	@GetMapping
	public List<Caiet> findAll() {
		return caietService.findAll();
	}

	@GetMapping("{id}")
	public Caiet findById(@PathVariable("id") int id) throws ResourceNotFoundException {
		return caietService.findById(id);
	}

	@GetMapping("ids={ids}")
	public List<Caiet> findBySocietate_Id(@PathVariable("ids") int idsocietate) throws ResourceNotFoundException {
		return caietService.findBySocietate_Id(idsocietate);
	}

	@PostMapping("ids={ids}")
	public Caiet create(@PathVariable("ids") int idsocietate, @RequestBody Caiet caiet) throws ResourceNotFoundException {
		return caietService.save(caiet, idsocietate);
	}

	@PutMapping("ids={ids}/{id}")
	public Caiet update(@PathVariable("ids") int idsocietate, @PathVariable("id") int id, @RequestBody Caiet caiet) throws ResourceNotFoundException {
		return caietService.update(caiet, id, idsocietate);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteCaiet(@PathVariable("id") int id) throws ResourceNotFoundException {
		return caietService.delete(id);
	}
}
