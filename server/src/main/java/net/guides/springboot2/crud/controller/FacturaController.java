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

import net.guides.springboot2.crud.dto.FacturaDTO;
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

	@GetMapping("{id}")
	public ResponseEntity<FacturaDTO> getFacturaById(@PathVariable(value = "id") int facturaId)
			throws ResourceNotFoundException {
		Factura factura = facturaRepository.findById(facturaId)
				.orElseThrow(() -> new ResourceNotFoundException("Factura not found for this id :: " + facturaId));
		return ResponseEntity.ok().body(modelMapper.map(factura, FacturaDTO.class));
	}

	@GetMapping("/idsoc/{ids}")
	public List<FacturaDTO> getFacturaByIdSocietate(@PathVariable(value = "ids") int societateId) {
		List<Factura> factura = facturaRepository.findFacturiByIdsocietate(societateId);
		return factura.stream().map(c -> modelMapper.map(c, FacturaDTO.class)).collect(Collectors.toList());

	}

	@PostMapping
	public FacturaDTO createFactura(@RequestBody FacturaDTO factura) throws ResourceNotFoundException {
		return facturaService.save(factura);
	}

	@PutMapping("{id}")
	public ResponseEntity<FacturaDTO> updateFactura(@PathVariable(value = "id") int facturaId,
			@RequestBody FacturaDTO facturaDTO) throws ResourceNotFoundException {
		return ResponseEntity.ok(facturaService.update(facturaId, facturaDTO));
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteFactura(@PathVariable(value = "id") int facturaId)
			throws ResourceNotFoundException {
		Factura factura = facturaRepository.findById(facturaId)
				.orElseThrow(() -> new ResourceNotFoundException("Factura not found for this id :: " + facturaId));

		facturaRepository.delete(factura);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
