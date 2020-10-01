package net.guides.springboot2.crud.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.services.NotaContabilaService;

@RestController
@RequestMapping("notacontabila")
public class NotaContabilaController {
	@Autowired
	private NotaContabilaService fotaContabilaService;

	@GetMapping("/{ids}/mo={luna}&y={an}/{uid}")
	public boolean createNotaContabila(
		@PathVariable("ids") int ids,
		@PathVariable("luna") int luna,
		@PathVariable("an") int an,
		@PathVariable("uid") long uid
	) throws IOException, ResourceNotFoundException {
		return fotaContabilaService.createNotaContabila(luna, an, ids, uid);
	}
}
