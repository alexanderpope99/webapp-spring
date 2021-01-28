package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Oresuplimentare;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.repository.OresuplimentareRepository;
import net.guides.springboot2.crud.repository.RealizariRetineriRepository;

@Service
public class OresuplimentareService {

	@Autowired
	private RealizariRetineriRepository realizariRetineriRepository;
	@Autowired
	private OresuplimentareRepository oresuplimentareRepository;

	public Oresuplimentare save(Oresuplimentare oresuplimentare) throws ResourceNotFoundException {
		RealizariRetineri realizariRetineri = realizariRetineriRepository.findById(oresuplimentare.getStatsalariat().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Nu există realizări rețineri cu id-ul respectiv"));

		// os.setStatsalariat(realizariRetineri);
		oresuplimentare.setStatsalariat(realizariRetineri);
		oresuplimentare = oresuplimentareRepository.save(oresuplimentare);

		realizariRetineri.addOreSuplimentare(oresuplimentare);
		realizariRetineriRepository.save(realizariRetineri);

		return oresuplimentare;
	}

	public Oresuplimentare update(int osID, Oresuplimentare newOs) throws ResourceNotFoundException {
		newOs.setId(osID);
		return save(newOs);
	}
}
