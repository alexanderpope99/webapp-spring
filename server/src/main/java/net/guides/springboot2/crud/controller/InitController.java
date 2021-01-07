package net.guides.springboot2.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.services.DeduceriService;
import net.guides.springboot2.crud.services.ParametriiSalariuService;
import net.guides.springboot2.crud.services.RoleService;
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

	@Autowired
	private RoleService roleService;

	@GetMapping
	public String globalInitNoRoles() {
		deduceriService.init();
		parametriiSalariuService.init();
		sarbatoriService.initializeKnown();

		return "Initialized: deduceri, parametrii-salariu, sarbatori (2019-2021, fara paște)";
	}

	@GetMapping("/roles")
	public String initRoles() {
		roleService.init();

		return "Initialized roles: admin: 1, director: 2, contabil: 3, angajat: 4";
	}
}
