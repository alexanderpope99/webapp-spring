package net.guides.springboot2.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.services.ApplicationService;

@RestController
@RequestMapping("/application")
public class ApplicationController {
  
  @Autowired
  private ApplicationService appService;

  @GetMapping("/{name}")
  public Object getBeanByName(@PathVariable("name") String name) {
    return appService.getBean(name);
  }
}
