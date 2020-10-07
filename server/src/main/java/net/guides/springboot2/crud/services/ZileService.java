package net.guides.springboot2.crud.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.model.Contract;

@Service
public class ZileService {
	ZileService() {
	}

	@Autowired
	private SarbatoriService sarbatoriService;

	public long getZileLucratoareInInterval(LocalDate startDate, LocalDate endDate) {
		List<LocalDate> holidays = sarbatoriService.getZileSarbatoareInIntervalul(startDate, endDate);

		if (startDate == null || endDate == null || holidays == null) {
			throw new IllegalArgumentException(
					"Invalid method argument(s) to countBusinessDaysBetween(" + startDate + "," + endDate + "," + holidays + ")");
		}

		Predicate<LocalDate> isHoliday = date -> holidays.contains(date);

		Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
				|| date.getDayOfWeek() == DayOfWeek.SUNDAY;

		long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

		long businessDays = Stream.iterate(startDate, date -> date.plusDays(1)).limit(daysBetween)
				.filter(isHoliday.or(isWeekend).negate()).count();
		return businessDays;
	}

	public long getZileLucratoareInIntervalNoHolidays(LocalDate startDate, LocalDate endDate) {

		if (startDate == null || endDate == null) {
			throw new IllegalArgumentException(
					"Invalid method argument(s) to countBusinessDaysBetween(" + startDate + "," + endDate + ")");
		}

		Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
				|| date.getDayOfWeek() == DayOfWeek.SUNDAY;

		long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

		long businessDays = Stream.iterate(startDate, date -> date.plusDays(1)).limit(daysBetween + 1)
				.filter(isWeekend.negate()).count();
		return businessDays;
	}

	public int getZileLucratoareInLunaAnul(int month, int year) {
		int zileSarbatoare = sarbatoriService.getNrZileSarbatoareInLunaAnul(month, year);

		int weekdayNr;
		int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
		int workingDays = 0;
		for (int i = 1; i <= daysInMonth; ++i) {
			weekdayNr = LocalDate.of(year, month, i).getDayOfWeek().getValue();
			if (weekdayNr != 6 && weekdayNr != 7)
				workingDays++;
		}
		return workingDays - zileSarbatoare;
	}

	// include sarbatori
	public int getNrZileLucratoareContract(int luna, int an, Contract contract) {
		LocalDate dataincepere = contract.getDataincepere();

		if (dataincepere == null)
			return 0;
		LocalDate primaZiLunaAn = LocalDate.of(an, luna, 1);

		// contractul cuprinde intreaga luna
		if (dataincepere.compareTo(primaZiLunaAn) < 0)
			return getZileLucratoareInLunaAnul(luna, an);
		else {
			LocalDate ultimaZiLunaAn = LocalDate.of(an, luna, YearMonth.of(an, luna).lengthOfMonth());

			// contractul incepe in luna urmatoare
			if (dataincepere.compareTo(ultimaZiLunaAn) > 0)
				return 0;

			// contractul incepe in luna, an
			else
				return (int)getZileLucratoareInInterval(dataincepere, ultimaZiLunaAn);
		}
	}

	public String getNumeLunaByNr(int nrLuna) {
		switch (nrLuna) {
			case 1:
				return "Ianuarie";
			case 2:
				return "Februarie";
			case 3:
				return "Martie";
			case 4:
				return "Aprilie";
			case 5:
				return "Mai";
			case 6:
				return "Iunie";
			case 7:
				return "Iulie";
			case 8:
				return "August";
			case 9:
				return "Septembrie";
			case 10:
				return "Octombrie";
			case 11:
				return "Noiembrie";
			case 12:
				return "Decembrie";

			default:
				return "Ianuarie";
		}
	}
}
