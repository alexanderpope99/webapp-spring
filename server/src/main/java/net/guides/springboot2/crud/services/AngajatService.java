package net.guides.springboot2.crud.services;

import java.util.List;
import java.util.stream.Collectors;

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

		Persoana persoana = persoanaRepository.findById(angajatDTO.getIdpersoana()).orElseThrow(
				() -> new ResourceNotFoundException("Persoana not found for this id :: " + angajatDTO.getIdpersoana()));

		Societate societate = societateRepository.findById(angajatDTO.getIdsocietate()).orElseThrow(
				() -> new ResourceNotFoundException("Societate not found for this id :: " + angajatDTO.getIdsocietate()));

		angajat.setPersoana(persoana);
		angajat.setSocietate(societate);

		angajat = angajatRepository.save(angajat);
		return angajat;
	}

	// angajat.persoana not null, other fields can be null
	public Angajat save(Angajat angajat, int idsocietate, Integer idsuperior) throws ResourceNotFoundException {
		angajat.setPersoana(persoanaRepository.save(angajat.getPersoana()));

		Societate societate = societateRepository.findById(idsocietate)
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + idsocietate));

		angajat.setSocietate(societate);

		if (idsuperior != null && idsuperior > 0) {
			Angajat superior = angajatRepository.findById(idsuperior).orElseThrow(
					() -> new ResourceNotFoundException("Superior (class Angajat) not found for this id :: " + idsuperior));
			angajat.setSuperior(superior);
		}

		angajat = angajatRepository.save(angajat);
		return angajat;
	}

	public Angajat setSuperior(int idangajat, int idsuperior) throws ResourceNotFoundException {
		Angajat angajat = angajatRepository.findById(idangajat).orElseThrow(
			() -> new ResourceNotFoundException("Angajat not found for this id :: " + idangajat));
		Angajat superior = angajatRepository.findById(idsuperior).orElseThrow(
			() -> new ResourceNotFoundException("Superior (class Angajat) not found for this id :: " + idsuperior));
		
		angajat.setSuperior(superior);

		return angajatRepository.save(angajat);
	}

	public List<Angajat> getSubalterni(int idangajat) throws ResourceNotFoundException {
		Angajat angajat = angajatRepository.findById(idangajat).orElseThrow(
			() -> new ResourceNotFoundException("Angajat not found for this id :: " + idangajat));
		
		return angajat.getSubalterni();
	}

	public List<Angajat> getSuperioriPosibili(int idangajat) throws ResourceNotFoundException {
		Angajat angajat = angajatRepository.findById(idangajat).orElseThrow(
			() -> new ResourceNotFoundException("Angajat not found for this id :: " + idangajat));
		
		List<Integer> subalterni = angajat.getSubalterni().stream().map(Angajat::getIdpersoana).collect(Collectors.toList());
		if(subalterni.isEmpty()) {
			return angajatRepository.findBySocietate_IdAndIdpersoanaNot(angajat.getSocietate().getId(), idangajat);
		}
		else{
			return angajatRepository.findBySocietate_IdAndIdpersoanaNotAndIdpersoanaNotIn(angajat.getSocietate().getId(),
					idangajat, subalterni);
		}
		
	}
}
