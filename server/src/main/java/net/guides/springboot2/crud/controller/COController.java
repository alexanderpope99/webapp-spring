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

import net.guides.springboot2.crud.dto.CODTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.CO;
import net.guides.springboot2.crud.repository.CORepository;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/co")
public class COController {
	@Autowired
	private CORepository coRepository;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("latest")
	public List<CO> getAllCOsLatest() {
		return coRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
	}

	@GetMapping
	public List<CO> getAllCOs() {
		return coRepository.findAllByOrderByDelaAsc();
	}

	@GetMapping("{id}")
	public ResponseEntity<CO> getCOById(@PathVariable(value = "id") int coId) throws ResourceNotFoundException {
		CO co = coRepository.findById(coId)
				.orElseThrow(() -> new ResourceNotFoundException("CO not found for this id :: " + coId));
		return ResponseEntity.ok().body(co);
	}

	@GetMapping("idc={id}")
	public ResponseEntity<List<CODTO>> getCOByIdcontract(@PathVariable(value = "id") int idcontract)
			throws ResourceNotFoundException {
		List<CO> co = coRepository.findByIdcontractOrderByDelaDesc(idcontract);

		List<CODTO> coDTO = new ArrayList<>();
		co.forEach(c -> coDTO.add(modelMapper.map(c, CODTO.class)));

		return ResponseEntity.ok().body(coDTO);
	}

	@GetMapping("fp&idc={id}")
	public ResponseEntity<List<CO>> getCOByIdcontractWhereNeplatit(@PathVariable(value = "id") int idcontract)
			throws ResourceNotFoundException {
		List<CO> co = coRepository.findByIdcontractAndTipOrderByDelaDesc(idcontract, "Concediu fără plată");
		return ResponseEntity.ok().body(co);
	}

	@PostMapping
	public CO createCO(@RequestBody CO co) {
		return coRepository.save(co);
	}

	@PutMapping("{id}")
	public ResponseEntity<CO> updateCO(@PathVariable(value = "id") int coId, @RequestBody CO coDetails)
			throws ResourceNotFoundException {
		CO co = coRepository.findById(coId)
				.orElseThrow(() -> new ResourceNotFoundException("CO not found for this id :: " + coId));

		coDetails.setId(co.getId());
		final CO updatedCO = coRepository.save(coDetails);
		return ResponseEntity.ok(updatedCO);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteCO(@PathVariable(value = "id") int coId) throws ResourceNotFoundException {
		CO co = coRepository.findById(coId)
				.orElseThrow(() -> new ResourceNotFoundException("CO not found for this id :: " + coId));

		coRepository.delete(co);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
