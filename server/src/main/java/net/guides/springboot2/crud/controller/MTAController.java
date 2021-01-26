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

	@GetMapping("/{ids}&mo={luna}&y={an}/{uid}/{cbid}/r")
	public boolean createMTA_Raiffeisen(
		@PathVariable ("ids") int idsocietate, 
		@PathVariable ("luna") int luna, 
		@PathVariable ("an") int an, 
		@PathVariable("uid") int userID,
		@PathVariable("cbid") int idContBancar) throws ResourceNotFoundException, IOException {
		return mtaService.createMTA_Raiffeisen(idsocietate, luna, an, userID, idContBancar);
	}

	@GetMapping("/{ids}&mo={luna}&y={an}/{uid}/{cbid}/u")
	public boolean createMTA_Unicredit(
		@PathVariable ("ids") int idsocietate, 
		@PathVariable ("luna") int luna, 
		@PathVariable ("an") int an, 
		@PathVariable("uid") int userID,
		@PathVariable("cbid") int idContBancar) throws ResourceNotFoundException, IOException {
		return mtaService.createMTA_Unicredit(idsocietate, luna, an, userID, idContBancar);
	}
}
