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

import net.guides.springboot2.crud.dto.ZileCOAnDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.ZileCOAn;
import net.guides.springboot2.crud.repository.ZileCOAnRepository;
import net.guides.springboot2.crud.services.ZileCOAnService;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/zilecoan")
public class ZileCOAnController {
	@Autowired
	private ZileCOAnRepository zileCOAnRepository;

	@Autowired
	private ZileCOAnService zileCOAnService;

	@GetMapping("ida={ida}")
	public List<ZileCOAn> getZileCOAnByIdAngajat(@PathVariable("ida") int ida) {
		return zileCOAnRepository.findByAngajat_IdpersoanaOrderByAnDesc(ida);
	}

	@GetMapping
	public List<ZileCOAn> getAllPersoane() {
		return zileCOAnRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<ZileCOAn> getZileCOAnById(@PathVariable(value = "id") int id)
			throws ResourceNotFoundException {
		ZileCOAn zileCOAn = zileCOAnRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ZileCOAn not found for this id :: " + id));

		return ResponseEntity.ok().body(zileCOAn);
	}

	@PostMapping
	public ZileCOAnDTO createBazacalcul(@RequestBody ZileCOAnDTO zilecoanDTO) throws ResourceNotFoundException {
		return zileCOAnService.save(zilecoanDTO);
	}

	@PutMapping("{id}")
	public ResponseEntity<ZileCOAnDTO> updateBazacalcul(@PathVariable(value = "id") int id,
			@RequestBody ZileCOAnDTO zilecoanDTO) throws ResourceNotFoundException {
		return ResponseEntity.ok().body(zileCOAnService.update(id, zilecoanDTO));
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteZileCOAn(@PathVariable(value = "id") int id)
			throws ResourceNotFoundException {
		ZileCOAn zileCOAn = zileCOAnRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ZileCOAn not found for this id :: " + id));

		zileCOAnRepository.delete(zileCOAn);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
