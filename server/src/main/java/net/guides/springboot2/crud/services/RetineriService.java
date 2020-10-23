package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.model.Retineri;
import net.guides.springboot2.crud.repository.RetineriRepository;

@Service
public class RetineriService {
	@Autowired
	private RetineriRepository retineriRepository;

	public Retineri saveRetinere(long idstat) {
		Retineri emptyRetinere = new Retineri(0, 0, 0, 0, 0, idstat);
		return retineriRepository.save(emptyRetinere);
	}

	public Retineri saveRetinere(Retineri retinere) {
		return retineriRepository.save(retinere);
	}

	public Retineri updateRetinere(Retineri oldRetinere, long idstat) {
		Retineri newEmptyRetinere = new Retineri(0, 0, 0, 0, 0, idstat);
		newEmptyRetinere.setId(oldRetinere.getId());
		return retineriRepository.save(newEmptyRetinere);
	}

	public Retineri getRetinereByIdstat(long idstat) {
		return retineriRepository.findByIdstat(idstat);
	}
}
