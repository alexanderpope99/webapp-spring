package net.guides.springboot2.crud.services;

import java.time.LocalDate;
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

	public Angajat findById(int idangajat) throws ResourceNotFoundException {
		return angajatRepository.findById(idangajat).orElseThrow(() -> new ResourceNotFoundException("Angajat noy found for id :: " + idangajat));
	}

	public Angajat save(AngajatDTO angajatDTO) throws ResourceNotFoundException {
		Angajat angajat = modelMapper.map(angajatDTO, Angajat.class);

		Persoana persoana = persoanaRepository.findById(angajatDTO.getIdpersoana()).orElseThrow(() -> new ResourceNotFoundException("Nu eistă persoana cu id : " + angajatDTO.getIdpersoana()));

		Societate societate = societateRepository.findById(angajatDTO.getIdsocietate()).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + angajatDTO.getIdsocietate()));

		angajat.setPersoana(persoana);
		angajat.setSocietate(societate);

		angajat = angajatRepository.save(angajat);
		return angajat;
	}

	// angajat.persoana not null, other fields can be null
	public Angajat save(Angajat angajat, int idsocietate, Integer idsuperior) throws ResourceNotFoundException {
		angajat.setPersoana(persoanaRepository.save(angajat.getPersoana()));

		Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));

		angajat.setSocietate(societate);

		if (idsuperior != null && idsuperior > 0) {
			Angajat superior = angajatRepository.findById(idsuperior).orElseThrow(() -> new ResourceNotFoundException("Nu există superior cu id: " + idsuperior));
			angajat.setSuperior(superior);
		}

		angajat = angajatRepository.save(angajat);
		return angajat;
	}

	public Angajat setSuperior(int idangajat, int idsuperior) throws ResourceNotFoundException {
		Angajat angajat = angajatRepository.findById(idangajat).orElseThrow(() -> new ResourceNotFoundException("Nu există angajat cu id: " + idangajat));
		Angajat superior = angajatRepository.findById(idsuperior).orElseThrow(() -> new ResourceNotFoundException("Nu există superior cu id: " + idsuperior));

		angajat.setSuperior(superior);

		return angajatRepository.save(angajat);
	}

	public List<Angajat> getSubalterni(int idangajat) throws ResourceNotFoundException {
		Angajat angajat = angajatRepository.findById(idangajat).orElseThrow(() -> new ResourceNotFoundException("Nu există angajat cu id: " + idangajat));

		return angajat.getSubalterni();
	}

	public List<Angajat> getSuperioriPosibili(int idangajat) throws ResourceNotFoundException {
		Angajat angajat = angajatRepository.findById(idangajat).orElseThrow(() -> new ResourceNotFoundException("Nu există angajat cu id: " + idangajat));

		List<Integer> subalterni = angajat.getSubalterni().stream().map(Angajat::getIdpersoana).collect(Collectors.toList());
		if (subalterni.isEmpty()) {
			return angajatRepository.findBySocietate_IdAndIdpersoanaNot(angajat.getSocietate().getId(), idangajat);
		} else {
			return angajatRepository.findBySocietate_IdAndIdpersoanaNotAndIdpersoanaNotIn(angajat.getSocietate().getId(), idangajat, subalterni);
		}
	}

	public List<Angajat> getAngajatiContracteValide(int idsocietate, int an, int luna) {
		List<Angajat> angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(idsocietate);

		// filter by ultimazilucru
		angajati.removeIf(angajat -> {
			LocalDate ultimaZiLucru = angajat.getContract().getUltimazilucru();
			if (ultimaZiLucru != null) {
				if (ultimaZiLucru.getYear() < an)
					return true;
				else if (ultimaZiLucru.getYear() == an)
					return ultimaZiLucru.getMonthValue() < luna;
				else
					return false;
			} else
				return false;
		});

		// filter by data inceput activitate
		angajati.removeIf(angajat -> {
			LocalDate primaZiLucru = angajat.getContract().getDataincepere();
			if (primaZiLucru.getYear() > an)
				return true;
			else if (primaZiLucru.getYear() == an)
				return primaZiLucru.getMonthValue() > luna;
			else
				return false;
		});


		return angajati;
	}

}
