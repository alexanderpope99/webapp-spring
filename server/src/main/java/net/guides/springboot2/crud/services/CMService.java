package net.guides.springboot2.crud.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.CMDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.CM;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.CMRepository;
import net.guides.springboot2.crud.repository.ContractRepository;

@Service
public class CMService {
	CMService() {
	}

	@Autowired
	private CMRepository cmRepository;
	@Autowired
	private AngajatRepository angajatRepository;
	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private SarbatoriService sarbatoriService;
	@Autowired
	private BazacalculService bazacalculService;
	@Autowired
	private RealizariRetineriService realizariRetineriService;

	@Autowired
	private ModelMapper modelMapper;

	public CMDTO save(CMDTO cmDTO) throws ResourceNotFoundException {
		// convert from DTO to Entity
		CM cm = modelMapper.map(cmDTO, CM.class);

		Contract contract = contractRepository.findById(cmDTO.getIdcontract())
				.orElseThrow(() -> new ResourceNotFoundException("Nu există contract cu id: " + cmDTO.getIdcontract()));
		cm.setContract(contract);

		// check if overlaps with existing concedii (odihna + medicale)
		if (cm.overlaps())
			return null;

		// save to DB
		cm = cmRepository.save(cm);

		// get luna, an from co.dela
		int luna = cm.getDela().getMonthValue();
		int an = cm.getDela().getYear();

		// update salariu
		realizariRetineriService.recalcRealizariRetineri(luna, an, contract.getId(), -1, -1, -1);

		// return sent body with correct id
		cmDTO.setId(cm.getId());
		return cmDTO;
	}

	public CMDTO update(int cmID, CMDTO newCmDTO) throws ResourceNotFoundException {
		newCmDTO.setId(cmID);
		return save(newCmDTO);
	}

	public Map<String, Boolean> delete(int cmId) throws ResourceNotFoundException {
		CM cm = cmRepository.findById(cmId)
				.orElseThrow(() -> new ResourceNotFoundException("Nu există CM cu id: " + cmId));

		cmRepository.delete(cm);

		realizariRetineriService.recalcRealizariRetineri(cm.getDela().getMonthValue(), cm.getDela().getYear(),
				cm.getContract().getId(), -1, -1, -1);

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	public int getZileCM(int luna, int an, int idcontract) {
		// find all by idcontract
		List<CM> concediiMedicale = cmRepository.findByContract_IdOrderByDelaDescPanalaDesc(idcontract);
		if (concediiMedicale.isEmpty())
			return 0;

		return zileC(luna, an, concediiMedicale);
	}

	public int getZileCMLucratoare(int luna, int an, int idcontract) {
		// find all by idcontract
		List<CM> concediiMedicale = cmRepository.findByContract_IdOrderByDelaDescPanalaDesc(idcontract);
		if (concediiMedicale.isEmpty())
			return 0;

		return zileCLucratoare(luna, an, concediiMedicale);
	}

	public List<CM> getCMInLunaAnul(int luna, int an, int idcontract) {
		// select * from cm where '2020-09-01' <= dela and '2020-09-30' >= panala
		LocalDate inceputLuna = LocalDate.of(an, luna, 1);
		int nrZileLuna = inceputLuna.getMonth().length(inceputLuna.isLeapYear());
		LocalDate sfarsitLuna = LocalDate.of(an, luna, nrZileLuna);

		return cmRepository.findByContract_IdAndDelaBetween(idcontract, inceputLuna, sfarsitLuna);
	}
	
	public int getValCM(int luna, int an, int idcontract) {
		// get idangajat of idcontract
		int idangajat = angajatRepository.findIdpersoanaByIdcontract(idcontract);
		// get cm
		List<CM> cms = this.getCMInLunaAnul(luna, an, idcontract);
		float valCM = 0;
		// media zilnica pe 6 luni = venitTotal6luni / nrZileLucrate6luni <- din bazacalcul
		float mediaZilnica = bazacalculService.getMediaZilnicaUltimele6Luni(luna, an, idangajat);
		for (CM cm : cms) {
			valCM += this.zileCLucratoare(cm) * mediaZilnica * cm.getProcent() / 100;
		}

		return Math.round(valCM);
	}

	public int getValcmFNUASS(List<CM> concediiMedicale) {
		int valcmfnuass = 0;
		for(CM cm : concediiMedicale) {
			valcmfnuass += cm.getIndemnizatiefnuass();
		}
		return valcmfnuass;
	}

	public int getZilecmFNUASS(List<CM> concediiMedicale) {
		int zilecmfnuass = 0;
		for(CM cm : concediiMedicale) {
			zilecmfnuass += cm.getZilefnuass();
		}
		return zilecmfnuass;
	}

	public int getValcmFAAMBP(List<CM> concediiMedicale) {
		int valcmfaambp = 0;
		for(CM cm : concediiMedicale) {
			valcmfaambp += cm.getIndemnizatiefaambp();
		}
		return valcmfaambp;
	}

	public int getZilecmFAAMBP(List<CM> concediiMedicale) {
		int zilecmfaambp = 0;
		for(CM cm : concediiMedicale) {
			zilecmfaambp += cm.getZilefaambp();
		}
		return zilecmfaambp;
	}

	private int zileC(int luna, int an, List<CM> concedii) {
		LocalDate inceputLuna = LocalDate.of(an, luna, 1);
		int nrZileLuna = inceputLuna.getMonth().length(inceputLuna.isLeapYear());

		LocalDate dela, panala;
		LocalDate day;
		int zileC = 0;
		for (CM concediu : concedii) {
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

	private int zileCLucratoare(int luna, int an, List<CM> concedii) {
		LocalDate inceputLuna = LocalDate.of(an, luna, 1);
		int nrZileLuna = inceputLuna.getMonth().length(inceputLuna.isLeapYear());
		LocalDate sfarsitLuna = LocalDate.of(an, luna, nrZileLuna);

		List<LocalDate> sarbatori = sarbatoriService.getZileSarbatoareInIntervalul(inceputLuna, sfarsitLuna);

		LocalDate dela, panala;
		LocalDate day;
		int zileC = 0;
		for (CM concediu : concedii) {
			dela = concediu.getDela();
			panala = concediu.getPanala();

			for (int i = 1; i <= nrZileLuna; ++i) {
				day = LocalDate.of(an, luna, i);
				if (day.compareTo(dela) >= 0 && day.compareTo(panala) <= 0 && day.getDayOfWeek().getValue() != 6
						&& day.getDayOfWeek().getValue() != 7 && !sarbatori.contains(day))
					zileC++;
			}
		}
		return zileC;
	}

	private int zileCLucratoare(CM cm) {
		LocalDate dela = cm.getDela();
		LocalDate panala = cm.getPanala();
		List<LocalDate> sarbatori = sarbatoriService.getZileSarbatoareInIntervalul(dela, panala);

		int an = dela.getYear();
		int luna = dela.getMonthValue();
		int zileC = 0;

		LocalDate day;
		for (int i = dela.getDayOfMonth(); i <= panala.getDayOfMonth(); ++i) {
			day = LocalDate.of(an, luna, i);
			if (day.getDayOfWeek().getValue() != 6 && day.getDayOfWeek().getValue() != 7 && !sarbatori.contains(day))
				zileC++;
		}

		return zileC;
	}

} // class
