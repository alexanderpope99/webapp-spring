package net.guides.springboot2.crud.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.CODTO;
import net.guides.springboot2.crud.dto.CereriConcediuDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.model.User;
import net.guides.springboot2.crud.model.CO;
import net.guides.springboot2.crud.model.CereriConcediu;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.CORepository;
import net.guides.springboot2.crud.repository.CereriConcediuRepository;
import net.guides.springboot2.crud.repository.ContractRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;
import net.guides.springboot2.crud.repository.UserRepository;

@Service
public class CereriConcediuService {
	@Autowired
	private CereriConcediuRepository cereriConcediuRepository;

	@Autowired
	private CORepository coRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AngajatRepository angajatRepository;

	@Autowired
	private SocietateRepository societateRepository;

	@Autowired
	private COService coService;

	@Autowired
	private ModelMapper modelMapper;

	public CereriConcediuDTO save(CereriConcediuDTO cerereConcediuDTO) throws ResourceNotFoundException {
		CereriConcediu cerereConcediu = modelMapper.map(cerereConcediuDTO, CereriConcediu.class);

		User user = userRepository.findById(cerereConcediuDTO.getIduser())
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id"));

		cerereConcediu.setUser(user);

		Societate societate = societateRepository.findById(cerereConcediuDTO.getIdsocietate())
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id"));

		cerereConcediu.setSocietate(societate);

		cereriConcediuRepository.save(cerereConcediu);

		cerereConcediuDTO.setId(cerereConcediu.getId());
		return cerereConcediuDTO;
	}

	public CereriConcediu setStatus(int cereriConcediuId, String status) throws ResourceNotFoundException {
		CereriConcediu cerereConcediu = cereriConcediuRepository.findById(cereriConcediuId)
				.orElseThrow(() -> new ResourceNotFoundException("Cereri Concediu not found for this id"));

		cerereConcediu.setStatus(status);

		int idcontract = angajatRepository
				.findBySocietate_IdAndUser_Id(cerereConcediu.getSocietate().getId(), cerereConcediu.getUser().getId())
				.getContract().getId();

		if (status == "Aprobat") {
			CODTO co = new CODTO(cerereConcediu.getTip(), cerereConcediu.getDela(), cerereConcediu.getPanala(),
					idcontract);
			coService.save(co);

		} else if (status == "Respins") {
			CO co = coRepository.findByTipAndDelaAndPanalaAndContract_Id(cerereConcediu.getTip(),
					cerereConcediu.getDela(), cerereConcediu.getPanala(), idcontract);
			if (co != null)
				coService.delete(co.getId());
		}
		cereriConcediuRepository.save(cerereConcediu);
		return cerereConcediu;
	}

}
