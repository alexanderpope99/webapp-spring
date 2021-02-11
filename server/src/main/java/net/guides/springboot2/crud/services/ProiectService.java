package net.guides.springboot2.crud.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.ProiectDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Proiect;
import net.guides.springboot2.crud.model.Activitate;
import net.guides.springboot2.crud.repository.ProiectRepository;
import net.guides.springboot2.crud.repository.ActivitateRepository;

@Service
public class ProiectService {
	@Autowired
	private ActivitateRepository activitateRepository;

	@Autowired
	private ProiectRepository proiectRepository;

	@Autowired
	private ModelMapper modelMapper;

	public ProiectDTO save(ProiectDTO prDTO) throws ResourceNotFoundException {
		Proiect pr = modelMapper.map(prDTO, Proiect.class);

		Activitate activitate = activitateRepository.findById(prDTO.getIdactivitate())
				.orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id :: " + prDTO.getIdactivitate()));
		pr.setActivitate(activitate);

		proiectRepository.save(pr);
		prDTO.setId(pr.getId());
		return prDTO;
	}

	public ProiectDTO update(int id, ProiectDTO prDTO) throws ResourceNotFoundException {
		prDTO.setId(id);
		return save(prDTO);
	}
}
