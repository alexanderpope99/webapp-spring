package net.guides.springboot2.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.services.RealizariRetineriService;
import net.guides.springboot2.crud.services.TicheteService;


@RestController
@RequestMapping("/realizariretineri")
public class RealizariRetineriController {
    @Autowired
    private RealizariRetineriService realizariRetineriService;


    @GetMapping("idc={id}&mo={luna}&y={an}")
    public RealizariRetineri getRealizariRetineriByIdcontract(@PathVariable(value = "id") Long idcontract, @PathVariable(value="luna") Integer luna, @PathVariable(value="an") Integer an){
        return realizariRetineriService.getRealizariRetineri(luna, an, idcontract);
    }

    @GetMapping("idp={id}&mo={luna}&y={an}")
    public RealizariRetineri getRealizariRetineriByIdpersoana(@PathVariable(value="id") Long idpersoana, @PathVariable(value="luna") Integer luna, @PathVariable(value="an") Integer an) throws ResourceNotFoundException{
      // get contract of persoana
      long idcontract = realizariRetineriService.getIdContractByIdPersoana(idpersoana);
      return realizariRetineriService.getRealizariRetineri(luna, an, idcontract);
    }
}
    
