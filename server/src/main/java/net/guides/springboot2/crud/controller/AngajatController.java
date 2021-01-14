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
		for (Angajat a : angajati) {
			angajatiDTO.add(modelMapper.map(a, AngajatDTO.class));
		}

		return angajatiDTO;
	}

	@GetMapping("/expand")
	public List<Angajat> getAll() {
		return angajatRepository.findAll(Sort.by(Sort.Direction.ASC, "idpersoana"));
	}

	@GetMapping("{id}")
	public ResponseEntity<AngajatDTO> getAngajatByIdDTO(@PathVariable("id") int angajatId)
			throws ResourceNotFoundException {
		Angajat angajat = angajatRepository.findById(angajatId)
				.orElseThrow(() -> new ResourceNotFoundException("Angajat not found for this id :: " + angajatId));

		return ResponseEntity.ok().body(modelMapper.map(angajat, AngajatDTO.class));
	}

	@GetMapping("/expand/{id}")
	public Angajat getAngajatById(@PathVariable("id") int angajatId) throws ResourceNotFoundException {
		return angajatRepository.findById(angajatId)
				.orElseThrow(() -> new ResourceNotFoundException("Angajat not found for this id :: " + angajatId));
	}

	@GetMapping("/c")
	public List<Angajat> getAngajatByIdWhereIdcontractNotNull() {
		return angajatRepository.findByContract_IdNotNull();
	}

	@GetMapping("/userid/{id}")
	public int getAngajatIdByUserId(@PathVariable("id") int userId) {
		return angajatRepository.findPersoanaIdByUserId(userId);
	}

	@GetMapping("/ids={ids}")
	public List<Angajat> findAngajatiByIdsocietate(@PathVariable("ids") int idsocietate) {
		return angajatRepository.findBySocietate_IdOrderByPersoana_NumeAsc(idsocietate);
	}

	@GetMapping("/ids={ids}&c")
	public List<Angajat> findAngajatiWithContractByIdsocietate(@PathVariable("ids") int idsocietate) {
		return angajatRepository.findBySocietate_IdAndContract_IdNotNull(idsocietate);
	}

	@GetMapping("/ids={ids}&u")
	public List<Angajat> findAngajatiWithUserAndAccessByIdsocietate(@PathVariable("ids") int idsocietate) {
		return angajatRepository.findBySocietate_IdAndContract_IdNotNullWithUserAndAccess(idsocietate);
	}

	@GetMapping("/ids={ids}&nu")
	public List<Angajat> findAngajatiNoUserAndAccessByIdsocietate(@PathVariable("ids") int idsocietate) {
		return angajatRepository.findBySocietate_IdAndUserIsNullOrderByPersoana_NumeAsc(idsocietate);
	}

	@GetMapping("/ids={ids}/count")
	public int countAngajatiByIdsocietate(@PathVariable("ids") int idsocietate) {
		return angajatRepository.countBySocietate_Id(idsocietate);
	}

	@GetMapping("/superiori-posibili/{id}")
	public List<Angajat> findSuperioriPosibiliOfAngajat(@PathVariable("id") int idangajat)
			throws ResourceNotFoundException {
		return angajatService.getSuperioriPosibili(idangajat);
	}

	@GetMapping("subalterni/{id}")
	public List<Angajat> getSubalterni(@PathVariable("id") int idangajat) throws ResourceNotFoundException {
		return angajatService.getSubalterni(idangajat);
	}

	@GetMapping("/socid={ids}/usrid={idu}")
	public Angajat getAngajatBySocietateIdAndUserId(@PathVariable("ids") int idsocietate,
			@PathVariable("idu") int iduser) {
		return angajatRepository.findBySocietate_IdAndUser_Id(idsocietate, iduser);
	}

	@GetMapping("/socid={ids}/usrid={idu}&c")
	public List<Angajat> getAngajatBySocietateIdAndUserIdWithContract(@PathVariable("ids") int idsocietate,
			@PathVariable("idu") int iduser) {
		return angajatRepository.findBySocietate_IdAndUser_IdAndContractNotNull(idsocietate, iduser);
	}

	@PostMapping("/expand")
	public Angajat createAngajat(@RequestBody Angajat angajat) {
		return angajatRepository.save(angajat);
	}

	@PostMapping
	public Angajat createAngajatDTO(@RequestBody AngajatDTO angajatDTO) throws ResourceNotFoundException {
		return angajatService.save(angajatDTO);
	}

	@PostMapping("ids={ids}/{idsuperior}")
	public Angajat createAngajat(@PathVariable("ids") int ids, @PathVariable("idsuperior") Integer idsuperior,
			@RequestBody Angajat angajat) throws ResourceNotFoundException {
		return angajatService.save(angajat, ids, idsuperior);
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

	@PutMapping("/superior/{id}&{idsuperior}")
	public Angajat setSuperior(@PathVariable("id") int id, @PathVariable("idsuperior") int idsuperior)
			throws ResourceNotFoundException {
		return angajatService.setSuperior(id, idsuperior);
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
