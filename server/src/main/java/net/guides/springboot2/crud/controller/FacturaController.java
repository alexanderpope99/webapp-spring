package net.guides.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import net.guides.springboot2.crud.services.FacturaService;

@RestController
@RequestMapping("/factura")
public class FacturaController {

	@Autowired
	private FacturaService facturaService;

	@GetMapping
	public List<FacturaDTO> getAllFacturi() {
		return facturaService.findAll();
	}

	@GetMapping("{id}")
	public Factura getById(@PathVariable("id") int id) throws ResourceNotFoundException {
		return facturaService.findById(id);
	}

	@PostMapping
	public Factura saveFactura(@RequestBody Factura newFactura) {
		return facturaService.save(newFactura);
	}

	@PostMapping("ids={ids}")
	public Factura saveFactura(@RequestBody Factura newFactura, @PathVariable("ids") int ids ) throws ResourceNotFoundException {
		return facturaService.save(newFactura);
	}

	@PutMapping("{id}")
	public Factura update(@RequestBody Factura newFactura, @PathVariable("id") int id) throws ResourceNotFoundException {
		return facturaService.update(newFactura, id);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteFactura(@PathVariable("id") int id) throws ResourceNotFoundException {
		facturaService.delete(id);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
