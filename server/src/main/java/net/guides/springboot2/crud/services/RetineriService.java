package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Retineri;
import net.guides.springboot2.crud.repository.RetineriRepository;

@Service
public class RetineriService {
	@Autowired
	private RetineriRepository retineriRepository;

	public Retineri saveRetinere(RealizariRetineri stat) {
		Retineri emptyRetinere = new Retineri(0, 0, 0, 0, 0, stat);
		return retineriRepository.save(emptyRetinere);
	}

	public Retineri saveRetinere(Retineri retinere) {
		return retineriRepository.save(retinere);
	}

	public Retineri updateRetinere(Retineri oldRetinere, RealizariRetineri stat) {

		Retineri newEmptyRetinere = new Retineri(0, 0, 0, 0, 0, stat);
		newEmptyRetinere.setId(oldRetinere.getId());
		return retineriRepository.save(newEmptyRetinere);
	}

	public Retineri getRetinereByIdstat(int stat) {
		return retineriRepository.findByStat(stat);
	}
}
