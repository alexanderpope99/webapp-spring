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
	private ModelMapper modelMapper;

	@Autowired
	private CMService cmService;

	@GetMapping
	public List<CMDTO> getAllCMs() {
		return cmRepository.findAll(Sort.by(Sort.Direction.DESC, "dela")).stream()
				.map(c -> modelMapper.map(c, CMDTO.class)).collect(Collectors.toList());
	}

	@GetMapping("{id}")
	public ResponseEntity<CMDTO> getCMById(@PathVariable(value = "id") int cmId) throws ResourceNotFoundException {
		CM cm = cmRepository.findById(cmId)
				.orElseThrow(() -> new ResourceNotFoundException("CM not found for this id :: " + cmId));
		return ResponseEntity.ok().body(modelMapper.map(cm, CMDTO.class));
	}

	@GetMapping("idc={id}")
	public ResponseEntity<List<CMDTO>> getCMByIdcontract(@PathVariable(value = "id") int idcontract)
			throws ResourceNotFoundException {
		List<CMDTO> cm = cmRepository.findByContract_IdOrderByDelaDescPanalaDesc(idcontract).stream()
				.map(c -> modelMapper.map(c, CMDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok().body(cm);
	}

	@GetMapping("testvalcm/l={luna}an={an}idc={idcontract}")
	public int getValCM(@PathVariable(value = "luna") int luna, @PathVariable(value = "an") int an,
			@PathVariable(value = "idcontract") int idcontract) throws ResourceNotFoundException {
		return cmService.getValCM(luna, an, idcontract);
	}

	@PostMapping
	public CMDTO createCM(@RequestBody CMDTO cmDTO) throws ResourceNotFoundException {
		return cmService.save(cmDTO);
	}

	@PutMapping("{id}")
	public ResponseEntity<CMDTO> updateCM(@PathVariable(value = "id") int cmId, @RequestBody CMDTO cmDTO)
			throws ResourceNotFoundException {
		return ResponseEntity.ok(cmService.update(cmId, cmDTO));
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteCM(@PathVariable(value = "id") int cmId) throws ResourceNotFoundException {
		CM cm = cmRepository.findById(cmId)
				.orElseThrow(() -> new ResourceNotFoundException("CM not found for this id :: " + cmId));

		cmRepository.delete(cm);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
