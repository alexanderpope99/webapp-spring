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

	@GetMapping("/{ids}/mo={luna}&y={an}")
	public boolean createStatSalarii(
		@PathVariable("ids") int ids,
		@PathVariable("luna") int luna,
		@PathVariable("an") int an
	) throws IOException, ResourceNotFoundException {
		return statSalariiService.createStatSalarii(luna, an, ids);
	}
}
