package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicheteService {
    TicheteService() {}

    @Autowired
    private ZileService zileService;
    
    public int getNrTichete(int luna, int an, long idcontract) {
        int zileSarbatori = 0;
        int zileLibere = zileService.getZileLibereInLunaAnul(luna, an, idcontract);
        int wd = zileService.getZileLucratoareInLunaAnul(luna, an);
        return wd - zileLibere - zileSarbatori;
    }

    
}  // class
