package net.guides.springboot2.crud.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.FacturaDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Factura;
import net.guides.springboot2.crud.repository.FacturaRepository;

@Service
public class FacturaService {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private FacturaRepository facturaRepository;

	FacturaService() {
	}

	public List<FacturaDTO> findAll() {
		List<Factura> facturi = facturaRepository.findAll();
		return facturi.stream().map(user -> modelMapper.map(user, FacturaDTO.class)).collect(Collectors.toList());
	}

	public Factura findById(int id) throws ResourceNotFoundException {
		return facturaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Factura not found for this id :: " + id));
	}

	public Factura save(Factura newFactura) {
		return facturaRepository.save(newFactura);
	}

	public Factura update(Factura newFactura, int id) throws ResourceNotFoundException {
		Factura factura = findById(id);
		
		return save(factura.update(newFactura));
	}

	public void delete(int facturaId) throws ResourceNotFoundException {
		Factura factura = facturaRepository.findById(facturaId).orElseThrow(() -> new ResourceNotFoundException("Factura not found for this id :: " + facturaId));

		facturaRepository.delete(factura);
	}

}
