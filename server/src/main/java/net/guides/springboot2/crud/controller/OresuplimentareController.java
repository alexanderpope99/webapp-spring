package net.guides.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Oresuplimentare;
import net.guides.springboot2.crud.repository.OresuplimentareRepository;
import net.guides.springboot2.crud.services.OresuplimentareService;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/oresuplimentare")
public class OresuplimentareController {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private OresuplimentareRepository oresuplimentareRepository;

	@Autowired
	private OresuplimentareService oresuplimentareService;

	@GetMapping
	public List<Oresuplimentare> getAllPersoane() {
		return oresuplimentareRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<Oresuplimentare> getOresuplimentareById(@PathVariable(value = "id") int id)
			throws ResourceNotFoundException {
		Oresuplimentare oresuplimentare = oresuplimentareRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Oresuplimentare not found for this id :: " + id));

		return ResponseEntity.ok().body(modelMapper.map(oresuplimentare, Oresuplimentare.class));
	}

	@GetMapping("/api/idc={id}&mo={luna}&y={an}")
	public ResponseEntity<List<Oresuplimentare>> getOresuplimentareByLunaAnIdcontract(
			@PathVariable(value = "id") int idcontract, @PathVariable(value = "luna") Integer luna,
			@PathVariable(value = "an") Integer an) {
		List<Oresuplimentare> oresuplimentare = oresuplimentareRepository.findByLunaAndAnAndIdcontract(luna, an, idcontract);

		return ResponseEntity.ok().body(oresuplimentare);
	}

	@GetMapping("idss={id}")
	public ResponseEntity<List<Oresuplimentare>> getOresuplimentareByIdstat(@PathVariable(value = "id") int idstat) {
		List<Oresuplimentare> oresuplimentare = oresuplimentareRepository.findByStatsalariat_Id(idstat).stream()
				.map(os -> modelMapper.map(os, Oresuplimentare.class)).collect(Collectors.toList());

		return ResponseEntity.ok().body(oresuplimentare);
	}

	@PostMapping
	public Oresuplimentare createOresuplimentare(@RequestBody Oresuplimentare oresuplimentare)
			throws ResourceNotFoundException {
		return oresuplimentareService.save(oresuplimentare);
	}

	@PutMapping("{id}")
	public ResponseEntity<Oresuplimentare> updateOresuplimentare(@PathVariable(value = "id") int id,
			@RequestBody Oresuplimentare newOresuplimentare) throws ResourceNotFoundException {
		return ResponseEntity.ok(oresuplimentareService.update(id, newOresuplimentare));
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteOresuplimentare(@PathVariable(value = "id") int id)
			throws ResourceNotFoundException {
		Oresuplimentare oresuplimentare = oresuplimentareRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Oresuplimentare not found for this id :: " + id));

		oresuplimentareRepository.delete(oresuplimentare);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
