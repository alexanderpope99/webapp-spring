package net.guides.springboot2.crud.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.CMDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.CM;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.LunaInchisa;
import net.guides.springboot2.crud.repository.CMRepository;
import net.guides.springboot2.crud.repository.ContractRepository;

@Service
public class CMService {
	CMService() {
	}

	@Autowired
	private CMRepository cmRepository;
	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private SarbatoriService sarbatoriService;
	@Autowired
	private RealizariRetineriService realizariRetineriService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private LunaInchisaService lunaInchisaService;

	@Autowired
	private ModelMapper modelMapper;

	public List<CM> findBySocietate_Id(int id) {
		return cmRepository.findByContract_Angajat_Societate_Id(id);
	}

  public CM findByContract_IdAndDela(int idcontract, LocalDate dela) throws ResourceNotFoundException {
    return cmRepository.findByContract_IdAndDela(idcontract, dela).orElseThrow(() -> new ResourceNotFoundException("Nu exista concediu medical care incepe la " + dela.toString()));
  }

	public CMDTO save(CMDTO cmDTO) throws ResourceNotFoundException {
		// convert from DTO to Entity
		CM cm = modelMapper.map(cmDTO, CM.class);

		Contract contract = contractRepository.findById(cmDTO.getIdcontract()).orElseThrow(() -> new ResourceNotFoundException("Nu există contract cu id: " + cmDTO.getIdcontract()));
		cm.setContract(contract);

		List<LunaInchisa> luniInchise = lunaInchisaService.findBySocietate_Id(contract.getAngajat().getSocietate().getId());
		// nu se suprapune cu un alt concediu / nu este intr-o luna inchisa
		if (cm.overlaps(luniInchise))
			throw new ResourceNotFoundException("Concediul se suprapune cu unul existent sau este intr-o luna închisă");

		// save to DB
		cm = cmRepository.save(cm);

		// get luna, an from co.dela
		int luna = cm.getDela().getMonthValue();
		int an = cm.getDela().getYear();
		// daca nu se termina in aceeasi luna
		if(cm.getPanala().getYear() != an || cm.getPanala().getMonthValue() != luna) {
			realizariRetineriService.recalcRealizariRetineri(cm.getPanala().getYear(), cm.getPanala().getMonthValue(), contract.getId());
		}
		// update salariu
		realizariRetineriService.recalcRealizariRetineri(luna, an, contract.getId());

		// return updated cm
		cmDTO.setId(cm.getId());
		return cmDTO;
	}

	public CMDTO update(int cmID, CMDTO newCmDTO) throws ResourceNotFoundException {
		newCmDTO.setId(cmID);
		return save(newCmDTO);
	}

	public Map<String, Boolean> delete(int cmId) throws ResourceNotFoundException {
		CM cm = cmRepository.findById(cmId).orElseThrow(() -> new ResourceNotFoundException("Nu există CM cu id: " + cmId));

		cmRepository.delete(cm);

		realizariRetineriService.recalcRealizariRetineri(cm.getDela().getMonthValue(), cm.getDela().getYear(), cm.getContract().getId(), -1, -1, -1, -1);

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
		LocalDate inceputLuna = LocalDate.of(an, luna, 1);
		int nrZileLuna = inceputLuna.getMonth().length(inceputLuna.isLeapYear());
		LocalDate sfarsitLuna = LocalDate.of(an, luna, nrZileLuna);

		return cmRepository.findByContract_IdAndDelaBetween(idcontract, inceputLuna, sfarsitLuna);
	}

	public int getValCM(int luna, int an, int idcontract) {
		List<CM> cms = this.getCMInLunaAnul(luna, an, idcontract);
		float valCM = 0;
		for (CM cm : cms) {
			valCM += this.zileCLucratoare(cm) * cm.getMediezilnica() * cm.getProcent() / 100;
		}

		return Math.round(valCM);
	}

	public int getValCMScutitImpozit(int luna, int an, int idcontract) throws ResourceNotFoundException {
		Contract contract = contractService.findById(idcontract);

		List<CM> toateConcediile = this.getCMInLunaAnul(luna, an, idcontract);
		Set<String> coduriScutite = Set.of("08", "09", "15");

		// daca e scutit de impozit, concediul medical nu intra in baza calcul
		if (!contract.isCalculdeduceri()) {
			float valCM = 0;
			for (CM cm : toateConcediile) {
				valCM += this.zileCLucratoare(cm) * cm.getMediezilnica() * cm.getProcent() / 100;
			}

			return Math.round(valCM);
		}

		toateConcediile.removeIf(cm -> {
			String cod = cm.getCodboala();
			if (cod == null || cod.isEmpty())
				return true;
			cod = cod.substring(0, 2);
			return !coduriScutite.contains(cod);
		});

		float valCM = 0;
		for (CM cm : toateConcediile) {
			valCM += this.zileCLucratoare(cm) * cm.getMediezilnica() * cm.getProcent() / 100;
		}

		return Math.round(valCM);
	}

	public int getValcmFNUASS(List<CM> concediiMedicale) {
		int valcmfnuass = 0;
		for (CM cm : concediiMedicale) {
			valcmfnuass += cm.getIndemnizatiefnuass();
		}
		return valcmfnuass;
	}

	public int getZilecmFNUASS(List<CM> concediiMedicale) {
		int zilecmfnuass = 0;
		for (CM cm : concediiMedicale) {
			zilecmfnuass += cm.getZilefnuass();
		}
		return zilecmfnuass;
	}

	public int getValcmFAAMBP(List<CM> concediiMedicale) {
		int valcmfaambp = 0;
		for (CM cm : concediiMedicale) {
			valcmfaambp += cm.getIndemnizatiefaambp();
		}
		return valcmfaambp;
	}

	public int getZilecmFAAMBP(List<CM> concediiMedicale) {
		int zilecmfaambp = 0;
		for (CM cm : concediiMedicale) {
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
				if (day.compareTo(dela) >= 0 && day.compareTo(panala) <= 0 && day.getDayOfWeek().getValue() != 6 && day.getDayOfWeek().getValue() != 7 && !sarbatori.contains(day))
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
