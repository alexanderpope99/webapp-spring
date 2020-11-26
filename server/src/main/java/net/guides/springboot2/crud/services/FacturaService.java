package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import net.guides.springboot2.crud.dto.FacturaDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.CentruCost;
import net.guides.springboot2.crud.model.Factura;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.CentruCostRepository;
import net.guides.springboot2.crud.repository.FacturaRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class FacturaService {
	FacturaService() {
	}

	@Autowired
	private SocietateRepository societateRepository;

	@Autowired
	private FacturaRepository facturaRepository;

	@Autowired
	private CentruCostRepository centruCostRepository;

	@Autowired
	private ModelMapper modelMapper;

	public Factura save(FacturaDTO facturaDTO) throws ResourceNotFoundException {
		Factura factura = modelMapper.map(facturaDTO, Factura.class);

		Societate societate = societateRepository.findById(facturaDTO.getIdsocietate()).orElseThrow(
				() -> new ResourceNotFoundException("Societate not found for this id :: " + facturaDTO.getIdsocietate()));
		factura.setSocietate(societate);

		if (facturaDTO.getIdcentrucost() != 0) {
			CentruCost centruCost = centruCostRepository.findById(facturaDTO.getIdcentrucost()).orElseThrow(
					() -> new ResourceNotFoundException("Centrucost not found for this id :: " + facturaDTO.getIdsocietate()));
			factura.setCentrucost(centruCost);
		}
		
		return facturaRepository.save(factura);
	}

	public Factura update(int facturaID, FacturaDTO newFacturaDTO) throws ResourceNotFoundException {
		newFacturaDTO.setId(facturaID);
		return save(newFacturaDTO);
	}

	public Factura updateIgnoreFile(int facturaID, FacturaDTO newFacturaDTO) throws ResourceNotFoundException {
		newFacturaDTO.setId(facturaID);
		
		return save(newFacturaDTO);
	}
}
