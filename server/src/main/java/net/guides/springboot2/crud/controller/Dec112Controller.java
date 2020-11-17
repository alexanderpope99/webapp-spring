package net.guides.springboot2.crud.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.services.Dec112Service;

@RestController
@RequestMapping("dec112")
public class Dec112Controller {
	@Autowired
	private Dec112Service dec112Service;

	@GetMapping("/{ids}/mo={luna}&y={an}&drec={drec}&numeDec={numeDec}&prenumeDec={prenumeDec}&functieDec={functieDec}/{uid}")
	public boolean createDec112(@PathVariable("ids") int ids, @PathVariable("luna") int luna,
			@PathVariable("an") int an, @PathVariable("uid") int uid, @PathVariable("drec") int drec,
			@PathVariable("numeDec") String numeDeclarant, @PathVariable("prenumeDec") String prenumeDeclarant,
			@PathVariable("functieDec") String functieDeclarant) throws IOException, ResourceNotFoundException {
		try {
			return dec112Service.createDec112(luna, an, ids, uid, drec, numeDeclarant, prenumeDeclarant,
					functieDeclarant);
		} catch (ResourceNotFoundException e) {
			throw e;
		}
	}
}