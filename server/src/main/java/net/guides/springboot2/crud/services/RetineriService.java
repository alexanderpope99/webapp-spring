package net.guides.springboot2.crud.services;

import org.modelmapper.ModelMapper;
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
	private ModelMapper modelMapper;

	@Autowired
	private RetineriRepository retineriRepository;

	@Autowired
	private RealizariRetineriRepository realizariRetineriRepository;

	public Retineri saveRetinere(RealizariRetineri stat) {
		Retineri emptyRetinere = new Retineri(stat);
		return retineriRepository.save(emptyRetinere);
	}

	public Retineri saveRetinere(Retineri retinere) {
		return retineriRepository.save(retinere);
	}

	public Retineri emptyRetinere(Retineri oldRetinere, RealizariRetineri stat) {
		Retineri newEmptyRetinere = new Retineri(stat);
		newEmptyRetinere.setId(oldRetinere.getId());
		return retineriRepository.save(newEmptyRetinere);
	}

	public Retineri updateRetinere(RetineriDTO newRetinereDTO) throws ResourceNotFoundException {

		RealizariRetineri realizariRetineri = realizariRetineriRepository.findById(newRetinereDTO.getIdstat())
				.orElseThrow(() -> new ResourceNotFoundException("RealizariRetineri not found for this id"));

		Retineri newRetinere = modelMapper.map(newRetinereDTO, Retineri.class);
		newRetinere.setStat(realizariRetineri);

		return retineriRepository.save(newRetinere);
	}

	public Integer calculeazaPensieDeductibila(int idc, int an, int luna) {
		Float totalPensie = retineriRepository.getTotalPensieFacByYear(idc, an);
		if (totalPensie == null || totalPensie > 400)
			return 0;
		else
			return retineriRepository.findByStat_Contract_IdAndStat_LunaAndStat_An(idc, luna, an).getPensiefacangajat();
	}

	public Retineri getRetinereByIdstat(int stat) {
		return retineriRepository.findByStat_Id(stat);
	}

	public Retineri getRetinereByIdcontractAndLunaAndAn(int idcontract, int luna, int an) {
		return retineriRepository.findByStat_Contract_IdAndStat_LunaAndStat_An(idcontract, luna, an);
	}
}
