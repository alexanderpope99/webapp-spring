package net.guides.springboot2.crud.services;

import java.util.ArrayList;
import java.util.List;

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
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.CORepository;
import net.guides.springboot2.crud.repository.CereriConcediuRepository;
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

		User user = userRepository.findById(cerereConcediuDTO.getIduser()).orElseThrow(
				() -> new ResourceNotFoundException("Nu există user cu id" + cerereConcediuDTO.getIduser()));

		cerereConcediu.setUser(user);

		Societate societate = societateRepository.findById(cerereConcediuDTO.getIdsocietate()).orElseThrow(
				() -> new ResourceNotFoundException("Nu există societate cu id" + cerereConcediuDTO.getIdsocietate()));

		cerereConcediu.setSocietate(societate);

		cereriConcediuRepository.save(cerereConcediu);

		cerereConcediuDTO.setId(cerereConcediu.getId());
		return cerereConcediuDTO;
	}

	public CereriConcediu setStatus(int cereriConcediuId, String status) throws ResourceNotFoundException {
		CereriConcediu cerereConcediu = cereriConcediuRepository.findById(cereriConcediuId).orElseThrow(
				() -> new ResourceNotFoundException("Nu există cereri concediu cu id " + cereriConcediuId));

		cerereConcediu.setStatus(status);

		int idcontract = angajatRepository
				.findBySocietate_IdAndUser_Id(cerereConcediu.getSocietate().getId(), cerereConcediu.getUser().getId())
				.getContract().getId();

		if (status.equals("Aprobat")) {
			CODTO co = new CODTO(cerereConcediu.getTip(), cerereConcediu.getDela(), cerereConcediu.getPanala(),
					idcontract);
			coService.save(co);

		} else if (status.equals("Respins")) {
			CO co = coRepository.findByTipAndDelaAndPanalaAndContract_Id(cerereConcediu.getTip(),
					cerereConcediu.getDela(), cerereConcediu.getPanala(), idcontract);
			if (co != null)
				coService.delete(co.getId());
		}
		cereriConcediuRepository.save(cerereConcediu);
		return cerereConcediu;
	}

	public List<CereriConcediuDTO> getCereriConcediuWithNumeUserBySocId(int socId) throws ResourceNotFoundException {
		List<CereriConcediu> cereri = cereriConcediuRepository.findBySocietate_Id(socId);

		List<CereriConcediuDTO> cereriDTO = new ArrayList<>();
		for (CereriConcediu cerere : cereri) {
			CereriConcediuDTO cerereDTO = modelMapper.map(cerere, CereriConcediuDTO.class);
			Persoana persoana = angajatRepository.findBySocietate_IdAndUser_Id(socId, cerere.getUser().getId())
					.getPersoana();
			if (persoana == null)
				throw new ResourceNotFoundException(
						"Nu există persoană cu id societate " + socId + " și id user" + cerere.getUser().getId());
			String nume = persoana.getNume() + " " + persoana.getPrenume();
			cerereDTO.setIdsocietate(cerere.getSocietate().getId());
			cerereDTO.setIduser(cerere.getUser().getId());
			cerereDTO.setNumeuser(nume);
			cereriDTO.add(cerereDTO);
		}
		return cereriDTO;
	}

}
