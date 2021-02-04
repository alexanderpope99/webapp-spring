package net.guides.springboot2.crud.controller;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.CereriConcediu;
import net.guides.springboot2.crud.dto.CereriConcediuDTO;
import net.guides.springboot2.crud.repository.CereriConcediuRepository;
import net.guides.springboot2.crud.services.CereriConcediuService;
import net.guides.springboot2.crud.services.ZileService;

import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/cerericoncediu")
public class CereriConcediuController {
	@Autowired
	private CereriConcediuRepository cereriConcediuRepository;

	@Autowired
	private ZileService zileService;

	@Autowired
	private CereriConcediuService cereriConcediuService;

	@PutMapping("/zilelucratoareintre")
	public long getZileLucratoareBetween(@RequestParam("date1") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date1,
			@RequestParam("date2") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date2) {
		return zileService.getZileLucratoareInInterval(date1, date2);
	}

	@GetMapping
	public List<CereriConcediu> getAllCereriConcediu() {
		return cereriConcediuRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<CereriConcediu> getCerereConcediuById(@PathVariable("id") int cereriConcediuId)
			throws ResourceNotFoundException {
		CereriConcediu cereriConcediu = cereriConcediuRepository.findById(cereriConcediuId).orElseThrow(
				() -> new ResourceNotFoundException("CereriConcediu not found for this id :: " + cereriConcediuId));
		return ResponseEntity.ok().body(cereriConcediu);
	}

	@GetMapping("/usersoc/{usrid}&{socid}")
	public List<CereriConcediu> getCereriConcediuByUserIdAndSocietyId(@PathVariable("usrid") int usrId,
			@PathVariable("socid") int socId) throws ResourceNotFoundException {
		return cereriConcediuRepository.findByUser_IdAndSocietate_Id(usrId, socId);
	}

	@GetMapping("/soc/{socid}")
	public List<CereriConcediuDTO> getCerereConcediuBySocietyId(@PathVariable("socid") int socId)
			throws ResourceNotFoundException {
		return cereriConcediuService.getCereriConcediuWithNumeUserBySocId(socId);
	}

	@PostMapping
	public CereriConcediuDTO createCereriConcediu(@RequestBody CereriConcediuDTO cereriConcediu)
			throws ResourceNotFoundException {
		return cereriConcediuService.save(cereriConcediu);
	}

	@PutMapping("{id}")
	public CereriConcediuDTO updateCereriConcediu(@PathVariable("id") int cereriConcediuId,
			@RequestBody CereriConcediuDTO cereriConcediuDTODetails) throws ResourceNotFoundException {
		cereriConcediuDTODetails.setId(cereriConcediuId);
		return cereriConcediuService.save(cereriConcediuDTODetails);
	}

	@PutMapping("/statusappr/{id}")
	public CereriConcediu approveStatus(@PathVariable("id") int cereriConcediuId)
			throws ResourceNotFoundException {
		return cereriConcediuService.setStatus(cereriConcediuId, "Aprobat");
	}

	@PutMapping("/statusrej/{id}")
	public CereriConcediu rejectStatus(@PathVariable("id") int cereriConcediuId)
			throws ResourceNotFoundException {
		return cereriConcediuService.setStatus(cereriConcediuId, "Respins");
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteCereriConcediu(@PathVariable("id") int cereriConcediuId)
			throws ResourceNotFoundException {
		CereriConcediu cereriConcediu = cereriConcediuRepository.findById(cereriConcediuId).orElseThrow(
				() -> new ResourceNotFoundException("CereriConcediu not found for this id :: " + cereriConcediuId));

		cereriConcediuRepository.delete(cereriConcediu);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
