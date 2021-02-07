package net.guides.springboot2.crud.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.services.FisaIndividualaService;

@RestController
@RequestMapping("/fisa-individuala")
public class FisaIndividualaController {
	@Autowired
	private FisaIndividualaService adeverintaService;

	@GetMapping("{ida}/m1={m1}&m2={m2}&y={an}/{uid}")
	public Boolean create(@PathVariable("m1") int lunaDela, @PathVariable("m2") int lunaPanala, @PathVariable("an") int an, @PathVariable("ida") int idangajat, @PathVariable("uid") int userID) throws ResourceNotFoundException, IOException {
		return adeverintaService.create(lunaDela, lunaPanala, an, idangajat, userID);
	}
}
