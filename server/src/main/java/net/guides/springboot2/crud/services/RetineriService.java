package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.RetineriDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Retineri;
import net.guides.springboot2.crud.repository.RealizariRetineriRepository;
import net.guides.springboot2.crud.repository.RetineriRepository;

@Service
public class RetineriService {
	@Autowired
	private RetineriRepository retineriRepository;

	@Autowired
	private RealizariRetineriRepository realizariRetineriRepository;

	public Retineri saveRetinere(RealizariRetineri stat) {
		Retineri emptyRetinere = new Retineri(0, 0, 0, 0, 0, stat);
		return retineriRepository.save(emptyRetinere);
	}

	public Retineri saveRetinere(Retineri retinere) {
		return retineriRepository.save(retinere);
	}

	public Retineri emptyRetinere(Retineri oldRetinere, RealizariRetineri stat) {
		Retineri newEmptyRetinere = new Retineri(0, 0, 0, 0, 0, stat);
		newEmptyRetinere.setId(oldRetinere.getId());
		return retineriRepository.save(newEmptyRetinere);
	}

	public Retineri updateRetinere(int oldRetinereID, RetineriDTO newRetinereDTO) throws ResourceNotFoundException {

		RealizariRetineri realizariRetineri = realizariRetineriRepository.findById(newRetinereDTO.getIdstat()).orElseThrow(() -> new ResourceNotFoundException("RealizaiRetineri not found for this id"));
		
		Retineri newRetinere = new Retineri(
			newRetinereDTO.getAvansnet(),
			newRetinereDTO.getPensiefacultativa(),
			newRetinereDTO.getPensiealimentara(),
			newRetinereDTO.getPopriri(),
			newRetinereDTO.getImprumuturi(),
			realizariRetineri
		);
		newRetinere.setId(oldRetinereID);

		return newRetinere;
	}

	public Retineri getRetinereByIdstat(int stat) {
		return retineriRepository.findByIdstat(stat);
	}
}
