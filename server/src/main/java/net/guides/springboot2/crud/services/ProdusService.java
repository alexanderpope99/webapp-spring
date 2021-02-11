package net.guides.springboot2.crud.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.ProdusDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Produs;
import net.guides.springboot2.crud.repository.ProdusRepository;

@Service
public class ProdusService {
	@Autowired
	private ProdusRepository produsRepository;

	@Autowired
	private ModelMapper modelMapper;


	public Produs save(ProdusDTO prDTO) {
		Produs pr = modelMapper.map(prDTO, Produs.class);

		produsRepository.save(pr);
		return pr;
	}

	public Produs update(int id, ProdusDTO prDTO){
		prDTO.setId(id);
		return save(prDTO);
	}
}
