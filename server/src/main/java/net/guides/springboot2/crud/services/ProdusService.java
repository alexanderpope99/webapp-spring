package net.guides.springboot2.crud.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.ProdusDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Produs;
import net.guides.springboot2.crud.model.Proiect;
import net.guides.springboot2.crud.repository.ProdusRepository;
import net.guides.springboot2.crud.repository.ProiectRepository;

@Service
public class ProdusService {
	@Autowired
	private ProdusRepository produsRepository;

	@Autowired
	private ProiectRepository proiectRepository;

	@Autowired
	private ModelMapper modelMapper;

	public ProdusDTO save(ProdusDTO prDTO) throws ResourceNotFoundException {
		Produs pr = modelMapper.map(prDTO, Produs.class);

		Proiect proiect = proiectRepository.findById(prDTO.getIdproiect())
				.orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id :: " + prDTO.getIdproiect()));
		pr.setProiect(proiect);

		produsRepository.save(pr);
		prDTO.setId(pr.getId());
		return prDTO;
	}

	public ProdusDTO update(int id, ProdusDTO prDTO) throws ResourceNotFoundException {
		prDTO.setId(id);
		return save(prDTO);
	}
}
