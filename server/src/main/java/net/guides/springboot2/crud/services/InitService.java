package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitService {

	@Autowired
	private DeduceriService deduceriService;

	@Autowired
	private ParametriiSalariuService parametriiSalariuService;

	@Autowired
	private SarbatoriService sarbatoriService;

	@Autowired
	private RoleService roleService;
	
	public String init() {
		deduceriService.init();
		parametriiSalariuService.init();
		sarbatoriService.initializeKnown();

		return "Initialized: deduceri, parametrii-salariu, sarbatori (2019-2021, cu paște)";
	}

	public String initRoles() {
		roleService.init();

		return "Initialized roles: admin: 1, director: 2, contabil: 3, angajat: 4";
	}
}
