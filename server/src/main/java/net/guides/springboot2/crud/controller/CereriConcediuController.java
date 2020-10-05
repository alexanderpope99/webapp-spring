package net.guides.springboot2.crud.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.CereriConcediu;
import net.guides.springboot2.crud.repository.CereriConcediuRepository;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import net.guides.springboot2.crud.services.ZileService;

@RestController
@RequestMapping("/cerericoncediu")
public class CereriConcediuController {
	@Autowired
	private CereriConcediuRepository cereriConcediuRepository;

	@Autowired
	private ZileService zileService;

	@GetMapping("/zilelucratoareintre")
	public long getZileLucratoareBetween(@RequestParam("date1") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date1,
			@RequestParam("date2") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date2) {
		return zileService.getZileLucratoareInInterval(date1, date2);
	}

	@GetMapping
	public List<CereriConcediu> getAllCereriConcediu() {
		return cereriConcediuRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<CereriConcediu> getCerereConcediuById(@PathVariable(value = "id") Long cereriConcediuId)
			throws ResourceNotFoundException {
		CereriConcediu cereriConcediu = cereriConcediuRepository.findById(cereriConcediuId).orElseThrow(
				() -> new ResourceNotFoundException("CereriConcediu not found for this id :: " + cereriConcediuId));
		return ResponseEntity.ok().body(cereriConcediu);
	}

	@GetMapping("/usersoc/{usrid}&{socid}")
	public List<CereriConcediu> getCerereConcediuByUserIdAndSocietyId(@PathVariable(value = "usrid") long usrId,
			@PathVariable(value = "socid") long socId) throws ResourceNotFoundException {
		return cereriConcediuRepository.findCerereConcediuByUserIdAndSocietyId(usrId, socId);
	}

	@GetMapping("/supsoc/{supid}&{socid}")
	public List<CereriConcediu> getCerereConcediuBySuperiorIdAndSocietyId(@PathVariable(value = "supid") long supId,
			@PathVariable(value = "socid") long socId) throws ResourceNotFoundException {
		return cereriConcediuRepository.findCerereConcediuBySuperiorIdAndSocietyId(supId, socId);
	}

	@PostMapping
	public CereriConcediu createCereriConcediu(@RequestBody CereriConcediu cereriConcediu) {
		return cereriConcediuRepository.save(cereriConcediu);
	}

	@PutMapping("{id}")
	public ResponseEntity<CereriConcediu> updateCereriConcediu(@PathVariable(value = "id") long cereriConcediuId,
			@RequestBody CereriConcediu cereriConcediuDetails) throws ResourceNotFoundException {
		CereriConcediu cereriConcediu = cereriConcediuRepository.findById(cereriConcediuId).orElseThrow(
				() -> new ResourceNotFoundException("CentruCost not found for this id :: " + cereriConcediuId));

		cereriConcediuDetails.setId(cereriConcediu.getId());
		final CereriConcediu updatedCereriConcediu = cereriConcediuRepository.save(cereriConcediuDetails);
		return ResponseEntity.ok(updatedCereriConcediu);
	}

	@PutMapping("/statusappr/{id}")
	public CereriConcediu approveStatus(@PathVariable(value = "id") long cereriConcediuId)
			throws ResourceNotFoundException {
		return cereriConcediuRepository.approveStatus(cereriConcediuId);
	}

	@PutMapping("/statusrej/{id}")
	public CereriConcediu rejectStatus(@PathVariable(value = "id") long cereriConcediuId)
			throws ResourceNotFoundException {
		return cereriConcediuRepository.rejectStatus(cereriConcediuId);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteCereriConcediu(@PathVariable(value = "id") Long cereriConcediuId)
			throws ResourceNotFoundException {
		CereriConcediu cereriConcediu = cereriConcediuRepository.findById(cereriConcediuId).orElseThrow(
				() -> new ResourceNotFoundException("CereriConcediu not found for this id :: " + cereriConcediuId));

		cereriConcediuRepository.delete(cereriConcediu);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
