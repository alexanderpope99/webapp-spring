package net.guides.springboot2.crud.controller;

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

import net.guides.springboot2.crud.dto.CMDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.CM;
import net.guides.springboot2.crud.repository.CMRepository;
import net.guides.springboot2.crud.services.CMService;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/cm")
public class CMController {
	@Autowired
	private CMRepository cmRepository;

	@Autowired
	private CMService cmService;

	@GetMapping
	public List<CM> getAllCMs() {
		return cmRepository.findAll(Sort.by(Sort.Direction.DESC, "dela"));
	}

	@GetMapping("{id}")
	public ResponseEntity<CM> getCMById(@PathVariable("id") int cmId) throws ResourceNotFoundException {
		CM cm = cmRepository.findById(cmId)
				.orElseThrow(() -> new ResourceNotFoundException("CM not found for this id :: " + cmId));
		return ResponseEntity.ok().body(cm);
	}

	@GetMapping("idc={id}")
	public List<CM> getCMByIdcontract(@PathVariable("id") int idcontract) {
		return cmRepository.findByContract_IdOrderByDelaDescPanalaDesc(idcontract);
	}

	@GetMapping("testvalcm/l={luna}an={an}idc={idcontract}")
	public int getValCM(@PathVariable("luna") int luna, @PathVariable("an") int an,
			@PathVariable("idcontract") int idcontract) {
		return cmService.getValCM(luna, an, idcontract);
	}

	@PostMapping
	public CMDTO createCM(@RequestBody CMDTO cmDTO) throws ResourceNotFoundException {
		return cmService.save(cmDTO);
	}

	@PutMapping("{id}")
	public ResponseEntity<CMDTO> updateCM(@PathVariable("id") int cmId, @RequestBody CMDTO cmDTO)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(cmService.update(cmId, cmDTO));
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteCM(@PathVariable("id") int cmId) throws ResourceNotFoundException {
		return cmService.delete(cmId);
	}
}
