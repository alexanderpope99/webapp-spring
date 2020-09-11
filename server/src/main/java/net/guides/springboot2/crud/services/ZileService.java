package net.guides.springboot2.crud.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZileService {
    ZileService() {}

    @Autowired
    private CMService cmService;

    public int getZileLibereInLunaAnul(int luna, int an, long idcontract) {
        // get zile cm in luna, anul
        int zileCM = cmService.getZileCM(luna, an, idcontract);
        return zileCM;
        // get zile co in luna, anul
        
        // get zile sarbatoare in luna, anul

        // return zileCM + zileCO;
    }

    public long getZileLucratoareInterval(LocalDate startDate, LocalDate endDate,
    Optional<List<LocalDate>> holidays) 
    {
        if (startDate == null || endDate == null || holidays == null) {
            throw new IllegalArgumentException("Invalid method argument(s) to countBusinessDaysBetween(" + startDate
                    + "," + endDate + "," + holidays + ")");
        }

        Predicate<LocalDate> isHoliday = date -> holidays.isPresent() ? holidays.get().contains(date) : false;

        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        long businessDays = Stream.iterate(startDate, date -> date.plusDays(1)).limit(daysBetween)
                .filter(isHoliday.or(isWeekend).negate()).count();
        return businessDays;
    }

    public int getZileLucratoareInLunaAnul(int month, int year) {
        int weekdayNr;
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        int workingDays = 0;
        for(int i=1; i <= daysInMonth; ++i) {
            weekdayNr = LocalDate.of(year, month, i).getDayOfWeek().getValue();
            if(weekdayNr != 6 && weekdayNr != 7)
                workingDays++;
        }
        return workingDays;
    }
}
