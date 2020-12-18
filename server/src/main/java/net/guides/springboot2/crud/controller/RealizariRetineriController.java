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
	public RealizariRetineri getRealizariRetineriByIdcontract(@PathVariable(value = "id") int idcontract,
			@PathVariable(value = "luna") int luna, @PathVariable(value = "an") int an) {
		return realizariRetineriService.getRealizariRetineri(luna, an, idcontract);
	}

	@GetMapping("idp={id}&mo={luna}&y={an}")
	public RealizariRetineri getRealizariRetineriByIdpersoana(@PathVariable(value = "id") int idpersoana,
			@PathVariable(value = "luna") Integer luna, @PathVariable(value = "an") Integer an) {
		// get contract of persoana
		int idcontract = angajatRepository.findIdcontractByIdpersoana(idpersoana);
		return realizariRetineriService.getRealizariRetineri(luna, an, idcontract);

	}

	// just a calculator
	@GetMapping("calc/idc={id}&mo={luna}&y={an}&pb={pb}&nrt={nrt}&tos={tos}")
	public RealizariRetineriDTO calcRealizariRetineri(@PathVariable(value = "id") int idcontract,
			@PathVariable(value = "luna") Integer luna, @PathVariable(value = "an") Integer an,
			@PathVariable(value = "pb") Integer primabruta, @PathVariable(value = "nrt") Integer nrTichete,
			@PathVariable(value = "tos") Integer totalOreSuplimentare) throws ResourceNotFoundException {
		return modelMapper.map(realizariRetineriService.calcRealizariRetineri(idcontract, luna, an, primabruta, nrTichete,
				totalOreSuplimentare), RealizariRetineriDTO.class);
	}

	// classic PUT
	@PutMapping("update/idc={idc}&mo={luna}&y={an}")
	public RealizariRetineri updateRealizariRetineri(@PathVariable(name = "luna") int luna,
			@PathVariable(name = "an") int an, @PathVariable(name = "idc") int idcontract,
			@RequestBody RealizariRetineri newRealizariRetineri) {

		RealizariRetineri oldRealizariRetineri = realizariRetineriRepository.findByLunaAndAnAndContract_Id(luna, an,
				idcontract);
		newRealizariRetineri.setId(oldRealizariRetineri.getId());

		final RealizariRetineri updatedRR = realizariRetineriRepository.save(newRealizariRetineri);

		bazacalculService.updateBazacalcul(updatedRR);

		return updatedRR;
	}

	// * CALCULEAZA pt un angajat pe o luna
	@PostMapping("save/idc={id}&mo={luna}&y={an}")
	public RealizariRetineri saveRealizariRetineri(@PathVariable(value = "id") int idcontract,
			@PathVariable(value = "luna") Integer luna, @PathVariable(value = "an") Integer an)
			throws ResourceNotFoundException {
		return realizariRetineriService.saveOrGetRealizariRetineri(luna, an, idcontract);
	}

	// * RECALCULEAZA pt un angajat, pe o luna
	// * butonul RealizariRetineri.js : "Recalculează"
	@PutMapping("update/calc/idc={id}&mo={luna}&y={an}&pb={pb}&nrt={nrt}&tos={tos}")
	public RealizariRetineri recalcRealizariRetineri(@PathVariable("id") int idcontract,
			@PathVariable("luna") Integer luna, @PathVariable("an") Integer an, @PathVariable("pb") Integer primaBruta,
			@PathVariable("nrt") Integer nrTichete, @PathVariable(value = "tos") Integer totalOreSuplimentare)
			throws ResourceNotFoundException {

		return realizariRetineriService.recalcRealizariRetineri(luna, an, idcontract, primaBruta, nrTichete,
				totalOreSuplimentare);
		// RealizariRetineri oldRealizariRetineri =
		// realizariRetineriRepository.findByLunaAndAnAndContract_Id(luna, an,
		// idcontract);

		// RealizariRetineri newRealizariRetineri =
		// realizariRetineriService.calcRealizariRetineri(idcontract, luna, an,
		// primaBruta, nrTichete, totalOreSuplimentare);
		// newRealizariRetineri.setId(oldRealizariRetineri.getId());

		// bazacalculService.updateBazacalcul(newRealizariRetineri);

		// return realizariRetineriRepository.save(newRealizariRetineri);
	}

	// * CALCULEAZA pt un angajat, pe ultimele 6 luni, exclusiv (luna, an)
	// * butonul RealizariRetineri.js : "Recalculeaza ultimele 6 luni"
	@PutMapping("calc/ultimele6/idc={idc}&mo={luna}&y={an}")
	public ResponseEntity<MessageResponse> calcUltimele6Luni(@PathVariable("idc") int idcontract,
			@PathVariable("luna") int luna, @PathVariable("an") int an) throws ResourceNotFoundException {

		realizariRetineriService.saveRealizariRetineriUltimele6Luni(luna, an, idcontract);

		return ResponseEntity.ok(new MessageResponse("Ultimele 6 luni calculate!"));
	}

	// * RECALCULEAZA pt un angajat, pe ultimele 6 luni, exclusiv (luna, an)
	// * butonul RealizariRetineri.js : "Recalculeaza ultimele 6 luni"
	@PutMapping("recalc/ultimele6/idc={idc}&mo={luna}&y={an}")
	public ResponseEntity<MessageResponse> recalcUltimele6Luni(@PathVariable("idc") int idcontract,
			@PathVariable("luna") int luna, @PathVariable("an") int an) throws ResourceNotFoundException {

		realizariRetineriService.recalcRealizariRetineriUltimele6Luni(luna, an, idcontract);

		return ResponseEntity.ok(new MessageResponse("Ultimele 6 luni recalculate!"));
	}

	// * recalculeaza pt o societate, pe o luna
	// * butonul Societati.js : "Recalculeaza toate salariile"
	@PutMapping("recalc/societate/ids={ids}&mo={luna}&y={an}")
	public ResponseEntity<MessageResponse> recalcSocietate(@PathVariable("ids") int idsocietate,
			@PathVariable("luna") int luna, @PathVariable("an") int an) throws ResourceNotFoundException {

		realizariRetineriService.recalcSocietate(luna, an, idsocietate);

		return ResponseEntity.ok(new MessageResponse("Salarii recalculate pentru societatea " + idsocietate));
	}

	// * recalculeaza pt o societate, pe ultimele 6 luni, exclusiv (luna, an)
	// * butonul Societati.js : "Recalculeaza toate salariile ultimele 6 luni"
	@PutMapping("recalc/societate/ultimele6/ids={ids}&mo={luna}&y={an}")
	public ResponseEntity<MessageResponse> recalcSocietateUltimele6Luni(@PathVariable("ids") int idsocietate,
			@PathVariable("luna") int luna, @PathVariable("an") int an) throws ResourceNotFoundException {

		realizariRetineriService.recalcSocietateUltimele6Luni(luna, an, idsocietate);

		return ResponseEntity
				.ok(new MessageResponse("Salarii recalculate in ultimele 6 luni, pentru societatea " + idsocietate));
	}
}
