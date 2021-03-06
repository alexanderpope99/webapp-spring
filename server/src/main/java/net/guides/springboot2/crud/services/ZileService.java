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

	@Autowired
	private SarbatoriService sarbatoriService;

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

	public long getZileLucratoareInInterval(LocalDate startDate, LocalDate endDate) {
		List<LocalDate> holidays = sarbatoriService.getZileSarbatoareInIntervalul(startDate, endDate);

		if (startDate == null || endDate == null || holidays == null) {
			throw new IllegalArgumentException("Invalid method argument(s) to countBusinessDaysBetween(" + startDate + "," + endDate + "," + holidays + ")");
		}

		Predicate<LocalDate> isHoliday = date -> holidays.contains(date);

		Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;

		long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

		return Stream.iterate(startDate, date -> date.plusDays(1)).limit(daysBetween).filter(isHoliday.or(isWeekend).negate()).count();
	}

	public long getZileLucratoareInIntervalNoHolidays(LocalDate startDate, LocalDate endDate) {

		if (startDate == null || endDate == null) {
			throw new IllegalArgumentException("Invalid method argument(s) to countBusinessDaysBetween(" + startDate + "," + endDate + ")");
		}

		Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;

		long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

		return Stream.iterate(startDate, date -> date.plusDays(1)).limit(daysBetween + 1).filter(isWeekend.negate()).count();
	}

	public int getZileLucratoareInLunaAnul(int month, int year) {
		List<LocalDate> sarbatori = sarbatoriService.getZileSarbatoareInLunaAnul(month, year);

		int weekdayNr;
		LocalDate day;
		int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
		int workingDays = 0;
		for (int i = 1; i <= daysInMonth; ++i) {
			day = LocalDate.of(year, month, i);
			weekdayNr = day.getDayOfWeek().getValue();
			if (weekdayNr != 6 && weekdayNr != 7 && !sarbatori.contains(day))
				workingDays++;
		}
		return workingDays;
	}

	public int getZileSuspendatLucratoare(Contract contract, int luna, int an) {
		if(contract.getSuspendari().isEmpty()) return 0;
		
		List<LocalDate[]> perioadeSuspendare = contract.getPerioadaSuspendat(luna, an);

		if(perioadeSuspendare.isEmpty()) return 0;
		else {
			int zileSuspendare = 0;
			for(LocalDate[] perioada : perioadeSuspendare) {
				zileSuspendare += this.getZileLucratoareInInterval(perioada[0], perioada[1]);
			}
			return zileSuspendare;
		}
	}

	// include sarbatori, nu include suspendate
	public int getNrZileLucratoareContract(Contract contract, int luna, int an) {
		LocalDate dataincepere = contract.getDataincepere();
		LocalDate ultimaZiLucru = contract.getUltimazilucru();

		if (dataincepere == null)
			return 0;
		LocalDate primaZiLunaAn = LocalDate.of(an, luna, 1);

		// daca contractul incepe in luna, an
		if (ultimaZiLucru != null) {
			if (ultimaZiLucru.getMonthValue() == luna && ultimaZiLucru.getYear() == an) {
				// daca contractul incepe si se termina in aceeasi luna
				if (dataincepere.getMonthValue() == luna && dataincepere.getYear() == an) {
					return (int) getZileLucratoareInInterval(dataincepere, ultimaZiLucru);
				}

				return (int) getZileLucratoareInInterval(primaZiLunaAn, ultimaZiLucru);
			}
			else if (ultimaZiLucru.compareTo(primaZiLunaAn.plusMonths(1)) < 0)
				return 0;
		}
		// contractul cuprinde intreaga luna
		if (dataincepere.compareTo(primaZiLunaAn) < 0)
			return getZileLucratoareInLunaAnul(luna, an);
		else {
			LocalDate ultimaZiLuna = LocalDate.of(an, luna, YearMonth.of(an, luna).lengthOfMonth());

			// activitatea nu a inceput incă
			if (dataincepere.compareTo(ultimaZiLuna) > 0)
				return 0;

			// activitatea incepe in luna, an
			else
				return (int) getZileLucratoareInInterval(dataincepere, ultimaZiLuna);
		}
	} // end of 'get zile lucratoare contract'

	public int getZileContract(Contract contract, int luna, int an) {
		return getNrZileLucratoareContract(contract, luna, an) - getZileSuspendatLucratoare(contract, luna, an);
	}
}
