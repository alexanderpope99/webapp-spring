package net.guides.springboot2.crud.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.SocietateDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Fisier;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.FisierRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class SocietateService {
	@Autowired
	private SocietateRepository societateRepository;

	@Autowired
	private FisierRepository fisierRepository;

	@Autowired
	private ModelMapper modelMapper;

	public Societate findById(int idsocietate) throws ResourceNotFoundException {
		return societateRepository.findById(idsocietate)
		.orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
	}

	public Societate save(SocietateDTO societateDTO) throws ResourceNotFoundException {
		Fisier fisier = fisierRepository.findById(societateDTO.getIdimagine())
		.orElseThrow(() -> new ResourceNotFoundException("Nu există user cu id: " +societateDTO.getIdimagine()));
		Societate societate = modelMapper.map(societateDTO, Societate.class);
		societate.setImagine(fisier);

		societate = societateRepository.save(societate);
		return societate;
	}

	public Societate update(int societateId, SocietateDTO newSocietateDTO) throws ResourceNotFoundException {
		newSocietateDTO.setId(societateId);
		return this.save(newSocietateDTO);
	}

	public void delete(int id) throws ResourceNotFoundException {
		// conturi bancare, angajati, facturi, centrecost, useri
		Societate societate = findById(id);

		societate.setAdresa(null);
		societate.setAngajati(null);
		societate.setFacturi(null);
		societate.setCentreCost(null);

		societate.getUseri().forEach(user -> user.removeSocietate(societate));
		societate.setUseri(null);

		societateRepository.delete(societate);
	}

}
