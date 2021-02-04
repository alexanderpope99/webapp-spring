package net.guides.springboot2.crud.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import net.guides.springboot2.crud.model.ContBancar;
import net.guides.springboot2.crud.repository.ContBancarRepository;
import net.guides.springboot2.crud.services.ContBancarService;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/contbancar")
public class ContBancarController {
	@Autowired
	private ContBancarRepository contBancarRepository;

	@Autowired
	ContBancarService contBancarService;

	@GetMapping
	public List<ContBancar> getAllContBancars() {
		return contBancarRepository.findAll(Sort.by(Sort.Direction.ASC, "iban"));
	}

	@GetMapping("{id}")
	public ResponseEntity<ContBancar> getContBancarById(@PathVariable("id") int contBancarId)
			throws ResourceNotFoundException {
		ContBancar contBancar = contBancarRepository.findById(contBancarId)
				.orElseThrow(() -> new ResourceNotFoundException("ContBancar not found for this id :: " + contBancarId));
		return ResponseEntity.ok().body(contBancar);
	}

	@GetMapping("ids={ids}")
	public List<ContBancar> getContBancarBySocietate_Id(@PathVariable("ids") int ids) {
		return contBancarRepository.findBySocietate_Id(ids);
	}

	@PostMapping
	public ContBancar createContBancar(@RequestBody ContBancar contBancar) {
		return contBancarRepository.save(contBancar);
	}

	@PostMapping("ids={ids}")
	public ContBancar saveForSocietate(@PathVariable("ids") int idsocietate, @RequestBody ContBancar newCont)
			throws ResourceNotFoundException {
		return contBancarService.save(newCont, idsocietate);
	}

	@PutMapping("{id}")
	public ResponseEntity<ContBancar> updateContBancar(@PathVariable("id") int contBancarId,
			@RequestBody ContBancar contBancarDetails) throws ResourceNotFoundException {
		ContBancar contBancar = contBancarRepository.findById(contBancarId)
				.orElseThrow(() -> new ResourceNotFoundException("ContBancar not found for this id :: " + contBancarId));

		contBancarDetails.setIban(contBancar.getIban());
		final ContBancar updatedContBancar = contBancarRepository.save(contBancar);
		return ResponseEntity.ok(updatedContBancar);
	}

	@PutMapping("{id}/ids={ids}")
	public ContBancar updateForSocietate(
		@PathVariable("id") int idcont,
		@PathVariable("ids") int idsocietate, 
		@RequestBody ContBancar newCont) throws ResourceNotFoundException {
		return contBancarService.update(newCont, idsocietate, idcont);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteContBancar(@PathVariable("id") int contBancarId)
			throws ResourceNotFoundException {
		return contBancarService.delete(contBancarId);
	}
}
