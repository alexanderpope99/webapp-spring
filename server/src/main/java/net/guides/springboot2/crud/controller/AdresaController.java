package net.guides.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Adresa;
import net.guides.springboot2.crud.repository.AdresaRepository;
import net.guides.springboot2.crud.repository.PersoanaRepository;

@RestController
@RequestMapping("/adresa")
public class AdresaController {
	@Autowired
	private AdresaRepository adresaRepository;
	@Autowired
	private PersoanaRepository persoanaRepository;

	@GetMapping
	public List<Adresa> getAll() {
		return adresaRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("{id}")
	public ResponseEntity<Adresa> getAdresaById(@PathVariable(value = "id") int id) throws ResourceNotFoundException {
		Adresa adresa = adresaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Adresa not found for this id :: " + id));

		return ResponseEntity.ok().body(adresa);
	}

	@GetMapping("idp={idp}")
	public Adresa getAddressByIdpersoana(@PathVariable("idp") int idp) {
		Adresa adresa = persoanaRepository.findById(idp).get().getAdresa();
		return adresa;
	}

	@PostMapping
	public Adresa createAdresaDTO(@RequestBody Adresa adresa) {
		return adresaRepository.save(adresa);
	}

	@PutMapping("{id}")
	public ResponseEntity<Adresa> updateAdresa(@PathVariable(value = "id") int id, @RequestBody Adresa newAdresa)
			throws ResourceNotFoundException {
		Adresa adresa = adresaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Adresa not found for this id :: " + id));

		newAdresa.setId(adresa.getId());
		final Adresa updatedAdresa = adresaRepository.save(newAdresa);
		return ResponseEntity.ok(updatedAdresa);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteAdresa(@PathVariable(value = "id") int id) throws ResourceNotFoundException {
		Adresa adresa = adresaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Adresa not found for this id :: " + id));

		adresaRepository.delete(adresa);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
