package net.guides.springboot2.crud.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.dto.NotaContabilaDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.repository.RealizariRetineriRepository;
import net.guides.springboot2.crud.services.NotaContabilaService;

@RestController
@RequestMapping("notacontabila")
public class NotaContabilaController {
	@Autowired
	private NotaContabilaService notaContabilaService;
	@Autowired
	private RealizariRetineriRepository realizariRetineriRepository;

	@GetMapping("/{ids}/mo={luna}&y={an}/{uid}")
	public boolean createNotaContabila(@PathVariable("ids") int ids, @PathVariable("luna") int luna,
			@PathVariable("an") int an, @PathVariable("uid") long uid) throws IOException, ResourceNotFoundException {
		return notaContabilaService.createNotaContabila(luna, an, ids, uid);
	}

	@GetMapping("/test/{ids}/mo={luna}&y={an}")
	public NotaContabilaDTO createNotaContabilaWhole(@PathVariable("ids") int ids, @PathVariable("luna") int luna,
			@PathVariable("an") int an) throws IOException, ResourceNotFoundException {
		return realizariRetineriRepository.getNotaContabilaByLunaAndAnAndIdsocietate(luna, an, ids);
	}
}
