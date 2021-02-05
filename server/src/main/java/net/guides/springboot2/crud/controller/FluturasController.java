package net.guides.springboot2.crud.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.services.FluturasSalariuService;

@RestController
@RequestMapping("/fluturas")
public class FluturasController {
	@Autowired
	private FluturasSalariuService adeverintaService;

	@GetMapping("{ida}/m={m}&y={an}/{uid}")
	public Boolean createFluturas(@PathVariable("m") int luna, @PathVariable("an") int an, @PathVariable("ida") int idangajat, @PathVariable("uid") int userID) throws ResourceNotFoundException, IOException {
		return adeverintaService.createFluturas(luna, an, idangajat, userID);
	}
}
