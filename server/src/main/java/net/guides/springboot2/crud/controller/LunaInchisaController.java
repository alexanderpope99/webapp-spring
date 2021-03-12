package net.guides.springboot2.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.LunaInchisa;
import net.guides.springboot2.crud.services.LunaInchisaService;

@RestController
@RequestMapping("luna-inchisa")
public class LunaInchisaController {
	
	@Autowired
	private LunaInchisaService service;

	@GetMapping
	public List<LunaInchisa> findAll() {
		return service.findAll();
	}

	@GetMapping("{id}")
	public LunaInchisa findById(@PathVariable("id") int id) throws ResourceNotFoundException {
		return service.findById(id);
	}

	@GetMapping("ids={ids}")
	public List<LunaInchisa> findBySocitate_Id(@PathVariable("ids") int idsocietate) {
		return service.findBySocietate_Id(idsocietate);
	}

	@GetMapping("{mo}&{y}&{ids}")
	public LunaInchisa findByLunaAnSocietate_Id(
		@PathVariable("mo") int luna,
		@PathVariable("an") int an,
		@PathVariable("ids") int idsocietate
	) throws ResourceNotFoundException {
		return service.findByLunaAnSocietate_Id(luna, an, idsocietate);
	}

	@PostMapping("ids={ids}")
	public LunaInchisa save(@PathVariable("ids") int idsocietate, @RequestBody LunaInchisa newItem) throws ResourceNotFoundException {
		return service.save(newItem, idsocietate);
	}

	@DeleteMapping("{id}")
	public Boolean delete(@PathVariable("id") int id) throws ResourceNotFoundException {
		return service.delete(id);
	}

	@DeleteMapping("{mo}/{y}/{ids}")
	public Boolean deleteByLunaAnSocietate_Id(
		@PathVariable("mo") int luna,
		@PathVariable("y") int an,
		@PathVariable("ids") int idsocietate
	) throws ResourceNotFoundException {
		return service.delete(luna, an, idsocietate);
	}
}
