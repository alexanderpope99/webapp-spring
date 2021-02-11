package net.guides.springboot2.crud.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.ActivitateDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Activitate;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.ActivitateRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class ActivitateService {
	@Autowired
	private ActivitateRepository activitateRepository;

	@Autowired
	private SocietateRepository societateRepository;

	@Autowired
	private ModelMapper modelMapper;

	public ActivitateDTO save(ActivitateDTO acDTO) throws ResourceNotFoundException {
		Activitate ac = modelMapper.map(acDTO, Activitate.class);

		Societate societate = societateRepository.findById(acDTO.getIdsocietate())
				.orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id :: " + acDTO.getIdsocietate()));
		ac.setSocietate(societate);

		activitateRepository.save(ac);
		acDTO.setId(ac.getId());
		return acDTO;
	}

	public ActivitateDTO update(int id, ActivitateDTO acDTO) throws ResourceNotFoundException {
		acDTO.setId(id);
		return save(acDTO);
	}
}
