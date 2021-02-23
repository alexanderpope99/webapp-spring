package net.guides.springboot2.crud.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import net.guides.springboot2.crud.services.FisierService;
import net.guides.springboot2.crud.messages.ResponseMessage;
import net.guides.springboot2.crud.model.Fisier;
import net.guides.springboot2.crud.repository.FisierRepository;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.messages.ResponseFile;

@Controller
@RequestMapping("/fisier")
public class FisierController {

	@Autowired
	private FisierService fisierService;

	@Autowired
	private FisierRepository fisierRepository;

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			Fisier fisier = fisierService.store(file);

			message = "Fișier încărcat cu succes: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, fisier.getId()));
		} catch (Exception e) {
			message = "Nu am putut încărca fișierul: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message, null));
		}
	}

	@GetMapping
	public ResponseEntity<List<ResponseFile>> getListFiles() {
		List<ResponseFile> files = fisierService.getAllFiles().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/fisier/").path(String.valueOf(dbFile.getId())).toUriString();

			return new ResponseFile(dbFile.getNume(), fileDownloadUri, dbFile.getType(), dbFile.getData().length);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseFile> getFile(@PathVariable int id) {
		Fisier fisier = fisierService.getFile(id);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/fisier/").path(String.valueOf(fisier.getId())).toUriString();
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseFile(fisier.getNume(), fileDownloadUri, fisier.getType(), fisier.getData().length));
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable int id) {
		Fisier fisier = fisierService.getFile(id);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fisier.getNume() + "\"").body(fisier.getData());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Fisier> updateFile(@PathVariable int id, @RequestParam("file") MultipartFile file) throws ResourceNotFoundException, IOException {
	Fisier fisier = fisierRepository.findById(id)
	.orElseThrow(() -> new ResourceNotFoundException("Fisier not found for this id :: " + id));
	String fileName = StringUtils.cleanPath(file.getOriginalFilename());

	fisier.setNume(fileName);
	fisier.setData(file.getBytes());
	fisier.setType(file.getContentType());
	fisierRepository.save(fisier);
	return ResponseEntity.ok(fisier);
  }
}
