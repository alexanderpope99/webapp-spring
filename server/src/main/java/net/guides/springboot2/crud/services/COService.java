package net.guides.springboot2.crud.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private RealizariRetineriService realizaiRetineriService;

	@Autowired
	private CORepository coRepository;
	@Autowired
	private ContractRepository contractRepository;

	public CO overlaps(CO co, List<CO> concedii) {
		concedii.removeIf(c -> c.getId() == co.getId());
		LocalDate dela = co.getDela();
		LocalDate panala = co.getPanala();

		// scrise separat pentru lizibilitate
		for (CO concediu : concedii) {
			// co includes concediu.dela: co.dela < concediu.dela < co.panala
			if (dela.compareTo(concediu.getDela()) >= 0 && panala.compareTo(concediu.getDela()) <= 0)
				return concediu;

			// co includes concediu.panala: co.dela < concediu.dela < co.panala
			if (dela.compareTo(concediu.getPanala()) >= 0 && panala.compareTo(concediu.getPanala()) <= 0)
				return concediu;

			// overlaps - co.dela inside concediu: concediu.dela < co.dela < concediu.panala
			if (dela.compareTo(concediu.getDela()) >= 0 && dela.compareTo(concediu.getPanala()) <= 0) {
				return concediu;
			}

			// overlaps co.panala inside concediu: concediu.dela < co.panala <
			// concediu.panla
			if (panala.compareTo(concediu.getDela()) >= 0 && panala.compareTo(concediu.getPanala()) <= 0) {
				return concediu;
			}
		}

		return null;
	}

	public CODTO overlapsResponse(CO co) {
		List<CO> concediiExistente = coRepository.findByContract_Id(co.getContract().getId());

		CO concediuOverlapped = this.overlaps(co, concediiExistente);

		if (concediuOverlapped != null)
			return modelMapper.map(concediuOverlapped, CODTO.class);
		else
			return null;
	}

	public CODTO overlapsResponse(CODTO coDTO) {
		List<CO> concediiExistente = coRepository.findByContract_Id(coDTO.getIdcontract());

		CO co = modelMapper.map(coDTO, CO.class);

		CO concediuOverlapped = this.overlaps(co, concediiExistente);

		if (concediuOverlapped != null)
			return modelMapper.map(concediuOverlapped, CODTO.class);
		else
			return null;
	}

	public CODTO save(CODTO coDTO) throws ResourceNotFoundException {
		CO co = modelMapper.map(coDTO, CO.class);

		// get luna, an from co.dela
		int luna = co.getDela().getMonthValue();
		int an = co.getDela().getYear();

		Contract contract = contractRepository.findById(coDTO.getIdcontract())
				.orElseThrow(() -> new ResourceNotFoundException("Nu există contract cu id:" + coDTO.getIdcontract()));

		co.setContract(contract);
		// verifica ca nu se suprapune cu alt concediu
		if (co.overlaps())
			return null;

		coRepository.save(co);

		// update salariu
		realizaiRetineriService.recalcRealizariRetineri(luna, an, contract.getId(), -1, -1, -1);

		coDTO.setId(co.getId());
		return coDTO;
	}

	public CODTO update(int coID, CODTO newCoDTO) throws ResourceNotFoundException {
		newCoDTO.setId(coID);
		return save(newCoDTO);
	}

	public Map<String, Boolean> fixConcedii() throws ResourceNotFoundException {
		List<CO> co = coRepository.findAll();

		for(CO concediu: co)
		{
			if(concediu.getDela().getYear()<1900 || concediu.getPanala().getYear()<1900)
				coRepository.delete(concediu);
		}


		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	public Map<String, Boolean> delete(int coId) throws ResourceNotFoundException {
		CO co = coRepository.findById(coId)
				.orElseThrow(() -> new ResourceNotFoundException("Nu există CO cu id: " + coId));

		coRepository.delete(co);

		realizaiRetineriService.recalcRealizariRetineri(co.getDela().getMonthValue(), co.getDela().getYear(),
				co.getContract().getId(), -1, -1, -1);

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	public int getZileCODisponibile(int idcontract) throws ResourceNotFoundException {
		// get contract -> zilecoan
		Contract contract = contractRepository.findById(idcontract)
				.orElseThrow(() -> new ResourceNotFoundException("Nu există contract cu id " + idcontract));

		int zilecodisponibile = 0;
		// get concedii odihna (cu plata)
		List<CO> concedii = coRepository.findByContract_IdAndTip(idcontract, "Concediu de odihnă");

		LocalDate today = LocalDate.now();

		long luniLucrate = ChronoUnit.MONTHS.between(contract.getDataincepere(), today) - 1;

		int zilecoan = contract.getZilecoan();
		float zilecopeluna = (float) zilecoan / 12;

		// * calculeaza cate zile are in primul an de activitate (cel mai probabil nu
		// are 21)
		// exclude luna in care a inceput activitatea
		int zileconcediu = this.zileC(concedii);
		zilecodisponibile = Math.round(luniLucrate * zilecopeluna) - zileconcediu;

		return zilecodisponibile;
	}

	public int getZileCOTotal(int luna, int an, int idcontract) {
		List<CO> concediiOdihna = coRepository.findByContract_Id(idcontract);

		return zileC(luna, an, concediiOdihna);
	}

	public int getZileCOLucratoareTotal(int luna, int an, int idcontract) {
		List<CO> concediiOdihna = coRepository.findByContract_Id(idcontract);

		return zileCLucratoare(luna, an, concediiOdihna);
	}

	public int getZileCFP(int luna, int an, int idcontract) {
		List<CO> concediiOdihnaFaraPlata = coRepository.findByContract_IdAndTip(idcontract, "Concediu fără plată");

		return zileC(luna, an, concediiOdihnaFaraPlata);
	}

	public int getZileCFPLucratoare(int luna, int an, int idcontract) {
		List<CO> concediiOdihnaFaraPlata = coRepository.findByContract_IdAndTip(idcontract, "Concediu fără plată");

		return zileCLucratoare(luna, an, concediiOdihnaFaraPlata);
	}

	public int getZileCS(int luna, int an, int idcontract) {
		List<CO> cs = coRepository.findByContract_IdAndTip(idcontract, "Concediu pentru studii");

		return zileC(luna, an, cs);
	}

	public int getZileST(int luna, int an, int idcontract) {
		List<CO> st = coRepository.findByContract_IdAndTip(idcontract, "Concediu pentru situații speciale");

		return zileC(luna, an, st);
	}

	public int getZileCO(int luna, int an, int idcontract) {
		List<CO> st = coRepository.findByContract_IdAndTip(idcontract, "Concediu de odihnă");

		return zileC(luna, an, st);
	}

	public int getZileCOLucratoare(int luna, int an, int idcontract) {
		List<CO> concediiOdihna = coRepository.findByContract_IdAndTip(idcontract, "Concediu de odihnă");

		return zileCLucratoare(luna, an, concediiOdihna);
	}

	private int zileC(List<CO> concedii) {
		if (concedii.isEmpty())
			return 0;

		LocalDate dela, panala;
		int zileC = 0;
		for (CO concediu : concedii) {
			dela = concediu.getDela();
			panala = concediu.getPanala();

			zileC += ChronoUnit.DAYS.between(dela, panala) + 1;
		}

		return zileC;
	}

	private int zileC(int luna, int an, List<CO> concedii) {
		if (concedii.isEmpty())
			return 0;

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
		if (concedii.isEmpty())
			return 0;

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
				if (day.compareTo(dela) >= 0 && day.compareTo(panala) <= 0 && day.getDayOfWeek().getValue() != 6
						&& day.getDayOfWeek().getValue() != 7 && !sarbatori.contains(day))
					zileC++;
			}
		}
		return zileC;
	}

} // class
