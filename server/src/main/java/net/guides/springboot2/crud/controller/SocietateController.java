package net.guides.springboot2.crud.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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

import net.guides.springboot2.crud.dto.SocietateDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.model.User;
import net.guides.springboot2.crud.repository.SocietateRepository;
import net.guides.springboot2.crud.repository.UserRepository;
import net.guides.springboot2.crud.services.CentralizatorService;
import net.guides.springboot2.crud.services.ListaAngajatiService;
import net.guides.springboot2.crud.services.SocietateService;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/societate")
public class SocietateController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private SocietateRepository societateRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SocietateService societateService;

	@Autowired
	private ListaAngajatiService listaAngajatiService;

	@Autowired
	private CentralizatorService centralizatorService;

	@GetMapping
	public List<Societate> getAll() {
		return societateRepository.findAll(Sort.by(Sort.Direction.ASC, "nume"));
	}

	@GetMapping("{id}")
	public ResponseEntity<SocietateDTO> getSocietateById(@PathVariable("id") int id)
			throws ResourceNotFoundException {
		Societate societate = societateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + id));
		SocietateDTO societateDTO = modelMapper.map(societate, SocietateDTO.class);
		if(societate.getImagine()!=null)societateDTO.setIdimagine(societate.getImagine().getId());
			else societateDTO.setIdimagine(null);
		societateDTO.setNrangajati(societate.getAngajati().size());
		return ResponseEntity.ok().body(societateDTO);
	}

	@GetMapping("/user/{id}")
	public List<SocietateDTO> getSocietateByUserId(@PathVariable("id") Integer id) {
		List<Societate> societati = societateRepository.findByUserId(id);
		List<SocietateDTO> societatiDTO = societati.stream().map(soc -> modelMapper.map(soc, SocietateDTO.class))
				.collect(Collectors.toList());
		for (int i = 0; i < societati.size(); ++i) {
			societatiDTO.get(i).setNrangajati(societati.get(i).getAngajati().size());
		}
		return societatiDTO;
	}

	@GetMapping("/raport/listaangajati/{ids}/mo={luna}&y={an}/{uid}")
	public boolean createListaAngajati(@PathVariable("ids") int ids, @PathVariable("luna") int luna, @PathVariable("an") int an, @PathVariable("uid") int uid) throws IOException, ResourceNotFoundException {
		return listaAngajatiService.createListaAngajati(luna, an, ids, uid);
	}

	@GetMapping("/raport/centralizator/{pentru}/{tip}/{ids}/mo={luna}&y={an}/{uid}")
	public boolean createCentralizator(@PathVariable("pentru") int pentru,@PathVariable("tip") int tip,@PathVariable("ids") int ids, @PathVariable("luna") int luna, @PathVariable("an") int an, @PathVariable("uid") int uid) throws IOException, ResourceNotFoundException {
		if(pentru==1 && tip==1)
		return centralizatorService.createCentralizatorVarsta(luna, an, ids, uid);
		else if(pentru==1 && tip==2)
		return centralizatorService.createCentralizatorVarstaComplet(luna, an, ids, uid);
		else if(pentru==2 && tip==1)
		return centralizatorService.createCentralizatorSex(luna, an, ids, uid);
		else if(pentru==2 && tip==2)
		return centralizatorService.createCentralizatorSexComplet(luna, an, ids, uid);
		else if(pentru==3 && tip==1)
		return centralizatorService.createCentralizatorVechime(luna, an, ids, uid);
		else if(pentru==3 && tip==2)
		return centralizatorService.createCentralizatorVechimeComplet(luna, an, ids, uid);
		return false;

	}


	@PostMapping
	public Societate createSocietate(@RequestBody Societate societate) throws ResourceNotFoundException {
		societate.checkData();
		return societateRepository.save(societate);
	}

	@PostMapping("/{uid}")
	public Societate createSocietate(@RequestBody Societate societate, @PathVariable("uid") int uid)
			throws ResourceNotFoundException {
		societate.checkData();
		Societate newSoc = societateRepository.save(societate);
		User user = userRepository.findById(uid).orElseThrow(() -> new RuntimeException("Error"));
		user.addSocietate(newSoc);
		userRepository.save(user);

		return newSoc;
	}

	@PostMapping("/{uid}/imageid={id}")
	public Societate createSocietateWithImagine(@RequestBody Societate societate, @PathVariable("uid") int uid, @PathVariable("id") int id)
			throws ResourceNotFoundException {
		return societateService.postWithImagine(societate, uid, id);
	}

	@PutMapping("{id}")
	public ResponseEntity<Societate> updateSocietate(@PathVariable("id") int id,
			@RequestBody Societate newSocietate) throws ResourceNotFoundException {
		newSocietate.checkData();
		newSocietate.setId(id);

		final Societate updatedSocietate = societateRepository.save(newSocietate);

		return ResponseEntity.ok().body(updatedSocietate);
	}

	@PutMapping("{id}/imageid={iid}")
	public Societate putSocietateWithImagine(@PathVariable("id") int id,
			@RequestBody Societate newSocietate,@PathVariable("iid") int iid) throws ResourceNotFoundException {
				return societateService.putWithImagine(id,newSocietate, iid);

	}


	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteSocietate(@PathVariable("id") int id) throws ResourceNotFoundException {
		// Societate societate = societateRepository.findById(id)
		// .orElseThrow(() -> new ResourceNotFoundException("Societate not found for
		// this id :: " + id));

		// societateRepository.delete(societate);
		societateService.delete(id);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
