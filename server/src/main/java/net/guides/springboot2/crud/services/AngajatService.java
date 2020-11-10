package net.guides.springboot2.crud.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.AngajatDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.PersoanaRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class AngajatService {
	@Autowired
	private AngajatRepository angajatRepository;
	@Autowired
	private PersoanaRepository persoanaRepository;
	@Autowired
	private SocietateRepository societateRepository;

	@Autowired
	private ModelMapper modelMapper;

	public Angajat save(AngajatDTO angajatDTO) throws ResourceNotFoundException {

		Angajat angajat = modelMapper.map(angajatDTO, Angajat.class);

		Persoana persoana = persoanaRepository.findById(angajatDTO.getIdpersoana())
		.orElseThrow(() -> new ResourceNotFoundException("Persoana not found for this id :: " + angajatDTO.getIdpersoana()));

		Societate societate = societateRepository.findById(angajatDTO.getIdsocietate())
		.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + angajatDTO.getIdsocietate()));

		angajat.setPersoana(persoana);
		angajat.setSocietate(societate);

		angajat = angajatRepository.save(angajat);
		return angajat;
	}

	// angajat.persoana not null, other fields are null
	public Angajat save(Angajat angajat, int idsocietate) throws ResourceNotFoundException {

		angajat.setPersoana(persoanaRepository.save(angajat.getPersoana()));

		Societate societate = societateRepository.findById(idsocietate)
		.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + idsocietate));

		angajat.setSocietate(societate);

		angajat = angajatRepository.save(angajat);
		return angajat;
	}
}
