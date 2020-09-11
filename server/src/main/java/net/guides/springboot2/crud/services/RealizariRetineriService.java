package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealizariRetineriService {
    RealizariRetineriService() {}

    @Autowired
    private TicheteService ticheteService;

    public int getNrTichete(int luna, int an, long idcontract) {
        return ticheteService.getNrTichete(luna, an, idcontract);
    }
}
