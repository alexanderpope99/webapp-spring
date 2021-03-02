package net.guides.springboot2.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.dto.LuniCuSalarii;
import net.guides.springboot2.crud.dto.RRDetails;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.payload.response.MessageResponse;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.services.RealizariRetineriService;

@RestController
@RequestMapping("/realizariretineri")
public class RealizariRetineriController {
	@Autowired
	private RealizariRetineriService realizariRetineriService;
	@Autowired
	private AngajatRepository angajatRepository;

	@GetMapping("get/idc={id}&mo={luna}&y={an}")
	public RealizariRetineri getRealizariRetineriByIdcontract(@PathVariable("id") int idcontract,
			@PathVariable("luna") int luna, @PathVariable("an") int an)
			throws ResourceNotFoundException {
		return realizariRetineriService.getRealizariRetineri(luna, an, idcontract);
	}

	@GetMapping("/luni-ani/ids={ids}&idu={idu}")
	public List<LuniCuSalarii> getAllRealizariRetineriByIdSocAndIdUser(@PathVariable("ids") int idsocietate,
			@PathVariable("idu") int iduser) throws ResourceNotFoundException {
		return realizariRetineriService.getAllRealizariRetineriByIdSocietateAndIdUser(idsocietate, iduser);
	}

	@GetMapping("idp={id}&mo={luna}&y={an}")
	public RealizariRetineri getRealizariRetineriByIdpersoana(@PathVariable("id") int idpersoana,
			@PathVariable("luna") Integer luna, @PathVariable("an") Integer an)
			throws ResourceNotFoundException {
		// get contract of persoana
		int idcontract = angajatRepository.findIdcontractByIdpersoana(idpersoana);
		return realizariRetineriService.getRealizariRetineri(luna, an, idcontract);

	}

	// * CALCULEAZA pt un angajat pe o luna
	@PostMapping("save/idc={id}&mo={luna}&y={an}")
	public RealizariRetineri saveRealizariRetineri(@PathVariable("id") int idcontract,
			@PathVariable("luna") Integer luna, @PathVariable("an") Integer an)
			throws ResourceNotFoundException {
		return realizariRetineriService.saveOrGetRealizariRetineri(luna, an, idcontract);
	}

	// * RECALCULEAZA pt un angajat, pe o luna
	// * butonul RealizariRetineri.js : "Recalculează"
	@PutMapping("update/calc")
	public RealizariRetineri recalcRealizariRetineri(@RequestBody RRDetails rrDetails)
			throws ResourceNotFoundException {
		
		return realizariRetineriService.recalcRealizariRetineri(rrDetails);
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
