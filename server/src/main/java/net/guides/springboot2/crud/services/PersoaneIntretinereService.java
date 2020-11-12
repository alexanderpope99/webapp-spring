package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.repository.PersoanaIntretinereRepository;

@Service
public class PersoaneIntretinereService {
	@Autowired
	private PersoanaIntretinereRepository persoanaIntretinereRepository;

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
