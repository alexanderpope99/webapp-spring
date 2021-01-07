package net.guides.springboot2.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.services.DeduceriService;
import net.guides.springboot2.crud.services.ParametriiSalariuService;
import net.guides.springboot2.crud.services.SarbatoriService;

@RestController
@RequestMapping("/init")
public class InitController {
	@Autowired
	private DeduceriService deduceriService;

	@Autowired
	private ParametriiSalariuService parametriiSalariuService;

	@Autowired
	private SarbatoriService sarbatoriService;

	@GetMapping
	public void globalInit() {
		deduceriService.init();
		parametriiSalariuService.init();
		sarbatoriService.initializeKnown();
	}
}
