package net.guides.springboot2.crud.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.model.CM;
import net.guides.springboot2.crud.repository.CMRepository;

@Service
public class CMService {
    CMService() {}

    @Autowired
    private CMRepository cmRepository;

    // DOAR ZILE C.M.
    public int getZileCMLucratoare(int luna, int an, long idcontract) {
        // find all by idcontract
        List<CM> concediiMedicale = cmRepository.findByIdcontract(idcontract);
        if(concediiMedicale.size() == 0)
            return 0;

        return zileCLucratoare(luna, an, concediiMedicale);
    }

    public int getZileCM(int luna, int an, long idcontract) {
        // find all by idcontract
        List<CM> concediiMedicale = cmRepository.findByIdcontract(idcontract);
        if(concediiMedicale.size() == 0)
            return 0;

        return zileC(luna, an, concediiMedicale);
	}
	
	public int getValCM(int luna, int an, long idcontract) {
		// media zilnica pe 6 luni = venitTotal6luni / nrZileLucrate6luni <- din bazacalcul


		return 0;
	}

	// public int getZileFNUASS(int luna, int an, long idcontract) {
	// 	List<CM> cm = cmRepository.findByIdcontractInLunaAnul(idcontract)
	// }

	private int zileC(int luna, int an, List<CM> concedii) {
        LocalDate inceputLuna = LocalDate.of(an, luna, 1);
        int nrZileLuna = inceputLuna.getMonth().length(inceputLuna.isLeapYear());

        LocalDate dela, panala;
        LocalDate day;
        int zileC = 0;
        for(CM concediu : concedii) {
            dela = concediu.getDela();
            panala = concediu.getPanala();

            for(int i=1; i <= nrZileLuna; ++i) {
                day = LocalDate.of(an, luna, i);
                if(day.compareTo(dela) >= 0 && day.compareTo(panala) <= 0)
                    zileC++;
            }
        }
        return zileC;
    }
    
    private int zileCLucratoare(int luna, int an, List<CM> concedii) {
        LocalDate inceputLuna = LocalDate.of(an, luna, 1);
        int nrZileLuna = inceputLuna.getMonth().length(inceputLuna.isLeapYear());

        LocalDate dela, panala;
        LocalDate day;
        int zileC = 0;
        for(CM concediu : concedii) {
            dela = concediu.getDela();
            panala = concediu.getPanala();

            for(int i=1; i <= nrZileLuna; ++i) {
                day = LocalDate.of(an, luna, i);
                if(day.compareTo(dela) >= 0 && day.compareTo(panala) <= 0)
                    if(day.getDayOfWeek().getValue() != 6 && day.getDayOfWeek().getValue() != 7)
                        zileC++;
            }
        }
        return zileC;
    }

}  // class
