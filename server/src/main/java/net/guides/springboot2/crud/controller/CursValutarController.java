package net.guides.springboot2.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;

import net.guides.springboot2.crud.services.CursValutarService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cursvalutar")
public class CursValutarController {
	@Autowired
	public CursValutarService cursValutarService;

	@GetMapping
	public String returnCursValutar() {
		return cursValutarService.getCursValutarFromBNR();
	}
}
