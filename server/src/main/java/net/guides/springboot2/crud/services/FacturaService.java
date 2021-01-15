package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import net.guides.springboot2.crud.dto.FacturaDTO;
import net.guides.springboot2.crud.dto.FacturaJSON;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.CentruCost;
import net.guides.springboot2.crud.model.Factura;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.CentruCostRepository;
import net.guides.springboot2.crud.repository.FacturaRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;
import net.guides.springboot2.crud.repository.AngajatRepository;
import org.springframework.http.ResponseEntity;

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
	private AngajatRepository angajatRepository;

	@Autowired
	private ModelMapper modelMapper;

	public Factura save(FacturaDTO facturaDTO) throws ResourceNotFoundException {
		Factura factura = modelMapper.map(facturaDTO, Factura.class);

		Societate societate = societateRepository.findById(facturaDTO.getIdsocietate()).orElseThrow(
				() -> new ResourceNotFoundException("Nu există societate cu id: " + facturaDTO.getIdsocietate()));
		factura.setSocietate(societate);

		if (facturaDTO.getIdcentrucost() != 0) {
			CentruCost centruCost = centruCostRepository.findById(facturaDTO.getIdcentrucost())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Nu există centru cost cu id: " + facturaDTO.getIdcentrucost()));
			factura.setCentrucost(centruCost);
		}

		if (facturaDTO.getIdaprobator() != 0) {
			Angajat aprobator = angajatRepository.findById(facturaDTO.getIdaprobator()).orElseThrow(
					() -> new ResourceNotFoundException("Nu există angajat cu id: " + facturaDTO.getIdaprobator()));
			factura.setAprobator(aprobator);
		}

		return facturaRepository.save(factura);
	}

	public ResponseEntity<String> updateObsCodp(int facturaID, FacturaJSON obscodp) throws ResourceNotFoundException {
		Factura factura = facturaRepository.findById(facturaID)
				.orElseThrow(() -> new ResourceNotFoundException("Nu există factură cu id: " + facturaID));
		factura.setObservatii(obscodp.getObservatii());
		factura.setCodproiect(obscodp.getCodproiect());
		facturaRepository.save(factura);
		return ResponseEntity.ok().body("Factură Actualizată");
	}

	public Factura update(int facturaID, FacturaDTO newFacturaDTO) throws ResourceNotFoundException {
		newFacturaDTO.setId(facturaID);
		return this.save(newFacturaDTO);
	}

	public ResponseEntity<String> approve(int facturaID) throws ResourceNotFoundException {
		Factura factura = facturaRepository.findById(facturaID)
				.orElseThrow(() -> new ResourceNotFoundException("Nu există factură cu id: " + facturaID));
		factura.setStatus("Aprobată");
		facturaRepository.save(factura);
		return ResponseEntity.ok().body("Factură Aprobată");
	}

	public ResponseEntity<String> reject(int facturaID) throws ResourceNotFoundException {
		Factura factura = facturaRepository.findById(facturaID)
				.orElseThrow(() -> new ResourceNotFoundException("Nu există factură cu id: " + facturaID));
		factura.setStatus("Respinsă");
		facturaRepository.save(factura);
		return ResponseEntity.ok().body("Factură Respinsă");
	}

	public ResponseEntity<String> postpone(int facturaID) throws ResourceNotFoundException {
		Factura factura = facturaRepository.findById(facturaID)
				.orElseThrow(() -> new ResourceNotFoundException("Nu există factură cu id: " + facturaID));
		factura.setStatus("Amânată");
		facturaRepository.save(factura);
		return ResponseEntity.ok().body("Factură Amânată");
	}

	public Factura updateKeepOldFile(int facturaID, FacturaDTO newFacturaDTO) throws ResourceNotFoundException {
		newFacturaDTO.setId(facturaID);
		Factura oldFactura = facturaRepository.findById(facturaID)
				.orElseThrow(() -> new ResourceNotFoundException("Nu există factura cu id: " + facturaID));

		Factura factura = modelMapper.map(newFacturaDTO, Factura.class);
		factura.setFisier(oldFactura.getFisier());
		factura.setNumefisier(oldFactura.getNumefisier());
		factura.setDimensiunefisier(oldFactura.getDimensiunefisier());

		Societate societate = societateRepository.findById(newFacturaDTO.getIdsocietate()).orElseThrow(
				() -> new ResourceNotFoundException("Nu există societate cu id: " + newFacturaDTO.getIdsocietate()));
		factura.setSocietate(societate);

		if (newFacturaDTO.getIdcentrucost() != 0) {
			CentruCost centruCost = centruCostRepository.findById(newFacturaDTO.getIdcentrucost())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Nu există centru cost cu id: " + newFacturaDTO.getIdcentrucost()));
			factura.setCentrucost(centruCost);
		}

		if (newFacturaDTO.getIdaprobator() != 0) {
			Angajat aprobator = angajatRepository.findById(newFacturaDTO.getIdaprobator()).orElseThrow(
					() -> new ResourceNotFoundException("Nu există angajat cu id: " + newFacturaDTO.getIdaprobator()));
			factura.setAprobator(aprobator);
		}

		return facturaRepository.save(factura);
	}
}
