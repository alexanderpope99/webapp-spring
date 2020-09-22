package net.guides.springboot2.crud.services;

import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZileService {
    ZileService() {}

    @Autowired
    private CMService cmService;
    @Autowired
    private COService coService;

    public int getZileLibereInLunaAnul(int luna, int an, long idcontract) {
        // get zile cm in luna, anul
        int zileCM = cmService.getZileCM(luna, an, idcontract);
        
        // get zile co fara plata in luna, anul
        int zileCO = coService.getZileCONeplatite(luna, an, idcontract);
        
        // get zile sarbatoare in luna, anul
        
        // return sum
        return zileCM + zileCO;
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
