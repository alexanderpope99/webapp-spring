package net.guides.springboot2.crud.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.OresuplimentareDTO;
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

	public OresuplimentareDTO save(OresuplimentareDTO osDTO) throws ResourceNotFoundException {
		Oresuplimentare os = modelMapper.map(osDTO, Oresuplimentare.class);

		RealizariRetineri realizariRetineri = realizariRetineriRepository.findById(osDTO.getIdstatsalariat())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Nu există realizară rețineri cu id: " + osDTO.getIdstatsalariat()));

		os.setStatsalariat(realizariRetineri);
		os = oresuplimentareRepository.save(os);
		os.setId(os.getId());
		return osDTO;
	}

	public OresuplimentareDTO update(int osID, OresuplimentareDTO newOsDTO) throws ResourceNotFoundException {
		newOsDTO.setId(osID);
		return save(newOsDTO);
	}
}
