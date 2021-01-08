package net.guides.springboot2.crud.services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.model.Sarbatori;
import net.guides.springboot2.crud.repository.SarbatoriRepository;

@Service
public class SarbatoriService {
	SarbatoriService() {}

	@Autowired
	private ZileService zileService;

	@Autowired
	private SarbatoriRepository sarbatoriRepository;

	public int getNrZileSarbatoareInLunaAnul(int luna, int an) {
		LocalDate inceputLuna = LocalDate.of(an, luna, 1);
		int nrZileLuna = YearMonth.of(an, luna).lengthOfMonth();
		LocalDate sfarsitLuna = LocalDate.of(an, luna, nrZileLuna);

		List<Sarbatori> sarbatori = sarbatoriRepository.findByDelaBetween(inceputLuna, sfarsitLuna);

		int nrZileSarbatoare = 0;

		for(Sarbatori sarbatoare : sarbatori) {
			if(sarbatoare.getDela().compareTo(sarbatoare.getPanala()) == 0)
				nrZileSarbatoare++;
			else
				nrZileSarbatoare += (int)zileService.getZileLucratoareInIntervalNoHolidays(sarbatoare.getDela(), sarbatoare.getPanala());
		}

		return nrZileSarbatoare;
	}

	public List<LocalDate> getZileSarbatoareInLunaAnul(int luna, int an) {
		LocalDate inceputLuna = LocalDate.of(an, luna, 1);
		int nrZileLuna = inceputLuna.getMonth().length(inceputLuna.isLeapYear());
		LocalDate sfarsitLuna = LocalDate.of(an, luna, nrZileLuna);
		return this.getZileSarbatoareInIntervalul(inceputLuna, sfarsitLuna);
	}

	public List<LocalDate> getZileSarbatoareInIntervalul(LocalDate dela, LocalDate panala) {
		List<LocalDate> sarbatoriDates = new ArrayList<>();

		List<Sarbatori> sarbatori = sarbatoriRepository.findByDelaBetween(dela, panala);
		if(sarbatori == null || sarbatori.isEmpty())
			return sarbatoriDates;

		for(Sarbatori sarbatoare : sarbatori) {
			if(sarbatoare.getDela().compareTo(sarbatoare.getPanala()) == 0)
				sarbatoriDates.add(sarbatoare.getDela());
			else {
				List<LocalDate> zileSarbatoare = sarbatoare.getDela().datesUntil(sarbatoare.getPanala().plusDays(1)).collect(Collectors.toList());

				sarbatoriDates.addAll(zileSarbatoare);
			}
		}

		return sarbatoriDates;
	}
	
	public void initialize(int an) {
		sarbatoriRepository.save(new Sarbatori(an+"-01-01", an+"-01-02", "Anul Nou"));

		sarbatoriRepository.save(new Sarbatori(an+"-01-24", an+"-01-24", "Ziua Unirii Principatelor Române"));

		sarbatoriRepository.save(new Sarbatori(an+"-05-01", an+"-05-01", "Ziua Muncii"));

		sarbatoriRepository.save(new Sarbatori(an+"-06-01", an+"-06-01", "Ziua Copilului"));

		sarbatoriRepository.save(new Sarbatori(an+"-08-15", an+"-08-15", "Adormirea Maicii Domnului"));

		sarbatoriRepository.save(new Sarbatori(an+"-11-30", an+"-11-30", "Sfântul Andrei"));

		sarbatoriRepository.save(new Sarbatori(an+"-12-01", an+"-12-01", "Ziua Națională a României"));

		sarbatoriRepository.save(new Sarbatori(an+"-12-25", an+"-12-26", "Crăciunul"));
	}

	public void initializeKnown() {
		if(sarbatoriRepository.count() > 0) return;

		initialize(2019);
		initialize(2020);
		initialize(2021);

		sarbatoriRepository.save(new Sarbatori("2019-04-26", "2019-04-26", "Vinerea mare"));
		sarbatoriRepository.save(new Sarbatori("2019-04-28", "2019-04-29", "Paște ortodox"));
		sarbatoriRepository.save(new Sarbatori("2019-06-16", "2019-06-17", "Rusalii"));

		sarbatoriRepository.save(new Sarbatori("2020-04-17", "2020-04-17", "Vinerea mare"));
		sarbatoriRepository.save(new Sarbatori("2020-04-19", "2020-04-20", "Paște ortodox"));
		sarbatoriRepository.save(new Sarbatori("2020-06-07", "2020-06-08", "Rusalii"));

		sarbatoriRepository.save(new Sarbatori("2021-04-30", "2021-04-30", "Vinerea mare"));
		sarbatoriRepository.save(new Sarbatori("2021-05-02", "2021-05-03", "Paște ortodox"));
		sarbatoriRepository.save(new Sarbatori("2021-06-20", "2021-06-21", "Rusalii"));
	}
}
