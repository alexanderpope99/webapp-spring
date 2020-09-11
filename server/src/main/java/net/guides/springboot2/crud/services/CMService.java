package net.guides.springboot2.crud.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public int getZileCM(int luna, int an, long idcontract) {
        // find all by idcontract
        List<CM> concediiMedicale = cmRepository.findByIdcontract((int)idcontract);
        if(concediiMedicale.size() == 0)
            return 0;


        LocalDate inceputLuna = LocalDate.of(an, luna, 1);
        int nrZileLuna = inceputLuna.getMonth().length(inceputLuna.isLeapYear());
        // findAllByIdContract
        LocalDate dela, panala;
        LocalDate day;
        int zileCM = 0;
        for(CM concediu : concediiMedicale) {
            dela = concediu.getDela();
            panala = concediu.getPanala();

            for(int i=1; i <= nrZileLuna; ++i) {
                day = LocalDate.of(an, luna, i);
                if(day.compareTo(dela) >= 0 && day.compareTo(panala) <= 0)
                    zileCM++;
            }
        }
        return zileCM;
    }
}
