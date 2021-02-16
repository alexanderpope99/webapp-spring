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
import net.guides.springboot2.crud.model.Activitate;
import net.guides.springboot2.crud.services.ActivitateService;

@RestController
@RequestMapping("/activitate")
public class ActivitateController {
	@Autowired
	private ActivitateService activitateService;

	@GetMapping
	public List<Activitate> getAll() {
		return activitateService.findAll();
	}

	@GetMapping("{id}")
	public Activitate getActivitateById(@PathVariable("id") int id) throws ResourceNotFoundException {
		return activitateService.findById(id);
	}

	@GetMapping("ids={ids}")
	public List<Activitate> findBySocietate_Id(@PathVariable("ids") int idsocietate) throws ResourceNotFoundException {
		return activitateService.findBySocietate_Id(idsocietate);
	}

	@PostMapping("ids={ids}")
	public Activitate createActivitate(@PathVariable("ids") int idsocietate, @RequestBody Activitate activitate) throws ResourceNotFoundException {
		return activitateService.save(activitate, idsocietate);
	}

	@PutMapping("{id}")
	public Activitate updateActivitate(@PathVariable("id") int id, @RequestBody Activitate activitate) throws ResourceNotFoundException {
		return activitateService.update(activitate, id);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteActivitate(@PathVariable("id") int id) throws ResourceNotFoundException {
		return activitateService.delete(id);
	}
}
