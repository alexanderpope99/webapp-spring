package net.guides.springboot2.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import net.guides.springboot2.crud.dto.SocietateDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.model.User;
import net.guides.springboot2.crud.repository.SocietateRepository;
import net.guides.springboot2.crud.repository.UserRepository;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/societate")
public class SocietateController {
	@Autowired
	private SocietateRepository societateRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	public List<SocietateDTO> getAllSocietati() {
		List<Societate> societati = societateRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		List<SocietateDTO> societatiDTO = new ArrayList<>();
		for (Societate soc : societati) {
			societatiDTO.add(modelMapper.map(soc, SocietateDTO.class));
		}
		return societatiDTO;
	}

	@GetMapping("{id}")
	public ResponseEntity<SocietateDTO> getSocietateById(@PathVariable(value = "id") int id)
			throws ResourceNotFoundException {
		Societate societate = societateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + id));

		return ResponseEntity.ok().body(modelMapper.map(societate, SocietateDTO.class));
	}

	@GetMapping("/user/{id}")
	public List<SocietateDTO> getSocietateByUserId(@PathVariable(value = "id") Integer id)
			throws ResourceNotFoundException {
		List<Societate> societati = societateRepository.findByUserId(id);

		List<SocietateDTO> societatiDTO = new ArrayList<>();
		for (Societate soc : societati) {
			societatiDTO.add(modelMapper.map(soc, SocietateDTO.class));
		}
		return societatiDTO;
	}

	@PostMapping
	public SocietateDTO createSocietate(@RequestBody SocietateDTO societate) {

		Societate soc = societateRepository.save(modelMapper.map(societate, Societate.class));
		return (modelMapper.map(soc, SocietateDTO.class));
	}

	@PostMapping("/{uid}")
	public SocietateDTO createSocietate(@RequestBody SocietateDTO societate, @PathVariable("uid") Long uid) {
		Societate newSoc = societateRepository.save(modelMapper.map(societate, Societate.class));
		User user = userRepository.findById(uid).orElseThrow(() -> new RuntimeException("Error"));
		Set<Societate> societati = user.getSocietati();
		societati.add(newSoc);
		user.setSocietati(societati);
		userRepository.save(user);

		return modelMapper.map(newSoc, SocietateDTO.class);
	}

	@PutMapping("{id}")
	public ResponseEntity<SocietateDTO> updateSocietate(@PathVariable(value = "id") int id,
			@RequestBody SocietateDTO newSoc) throws ResourceNotFoundException {
		Societate societate = societateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + id));
		Societate newSocietate = modelMapper.map(newSoc, Societate.class);
		newSocietate.setId(societate.getId());
		final Societate updatedSocietate = societateRepository.save(newSocietate);
		return ResponseEntity.ok(modelMapper.map(updatedSocietate, SocietateDTO.class));
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteSocietate(@PathVariable(value = "id") int id) throws ResourceNotFoundException {
		Societate societate = societateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + id));

		societateRepository.delete(societate);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
