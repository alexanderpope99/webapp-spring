package net.guides.springboot2.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.services.TicheteService;


@RestController
@RequestMapping("/realizariretineri")
public class RealizariRetineriController {
    @Autowired
    private TicheteService ticheteService;


    @GetMapping("idc={id}&mo={luna}&y={an}")
    public RealizariRetineri getRealizariRetineriByIdcontract(@PathVariable(value = "id") Long idcontract, @PathVariable(value="luna") Integer luna, @PathVariable(value="an") Integer an){
        // tichete = zile_lucratoare - zile_nelucrate
        int nrTichete = ticheteService.getNrTichete(luna, an, idcontract);
        return new RealizariRetineri(nrTichete);
    }
}
    
