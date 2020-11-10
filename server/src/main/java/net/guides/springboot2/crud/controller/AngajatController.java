package net.guides.springboot2.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import net.guides.springboot2.crud.dto.AngajatDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.services.AngajatService;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/angajat")
public class AngajatController {
	@Autowired
	private AngajatRepository angajatRepository;

	@Autowired
	private AngajatService angajatService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	public List<AngajatDTO> getAllDTO() {
		List<Angajat> angajati = angajatRepository.findAll(Sort.by(Sort.Direction.ASC, "idpersoana"));

		List<AngajatDTO> angajatiDTO = new ArrayList<>();
		for(Angajat a : angajati) {
			angajatiDTO.add(modelMapper.map(a, AngajatDTO.class));
		}

		return angajatiDTO;
	}

	@GetMapping("/expand")
	public List<Angajat> getAll() {
		List<Angajat> angajati = angajatRepository.findAll(Sort.by(Sort.Direction.ASC, "idpersoana"));

		return angajati;
	}

	@GetMapping("{id}")
	public ResponseEntity<AngajatDTO> getAngajatByIdDTO(@PathVariable(value = "id") int angajatId)
			throws ResourceNotFoundException {
		Angajat angajat = angajatRepository.findById(angajatId)
				.orElseThrow(() -> new ResourceNotFoundException("Angajat not found for this id :: " + angajatId));
				
		return ResponseEntity.ok().body(modelMapper.map(angajat, AngajatDTO.class));
	}

	@GetMapping("/expand/{id}")
	public ResponseEntity<Angajat> getAngajatById(@PathVariable(value = "id") int angajatId)
			throws ResourceNotFoundException {
		Angajat angajat = angajatRepository.findById(angajatId)
				.orElseThrow(() -> new ResourceNotFoundException("Angajat not found for this id :: " + angajatId));

		return ResponseEntity.ok().body(angajat);
	}

	@GetMapping("/c")
	public List<Angajat> getAngajatByIdWhereIdcontractNotNull() {
		return angajatRepository.findByIdcontractNotNull();
	}

	@GetMapping("/userid/{id}")
	public int getPersoanaIdByUserId(@PathVariable(value = "id") int userId) {
		return angajatRepository.findPersoanaIdByUserId(userId);
	}

	@GetMapping("/ids={ids}/count")
	public int countAngajatiByIdsocietate(@PathVariable(name = "ids") int idsocietate) {
		return angajatRepository.countByIdsocietate(idsocietate);
	}

	@PostMapping("/expand")
	public Angajat createAngajat(@RequestBody Angajat angajat) {
		return angajatRepository.save(angajat);
	}

	@PostMapping
	public Angajat createAngajatDTO(@RequestBody AngajatDTO angajatDTO) throws ResourceNotFoundException {
		return angajatService.save(angajatDTO);
	}

	@PostMapping("ids={ids}")
	public Angajat createAngajat(@PathVariable("ids") int ids, @RequestBody Angajat angajat)
		throws ResourceNotFoundException {
		return angajatService.save(angajat, ids);
	}

	@PutMapping("{id}")
	public ResponseEntity<Angajat> updateAngajat(@PathVariable(value = "id") int angajatId,
			@RequestBody Angajat angajatDetails) throws ResourceNotFoundException {
		Angajat angajat = angajatRepository.findById(angajatId)
				.orElseThrow(() -> new ResourceNotFoundException("Angajat not found for this id :: " + angajatId));

		angajatDetails.setPersoana(angajat.getPersoana());
		final Angajat updatedAngajat = angajatRepository.save(angajatDetails);
		return ResponseEntity.ok(updatedAngajat);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteAngajat(@PathVariable(value = "id") int angajatId)
			throws ResourceNotFoundException {
		Angajat angajat = angajatRepository.findById(angajatId)
				.orElseThrow(() -> new ResourceNotFoundException("Angajat not found for this id :: " + angajatId));

		angajatRepository.delete(angajat);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
