package net.guides.springboot2.crud.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.PersoanaIntretinereDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.PersoanaIntretinere;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.PersoanaIntretinereRepository;

@Service
public class PersoanaIntretinereService {
	@Autowired
	private PersoanaIntretinereRepository persoanaIntretinereRepository;
	@Autowired
	private AngajatRepository angajatRepository;

	@Autowired
	private ModelMapper modelMapper;

	public PersoanaIntretinereDTO save(PersoanaIntretinereDTO piDTO) throws ResourceNotFoundException {
		PersoanaIntretinere pi = modelMapper.map(piDTO, PersoanaIntretinere.class);

		Angajat angajat = angajatRepository.findById(piDTO.getIdangajat())
				.orElseThrow(() -> new ResourceNotFoundException("Nu există angajat cu id :: " + piDTO.getIdangajat()));
		pi.setAngajat(angajat);

		persoanaIntretinereRepository.save(pi);
		piDTO.setId(pi.getId());
		return piDTO;
	}

	public PersoanaIntretinereDTO update(int id, PersoanaIntretinereDTO piDTO) throws ResourceNotFoundException {
		piDTO.setId(id);
		return save(piDTO);
	}

	public String nrPersoaneIntretinereToString(int nr) {
		switch (nr) {
			case 0:
				return "zero";
			case 1:
				return "una";
			case 2:
				return "doua";
			case 3:
				return "trei";
			case 4:
				return "patru";
			default:
				return "patru";
		}
	}

	public int getNrPersoaneIntretinere(int idcontract) {
		return persoanaIntretinereRepository.findByIdcontract(idcontract).size();
	}

	public String getStrPersoaneIntretinere(int idcontract) {
		int nr = getNrPersoaneIntretinere(idcontract);
		return nrPersoaneIntretinereToString(nr);
	}
}
