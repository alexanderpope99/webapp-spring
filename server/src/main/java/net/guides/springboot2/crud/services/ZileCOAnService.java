package net.guides.springboot2.crud.services;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.ZileCOAnDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.ZileCOAn;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.ZileCOAnRepository;

@Service
public class ZileCOAnService {
	@Autowired
	private ZileCOAnRepository zileCOAnRepository;
	@Autowired
	private AngajatRepository angajatRepository;

	@Autowired
	private ModelMapper modelMapper;

	public ZileCOAnDTO save(ZileCOAnDTO zileCOAnDTO) throws ResourceNotFoundException {
		ZileCOAn zc = modelMapper.map(zileCOAnDTO, ZileCOAn.class);
		Angajat angajat = angajatRepository.findById(zileCOAnDTO.getIdangajat()).orElseThrow(
				() -> new ResourceNotFoundException("Nu există angajat cu id: " + zileCOAnDTO.getIdangajat()));

		zc.setAngajat(angajat);
		zileCOAnRepository.save(zc);
		return zileCOAnDTO;
	}

	public ZileCOAnDTO update(int id, ZileCOAnDTO zileCOAnDTO) throws ResourceNotFoundException {
		zileCOAnDTO.setId(id);
		return save(zileCOAnDTO);
	}
}