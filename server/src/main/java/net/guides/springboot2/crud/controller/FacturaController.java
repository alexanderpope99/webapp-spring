package net.guides.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.dto.FacturaDTO;
import net.guides.springboot2.crud.dto.FacturaJSON;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Factura;
import net.guides.springboot2.crud.repository.FacturaRepository;
import net.guides.springboot2.crud.services.FacturaService;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/factura")
public class FacturaController {
	@Autowired
	private FacturaRepository facturaRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private FacturaService facturaService;

	@GetMapping
	public List<FacturaDTO> getAllFacturi() {
		return facturaRepository.findAll(Sort.by(Sort.Direction.DESC, "data")).stream()
				.map(c -> modelMapper.map(c, FacturaDTO.class)).collect(Collectors.toList());
	}

	@GetMapping("/expand")
	public List<Factura> getAllFacturiExpand() {
		return facturaRepository.findAll(Sort.by(Sort.Direction.DESC, "data"));
	}

	@GetMapping("{id}")
	public ResponseEntity<FacturaDTO> getFacturaById(@PathVariable("id") int facturaId)
			throws ResourceNotFoundException {
		Factura factura = facturaRepository.findById(facturaId)
				.orElseThrow(() -> new ResourceNotFoundException("Factura not found for this id :: " + facturaId));
		return ResponseEntity.ok().body(modelMapper.map(factura, FacturaDTO.class));
	}

	@GetMapping("{id}/expand")
	public ResponseEntity<Factura> getFacturaByIdExpand(@PathVariable("id") int facturaId)
			throws ResourceNotFoundException {
		Factura factura = facturaRepository.findById(facturaId)
				.orElseThrow(() -> new ResourceNotFoundException("Factura not found for this id :: " + facturaId));
		return ResponseEntity.ok().body(factura);
	}

	@Transactional
	@GetMapping("/idsoc/{ids}")
	public List<Factura> getFacturaByIdSocietate(@PathVariable("ids") int societateId) {
		return facturaRepository.findBySocietate_Id(societateId);
	}

	@Transactional
	@GetMapping("/idsoc/approved/{ids}")
	public List<Factura> getApprovedFacturaByIdSocietate(@PathVariable("ids") int societateId) {
		return facturaRepository.findBySocietate_IdAndStatus(societateId, "Aprobată");
	}

	@Transactional
	@GetMapping("/{id}/respinge")
	public ResponseEntity<String> rejectFacturaById(@PathVariable("id") int facturaId)
			throws ResourceNotFoundException {
		return facturaService.reject(facturaId);
	}

	@Transactional
	@GetMapping("/{id}/aproba")
	public ResponseEntity<String> approveFacturaById(@PathVariable("id") int facturaId)
			throws ResourceNotFoundException {
		return facturaService.approve(facturaId);
	}

	@Transactional
	@GetMapping("/{id}/amana")
	public ResponseEntity<String> postponeFacturaById(@PathVariable("id") int facturaId)
			throws ResourceNotFoundException {
		return facturaService.postpone(facturaId);
	}

	@Transactional
	@GetMapping("/idsocuid/{ids}&{uid}")
	public List<Factura> getFacturaByIdSocietateAndIdAprobator(@PathVariable("ids") int societateId,
			@PathVariable("uid") int userID) {
		return facturaRepository.findBySocietate_IdAndAprobator_User_Id(societateId, userID);
	}

	@GetMapping("file/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable("id") int id) throws ResourceNotFoundException {
		Factura factura = facturaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Factura not found for this id :: " + id));

		return ResponseEntity.ok()
				.header("Content-Disposition", "attachment; filename=\"" + factura.getNumefisier() + "\"")
				.body(factura.getFisier());
	}

	@PostMapping
	public Factura saveWithFile(@ModelAttribute FacturaDTO facturaDTO) throws ResourceNotFoundException {
		return facturaService.save(facturaDTO);
	}

	@PutMapping("{id}/obs&codp")
	public ResponseEntity<String> updateFacturaObsCodp(@PathVariable("id") int facturaId,
			@RequestBody FacturaJSON obsCodp) throws ResourceNotFoundException {
		return facturaService.updateObsCodp(facturaId, obsCodp);
	}

	@PutMapping("{id}/new-file")
	public ResponseEntity<Factura> updateFactura(@PathVariable("id") int facturaId,
			@ModelAttribute FacturaDTO facturaDTO) throws ResourceNotFoundException {
		return ResponseEntity.ok(facturaService.update(facturaId, facturaDTO));
	}

	@PutMapping("{id}/keep-file")
	public ResponseEntity<Factura> updateFacturaIgnoreFile(@PathVariable("id") int facturaId,
			@ModelAttribute FacturaDTO facturaDTO) throws ResourceNotFoundException {
		return ResponseEntity.ok(facturaService.updateKeepOldFile(facturaId, facturaDTO));
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteFactura(@PathVariable("id") int facturaId) throws ResourceNotFoundException {
		Factura factura = facturaRepository.findById(facturaId)
				.orElseThrow(() -> new ResourceNotFoundException("Factura not found for this id :: " + facturaId));

		facturaRepository.delete(factura);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
