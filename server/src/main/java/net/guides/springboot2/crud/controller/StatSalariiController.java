package net.guides.springboot2.crud.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.services.StatSalariiService;

@RestController
@RequestMapping("stat")
public class StatSalariiController {
	@Autowired
	private StatSalariiService statSalariiService;

	@GetMapping("/{ids}/mo={luna}&y={an}&i={i}/{uid}")
	public boolean createStatSalarii(
		@PathVariable("ids") int ids,
		@PathVariable("luna") int luna,
		@PathVariable("an") int an,
		@PathVariable("i") String i, // intocmit de
		@PathVariable("uid") long uid
	) throws IOException, ResourceNotFoundException {
		return statSalariiService.createStatSalarii(luna, an, ids, i, uid);
	}

	@GetMapping("/{ids}/individual/ida={ida}&mo={luna}&y={an}/{uid}")
	public boolean createStatIndividual(
		@PathVariable("ids") int ids,
		@PathVariable("ida") long ida,
		@PathVariable("luna") int luna,
		@PathVariable("an") int an,
		@PathVariable("uid") long uid
	) throws IOException, ResourceNotFoundException {
		return statSalariiService.createStatIndividual(luna, an, ida, ids, uid);
	}
}
