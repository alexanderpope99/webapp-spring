package net.guides.springboot2.crud.services;

import org.modelmapper.ModelMapper;
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
	private ModelMapper modelMapper;

	@Autowired
	private RealizariRetineriRepository realizariRetineriRepository;
	@Autowired
	private OresuplimentareRepository oresuplimentareRepository;

	public Oresuplimentare save(Oresuplimentare oresuplimentare) throws ResourceNotFoundException {
		Oresuplimentare os = modelMapper.map(oresuplimentare, Oresuplimentare.class);

		RealizariRetineri realizariRetineri = realizariRetineriRepository.findById(oresuplimentare.getStatsalariat().getId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Nu există realizară rețineri cu id: " + oresuplimentare.getStatsalariat().getId()));

		// os.setStatsalariat(realizariRetineri);
		os = oresuplimentareRepository.save(os);
		os.setId(os.getId());

		realizariRetineri.addOreSuplimentare(os);
		realizariRetineriRepository.save(realizariRetineri);

		return oresuplimentare;
	}

	public Oresuplimentare update(int osID, Oresuplimentare newOs) throws ResourceNotFoundException {
		newOs.setId(osID);
		return save(newOs);
	}
}
