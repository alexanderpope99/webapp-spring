package net.guides.springboot2.crud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.FacturaDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Client;
import net.guides.springboot2.crud.model.Factura;
import net.guides.springboot2.crud.model.Produs;
import net.guides.springboot2.crud.model.Proiect;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.ClientRepository;
import net.guides.springboot2.crud.repository.ProdusRepository;
import net.guides.springboot2.crud.repository.FacturaRepository;
import net.guides.springboot2.crud.repository.ProiectRepository;

@Service
public class FacturaService {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ProiectRepository proiectRepository;
	@Autowired
	private FacturaRepository facturaRepository;
	@Autowired
	private ProdusRepository produsRepository;
	@Autowired
	private ClientRepository clientRepository;

	FacturaService() {
	}

	public List<FacturaDTO> findAll() {
		List<Factura> facturi = facturaRepository.findAll();
		modelMapper.typeMap(Factura.class, FacturaDTO.class).addMapping(Factura::getProiecte, FacturaDTO::setProiecteClass);
		return facturi.stream().map(user -> modelMapper.map(user, FacturaDTO.class)).collect(Collectors.toList());
	}

	public FacturaDTO findById(int id) {
		modelMapper.typeMap(Factura.class, FacturaDTO.class).addMapping(Factura::getProiecte, FacturaDTO::setProiecteClass);
		Factura factura = facturaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Factura not in database"));
		return modelMapper.map(factura, FacturaDTO.class);
	}


	public FacturaDTO save(FacturaDTO newFacturaDTO) throws ResourceNotFoundException {

		// convert from dto to model :: sets Proiecte
		Factura newFactura = modelMapper.map(newFacturaDTO, Factura.class);

		Client client = clientRepository.findById(newFacturaDTO.getIdclient())
		.orElseThrow(() -> new ResourceNotFoundException("Nu există cleint cu id :: " + newFacturaDTO.getIdclient()));
		newFactura.setClient(client);

		// set produs
		List<Produs> newProduse = new ArrayList<>();
		newFacturaDTO.getProduse().forEach(pr -> {
		// get from db and push to list
		Produs tmpProdus = produsRepository.findById(pr.getId()).get();
					
		// produs needs to point to this factura
		tmpProdus.setFactura(newFactura);
								
		newProduse.add(tmpProdus);
		});
		// set newFactura with complete Produse list
		newFactura.setProduse(newProduse);

		// convert from FacturaDTO.ProiecteJSON to Factura.Proiecte
		List<Proiect> newProiecte = new ArrayList<>();
		newFacturaDTO.getProiecte()
				.forEach(pr -> proiectRepository.findById(pr.getId()).ifPresent(newProiecte::add));
		newFacturaDTO.setProiecteClass(newProiecte);

		int newFacturaId = facturaRepository.save(newFactura).getId();
		newFacturaDTO.setId(newFacturaId);
		return newFacturaDTO;
	} // save

	public void delete(int facturaId) throws ResourceNotFoundException {
		Factura factura = facturaRepository.findById(facturaId)
				.orElseThrow(() -> new ResourceNotFoundException("Factura not found for this id :: " + facturaId));

		factura.getProduse().forEach(pr -> pr.setFactura(null));

		facturaRepository.delete(factura);
	}

}
