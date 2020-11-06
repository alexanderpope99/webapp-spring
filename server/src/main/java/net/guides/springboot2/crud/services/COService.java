package net.guides.springboot2.crud.services;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.CODTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.CO;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.repository.CORepository;
import net.guides.springboot2.crud.repository.ContractRepository;

@Service
public class COService {
	COService() {
	}
	
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private SarbatoriService sarbatoriService;
	@Autowired
	private CORepository coRepository;
	@Autowired
	private ContractRepository contractRepository;

	public CO save(CODTO coDTO) throws ResourceNotFoundException {
		CO co = modelMapper.map(coDTO, CO.class);
		
		Contract contract = contractRepository.findById(coDTO.getIdcontract())
				.orElseThrow(() -> new ResourceNotFoundException("Contract not found for this id"));

		co.setContract(contract);
		return coRepository.save(co);
	}

	public int getZileCFP(int luna, int an, int idcontract) {
		List<CO> concediiOdihnaNeplatite = coRepository.findByIdcontractAndTip(idcontract, "Concediu fără plată");
		if (concediiOdihnaNeplatite.size() == 0)
			return 0;

		return zileC(luna, an, concediiOdihnaNeplatite);
	}

	public int getZileCFPLucratoare(int luna, int an, int idcontract) {
		List<CO> concediiOdihnaNeplatite = coRepository.findByIdcontractAndTip(idcontract, "Concediu fără plată");
		if (concediiOdihnaNeplatite.size() == 0)
			return 0;

		return zileCLucratoare(luna, an, concediiOdihnaNeplatite);
	}

	public int getZileCOTotal(int luna, int an, int idcontract) {
		List<CO> concediiOdihna = coRepository.findByIdcontract(idcontract);
		if (concediiOdihna.size() == 0)
			return 0;

		return zileC(luna, an, concediiOdihna);
	}

	public int getZileCOLucratoare(int luna, int an, int idcontract) {
		List<CO> concediiOdihna = coRepository.findByIdcontract(idcontract);
		if (concediiOdihna.size() == 0)
			return 0;

		return zileCLucratoare(luna, an, concediiOdihna);
	}

	public int getZileCS(int luna, int an, int idcontract) {
		List<CO> cs = coRepository.findByIdcontractAndTip(idcontract, "Concediu pentru studii");
		if (cs.size() == 0)
			return 0;

		return zileC(luna, an, cs);
	}

	public int getZileST(int luna, int an, int idcontract) {
		List<CO> st = coRepository.findByIdcontractAndTip(idcontract, "Concediu pentru situații speciale");
		if (st.size() == 0)
			return 0;

		return zileC(luna, an, st);
	}

	public int getZileCO(int luna, int an, int idcontract) {
		List<CO> st = coRepository.findByIdcontractAndTip(idcontract, "Concediu de odihnă");
		if (st.size() == 0)
			return 0;

		return zileC(luna, an, st);
	}

	private int zileC(int luna, int an, List<CO> concedii) {
		LocalDate inceputLuna = LocalDate.of(an, luna, 1);
		int nrZileLuna = inceputLuna.getMonth().length(inceputLuna.isLeapYear());

		LocalDate dela, panala;
		LocalDate day;
		int zileC = 0;
		for (CO concediu : concedii) {
			dela = concediu.getDela();
			panala = concediu.getPanala();

			for (int i = 1; i <= nrZileLuna; ++i) {
				day = LocalDate.of(an, luna, i);
				if (day.compareTo(dela) >= 0 && day.compareTo(panala) <= 0)
					zileC++;
			}
		}
		return zileC;
	}

	private int zileCLucratoare(int luna, int an, List<CO> concedii) {
		LocalDate inceputLuna = LocalDate.of(an, luna, 1);
		int nrZileLuna = inceputLuna.getMonth().length(inceputLuna.isLeapYear());
		LocalDate sfarsitLuna = LocalDate.of(an, luna, nrZileLuna);
		List<LocalDate> sarbatori = sarbatoriService.getZileSarbatoareInIntervalul(inceputLuna, sfarsitLuna);

		LocalDate dela, panala;
		LocalDate day;
		int zileC = 0;
		for (CO concediu : concedii) {
			dela = concediu.getDela();
			panala = concediu.getPanala();

			for (int i = 1; i <= nrZileLuna; ++i) {
				day = LocalDate.of(an, luna, i);
				if (day.compareTo(dela) >= 0 && day.compareTo(panala) <= 0)
					if (day.getDayOfWeek().getValue() != 6 && day.getDayOfWeek().getValue() != 7 && !sarbatori.contains(day))
						zileC++;
			}
		}
		return zileC;
	}

} // class
