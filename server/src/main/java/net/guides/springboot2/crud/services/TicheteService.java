package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import net.guides.springboot2.crud.repository.TicheteRepository;

@Service
public class TicheteService {
    TicheteService() {}

    @Autowired
    private ZileService zileService;
    @Autowired
    private CMService cmService;
    @Autowired
    private COService coService;
    
    public int getNrTichete(int luna, int an, long idcontract) {
        int zileSarbatori = 0;
        int zileCMLucratoare = cmService.getZileCMLucratoare(luna, an, idcontract);
        int zileCOLucratoare = coService.getZileCOLucratoare(luna, an, idcontract);

        int wd = zileService.getZileLucratoareInLunaAnul(luna, an);
        return wd - zileCMLucratoare - zileCOLucratoare - zileSarbatori;
    }

    
}  // class
