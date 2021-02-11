package net.guides.springboot2.crud.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Factura;
import net.guides.springboot2.crud.model.Produs;
import net.guides.springboot2.crud.repository.FacturaRepository;

@Service
public class FacturaService {

	@Autowired
	private FacturaRepository facturaRepository;

	FacturaService() {
	}

	public List<Factura> findAll() {
		return facturaRepository.findAll();
	}

	public Factura findById(int id) throws ResourceNotFoundException {
		return facturaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Factura not found for this id :: " + id));
	}

	public List<Factura> findBySocietate_Id(int idsocietate) {
		return facturaRepository.findByClient_Societate_Id(idsocietate);
	}

	public Factura save(Factura newFactura) {
		// newFactura = facturaRepository.save(newFactura);
		for(Produs produs : newFactura.getProduse()) {
			produs.setFactura(newFactura);
		}
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
