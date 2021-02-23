package net.guides.springboot2.crud.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.SocietateDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Fisier;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.model.User;
import net.guides.springboot2.crud.repository.SocietateRepository;
import net.guides.springboot2.crud.repository.UserRepository;

@Service
public class SocietateService {
	@Autowired
	private SocietateRepository societateRepository;

	@Autowired
	private FisierService fisierService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	public Societate findById(int idsocietate) throws ResourceNotFoundException {
		return societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
	}

	public Societate save(SocietateDTO societateDTO) throws ResourceNotFoundException {
		Fisier fisier = fisierService.findById(societateDTO.getIdimagine());
		Societate societate = modelMapper.map(societateDTO, Societate.class);
		societate.checkData();
		societate.setImagine(fisier);


		societate = societateRepository.save(societate);
		return societate;
	}

	public Societate postWithImagine(Societate societate,int uid,int fisierId) throws ResourceNotFoundException {
		societate.checkData();
		if(fisierId!=0){
		Fisier fisier=fisierService.findById(fisierId);
		societate.setImagine(fisier);
		} else societate.setImagine(null);
		Societate newSoc = societateRepository.save(societate);
		User user = userRepository.findById(uid).orElseThrow(() -> new RuntimeException("Error"));
		user.addSocietate(newSoc);
		userRepository.save(user);

		return societate;
	}

	public Societate putWithImagine(int idSoc, Societate newSocietate,int fisierId) throws ResourceNotFoundException {
		Societate societate=findById(idSoc);
		if(fisierId!=0){
		Fisier fisier=fisierService.findById(fisierId);
		newSocietate.setImagine(fisier);
		}
		else newSocietate.setImagine(null);
		societate.update(newSocietate);

		return societateRepository.save(societate);
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
