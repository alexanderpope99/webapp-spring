package net.guides.springboot2.crud.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.model.CO;
import net.guides.springboot2.crud.repository.CORepository;

@Service
public class COService {
    COService() {}

    @Autowired
    private CORepository coRepository;

    public int getZileCONeplatite(int luna, int an, long idcontract) {
        List<CO> concediiOdihnaNeplatite = coRepository.findByIdcontractAndTip(idcontract, "Concediu fără plată");
        if(concediiOdihnaNeplatite.size() == 0)
            return 0;

        return zileC(luna, an, concediiOdihnaNeplatite);
    }

    public int getZileCO(int luna, int an, long idcontract) {
        List<CO> concediiOdihna = coRepository.findByIdcontract(idcontract);
        if(concediiOdihna.size() == 0)
            return 0;

        return zileC(luna, an, concediiOdihna);
    }

    
    private int zileC(int luna, int an, List<CO> concedii) {
        LocalDate inceputLuna = LocalDate.of(an, luna, 1);
        int nrZileLuna = inceputLuna.getMonth().length(inceputLuna.isLeapYear());
        // findAllByIdContract
        LocalDate dela, panala;
        LocalDate day;
        int zileC = 0;
        for(CO concediu : concedii) {
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

} // class
