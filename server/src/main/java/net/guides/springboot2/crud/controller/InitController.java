package net.guides.springboot2.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.services.InitService;

@RestController
@RequestMapping("/init")
public class InitController {

	@Autowired
	private InitService initService;

	@GetMapping
	public String globalInitNoRoles() {
		return initService.init();
	}

	@GetMapping("/roles")
	public String initRoles() {
		return initService.initRoles();
	}
}
