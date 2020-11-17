package net.guides.springboot2.crud.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.services.MTAService;

@RestController
@RequestMapping("mta")
public class MTAController {
	@Autowired
	private MTAService mtaService;

	@GetMapping("/{ids}&mo={luna}&y={an}/{uid}")
	public boolean createMTA(
		@PathVariable ("ids") int idsocietate, 
		@PathVariable ("luna") int luna, 
		@PathVariable ("an") int an, 
		@PathVariable("uid") int userID) throws ResourceNotFoundException, IOException {
		return mtaService.createMTA(idsocietate, luna, an, userID);
	}
}
