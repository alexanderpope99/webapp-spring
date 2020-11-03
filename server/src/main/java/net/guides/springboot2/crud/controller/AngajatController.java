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

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.repository.AngajatRepository;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/angajat")
public class AngajatController {
	@Autowired
	private AngajatRepository angajatRepository;

	@GetMapping("/sortbyid")
	public List<Angajat> getAllAngajats() {
		return angajatRepository.findAll(Sort.by(Sort.Direction.ASC, "idpersoana"));
	}

	@GetMapping("{id}")
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

	@PostMapping
	public Angajat createAngajat(@RequestBody Angajat angajat) {
		return angajatRepository.save(angajat);
	}

	@PutMapping("{id}")
	public ResponseEntity<Angajat> updateAngajat(@PathVariable(value = "id") int angajatId,
			@RequestBody Angajat angajatDetails) throws ResourceNotFoundException {
		Angajat angajat = angajatRepository.findById(angajatId)
				.orElseThrow(() -> new ResourceNotFoundException("Angajat not found for this id :: " + angajatId));

		angajatDetails.setIdpersoana(angajat.getIdpersoana());
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
