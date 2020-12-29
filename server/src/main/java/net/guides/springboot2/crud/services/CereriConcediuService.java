package net.guides.springboot2.crud.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.CereriConcediuDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.model.User;
import net.guides.springboot2.crud.model.CereriConcediu;
import net.guides.springboot2.crud.repository.CereriConcediuRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;
import net.guides.springboot2.crud.repository.UserRepository;

@Service
public class CereriConcediuService {
	@Autowired
	private CereriConcediuRepository cereriConcediuRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SocietateRepository societateRepository;

	@Autowired
	private ModelMapper modelMapper;

	public CereriConcediuDTO save(CereriConcediuDTO cerereConcediuDTO) throws ResourceNotFoundException {
		CereriConcediu cerereConcediu = modelMapper.map(cerereConcediuDTO, CereriConcediu.class);

		User user = userRepository.findById(cerereConcediuDTO.getIduser())
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id"));

		cerereConcediu.setUser(user);

		Societate societate = societateRepository.findById(cerereConcediuDTO.getIdsocietate())
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id"));

		cerereConcediu.setSocietate(societate);

		cereriConcediuRepository.save(cerereConcediu);

		cerereConcediuDTO.setId(cerereConcediu.getId());
		return cerereConcediuDTO;
	}
}
