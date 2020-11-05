package net.guides.springboot2.crud.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.dto.RealizariRetineriDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.payload.response.MessageResponse;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.RealizariRetineriRepository;
import net.guides.springboot2.crud.services.BazacalculService;
import net.guides.springboot2.crud.services.RealizariRetineriService;

@RestController
@RequestMapping("/realizariretineri")
public class RealizariRetineriController {
	@Autowired
	private RealizariRetineriService realizariRetineriService;
	@Autowired
	private RealizariRetineriRepository realizariRetineriRepository;
	@Autowired
	private AngajatRepository angajatRepository;

	@Autowired
	private BazacalculService bazacalculService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("idc={id}&mo={luna}&y={an}")
	public RealizariRetineriDTO getRealizariRetineriByIdcontract(@PathVariable(value = "id") int idcontract,
			@PathVariable(value = "luna") int luna, @PathVariable(value = "an") int an) throws ResourceNotFoundException {
		return modelMapper.map(realizariRetineriService.getRealizariRetineri(luna, an, idcontract),
				RealizariRetineriDTO.class);
	}

	@GetMapping("idp={id}&mo={luna}&y={an}")
	public RealizariRetineriDTO getRealizariRetineriByIdpersoana(@PathVariable(value = "id") int idpersoana,
			@PathVariable(value = "luna") Integer luna, @PathVariable(value = "an") Integer an)
			throws ResourceNotFoundException {
		// get contract of persoana
		int idcontract = angajatRepository.findIdcontractByIdpersoana(idpersoana);
		return modelMapper.map(realizariRetineriService.getRealizariRetineri(luna, an, idcontract),
				RealizariRetineriDTO.class);
	}

	// just a calculator
	@GetMapping("calc/idc={id}&mo={luna}&y={an}&pb={pb}&nrt={nrt}&tos={tos}")
	public RealizariRetineriDTO calcRealizariRetineri(@PathVariable(value = "id") int idcontract,
			@PathVariable(value = "luna") Integer luna, @PathVariable(value = "an") Integer an,
			@PathVariable(value = "pb") Integer primabruta, @PathVariable(value = "nrt") Integer nrTichete,
			@PathVariable(value = "tos") Integer totalOreSuplimentare) throws ResourceNotFoundException {
		return modelMapper.map(realizariRetineriService.calcRealizariRetineri(idcontract, luna, an, primabruta, nrTichete, totalOreSuplimentare), RealizariRetineriDTO.class);
	}

	@PostMapping("save/idc={id}&mo={luna}&y={an}")
	public RealizariRetineriDTO saveRealizariRetineri(@PathVariable(value = "id") int idcontract,
			@PathVariable(value = "luna") Integer luna, @PathVariable(value = "an") Integer an)
			throws ResourceNotFoundException {
		return modelMapper.map(realizariRetineriService.saveRealizariRetineri(luna, an, idcontract), RealizariRetineriDTO.class);
	}

	@PutMapping("update/idc={idc}&mo={luna}&y={an}")
	public RealizariRetineriDTO updateRealizariRetineri(@PathVariable(name = "luna") int luna,
			@PathVariable(name = "an") int an, @PathVariable(name = "idc") int idcontract,
			@RequestBody RealizariRetineri newRealizariRetineri) throws ResourceNotFoundException {

		RealizariRetineri oldRealizariRetineri = realizariRetineriRepository.findByLunaAndAnAndIdcontract(luna, an,
				idcontract);
		newRealizariRetineri.setId(oldRealizariRetineri.getId());

		final RealizariRetineri updatedRR = realizariRetineriRepository.save(newRealizariRetineri);

		bazacalculService.updateBazacalcul(updatedRR);

		return modelMapper.map(updatedRR, RealizariRetineriDTO.class);
	}

	@PutMapping("update/reset/idc={idc}&mo={luna}&y={an}")
	public RealizariRetineriDTO recalcRealizariRetineri(@PathVariable(name = "luna") int luna,
			@PathVariable(name = "an") int an, @PathVariable(name = "idc") int idcontract) throws ResourceNotFoundException {

		RealizariRetineri oldRealizariRetineri = realizariRetineriRepository.findByLunaAndAnAndIdcontract(luna, an,
				idcontract);

		RealizariRetineri newRealizariRetineri = realizariRetineriService.resetRealizariRetineri(luna, an, idcontract);

		newRealizariRetineri.setId(oldRealizariRetineri.getId());

		RealizariRetineri updatedRR = realizariRetineriRepository.save(newRealizariRetineri);

		bazacalculService.updateBazacalcul(updatedRR);

		return modelMapper.map(updatedRR, RealizariRetineriDTO.class);
	}

	@PutMapping("update/calc/idc={id}&mo={luna}&y={an}&pb={pb}&nrt={nrt}&tos={tos}")
	public RealizariRetineriDTO calcThenUpdateRealizariRetineri(@PathVariable(value = "id") int idcontract,
			@PathVariable(value = "luna") Integer luna, @PathVariable(value = "an") Integer an,
			@PathVariable(value = "pb") Integer primaBruta, @PathVariable(value = "nrt") Integer nrTichete,
			@PathVariable(value = "tos") Integer totalOreSuplimentare) throws ResourceNotFoundException {

		RealizariRetineri oldRealizariRetineri = realizariRetineriRepository.findByLunaAndAnAndIdcontract(luna, an,
				idcontract);
		int idstat = oldRealizariRetineri.getId();

		RealizariRetineri newRealizariRetineri = realizariRetineriService.calcRealizariRetineri(idcontract, luna, an,
				primaBruta, nrTichete, totalOreSuplimentare);
		newRealizariRetineri.setId(idstat);

		bazacalculService.updateBazacalcul(newRealizariRetineri);

		final RealizariRetineri updatedRR = realizariRetineriRepository.save(newRealizariRetineri);
		return modelMapper.map(updatedRR, RealizariRetineriDTO.class);
	}

	@PutMapping("calc/ultimele6/idc={idc}&mo={luna}&y={an}")
	public ResponseEntity<?> calcUltimele6Luni(
		@PathVariable("idc") int idcontract, 
		@PathVariable("luna") int luna,
		@PathVariable("an") int an) throws ResourceNotFoundException {

		realizariRetineriService.saveRealizariRetineriUltimele6Luni(luna, an, idcontract);

		return ResponseEntity.ok(new MessageResponse("Ultimele 6 luni calculate!"));
	}
}
