package net.guides.springboot2.crud.controller;

import java.util.HashMap;
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

import net.guides.springboot2.crud.dto.BazaCalculCMDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Bazacalcul;
import net.guides.springboot2.crud.repository.BazacalculRepository;
import net.guides.springboot2.crud.services.BazacalculService;

@RestController
@RequestMapping("/bazacalcul")
public class BazacalculController {
	@Autowired
	private BazacalculRepository bazacalculRepository;

	@Autowired
	private BazacalculService bazaCalculService;

	@GetMapping
	public List<Bazacalcul> getBazacalculsAlphabetically() {
		return bazacalculRepository.findAll();
	}

	@GetMapping("{id}")
	public ResponseEntity<Bazacalcul> getBazacalculById(@PathVariable(value = "id") Long bazacalculId)
			throws ResourceNotFoundException {
		Bazacalcul bazacalcul = bazacalculRepository.findById(bazacalculId)
				.orElseThrow(() -> new ResourceNotFoundException("Bazacalcul not found for this id :: " + bazacalculId));
		return ResponseEntity.ok().body(bazacalcul);
	}

	@GetMapping("ida={ida}")
	public List<Bazacalcul> getBazacalculByIdAndLunaAndAn(@PathVariable("ida") Long ida) {
		return bazacalculRepository.findByIdangajatOrderByAnDescLunaDesc(ida);
	}

	@GetMapping("cm/{ida}/mo={luna}&y={an}")
	public BazaCalculCMDTO getBazaCalculCM(
		@PathVariable("ida") long ida,
		@PathVariable("luna") int luna,
		@PathVariable("an") int an) {
			return bazaCalculService.getBazaCalculCM(luna, an, ida);
		}

	@GetMapping("ida={ida}/mo={luna}&y={an}")
	public Bazacalcul getBazacalculByIdAndLunaAndAn(
		@PathVariable("ida") Long ida,
		@PathVariable("luna") int luna,
		@PathVariable("an") int an) {
		return bazacalculRepository.findByLunaAndAnAndIdangajat(luna, an, ida);
	}

	@PostMapping
	public Bazacalcul createBazacalcul(@RequestBody Bazacalcul bazacalcul) {
		return bazacalculRepository.save(bazacalcul);
	}

	@PutMapping("{id}")
	public ResponseEntity<Bazacalcul> updateBazacalcul(@PathVariable(value = "id") Long bazacalculId,
			@RequestBody Bazacalcul bazacalculDetails) throws ResourceNotFoundException {
		Bazacalcul bazacalcul = bazacalculRepository.findById(bazacalculId)
				.orElseThrow(() -> new ResourceNotFoundException("Bazacalcul not found for this id :: " + bazacalculId));

		bazacalculDetails.setId(bazacalcul.getId());
		final Bazacalcul updatedBazacalcul = bazacalculRepository.save(bazacalculDetails);
		return ResponseEntity.ok(updatedBazacalcul);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteBazacalcul(@PathVariable(value = "id") Long bazacalculId)
			throws ResourceNotFoundException {
		Bazacalcul bazacalcul = bazacalculRepository.findById(bazacalculId)
				.orElseThrow(() -> new ResourceNotFoundException("Bazacalcul not found for this id :: " + bazacalculId));

		bazacalculRepository.delete(bazacalcul);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
