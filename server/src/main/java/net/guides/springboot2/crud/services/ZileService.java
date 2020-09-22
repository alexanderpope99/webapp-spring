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
