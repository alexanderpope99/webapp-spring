package net.guides.springboot2.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import net.guides.springboot2.crud.dto.NotificareDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Notificare;
import net.guides.springboot2.crud.repository.NotificareRepository;
import net.guides.springboot2.crud.services.NotificareService;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/notificare")
public class NotificareController {
	@Autowired
	private NotificareRepository notificareRepository;

	@Autowired
	private NotificareService notificareService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	public List<NotificareDTO> getAllDTO() {
		List<Notificare> notificari = notificareRepository.findAll(Sort.by(Sort.Direction.ASC, "user"));
		modelMapper.typeMap(Notificare.class, NotificareDTO.class).addMapping(Notificare::getUser,
				NotificareDTO::setIduserObj);
		List<NotificareDTO> notificariDTO = new ArrayList<>();
		for (Notificare n : notificari) {
			notificariDTO.add(modelMapper.map(n, NotificareDTO.class));
		}

		return notificariDTO;
	}

	@GetMapping("/userid/{id}")
	public List<NotificareDTO> getNotificariByUserId(@PathVariable(value = "id") int userId) {
		List<Notificare> notificari = notificareRepository.findByUser_Id(userId);
		modelMapper.typeMap(Notificare.class, NotificareDTO.class).addMapping(Notificare::getUser,
				NotificareDTO::setIduserObj);
		List<NotificareDTO> notificariDTO = new ArrayList<>();
		for (Notificare n : notificari) {
			notificariDTO.add(modelMapper.map(n, NotificareDTO.class));
		}

		return notificariDTO;
	}

	@GetMapping("/userid/{id}/unread")
	public List<NotificareDTO> getUnredNotificariByUserId(@PathVariable(value = "id") int userId) {
		List<Notificare> notificari = notificareRepository.findByUser_Id(userId);
		modelMapper.typeMap(Notificare.class, NotificareDTO.class).addMapping(Notificare::getUser,
				NotificareDTO::setIduserObj);
		List<NotificareDTO> notificariDTO = new ArrayList<>();
		for (Notificare n : notificari) {
			if (!n.isCitit())
				notificariDTO.add(modelMapper.map(n, NotificareDTO.class));
		}

		return notificariDTO;
	}

	@GetMapping("{id}")
	public ResponseEntity<NotificareDTO> getNotificareByIdDTO(@PathVariable(value = "id") int notificareId)
			throws ResourceNotFoundException {
		modelMapper.typeMap(Notificare.class, NotificareDTO.class).addMapping(Notificare::getUser,
				NotificareDTO::setIduserObj);
		Notificare notificare = notificareRepository.findById(notificareId).orElseThrow(
				() -> new ResourceNotFoundException("Notificare not found for this id :: " + notificareId));

		return ResponseEntity.ok().body(modelMapper.map(notificare, NotificareDTO.class));
	}

	@PostMapping
	public Notificare createNotificare(@RequestBody NotificareDTO notificareDTO) throws ResourceNotFoundException {
		return notificareService.save(notificareDTO);
	}

	@PutMapping("{id}")
	public ResponseEntity<Notificare> updateNotificare(@PathVariable(value = "id") int notificareId,
			@RequestBody Notificare notificareDetails) throws ResourceNotFoundException {
		Notificare notificare = notificareRepository.findById(notificareId).orElseThrow(
				() -> new ResourceNotFoundException("Notificare not found for this id :: " + notificareId));

		notificareDetails.setId(notificare.getId());
		final Notificare updatedNotificare = notificareRepository.save(notificareDetails);
		return ResponseEntity.ok(updatedNotificare);
	}

	@GetMapping("{id}/read")
	public Notificare readNotificare(@PathVariable(value = "id") int notificareId) throws ResourceNotFoundException {
		Notificare notificare = notificareRepository.findById(notificareId).orElseThrow(
				() -> new ResourceNotFoundException("Notificare not found for this id :: " + notificareId));
		notificare.setCitit(true);
		notificareRepository.save(notificare);
		return notificare;
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteNotificare(@PathVariable(value = "id") int notificareId)
			throws ResourceNotFoundException {
		Notificare notificare = notificareRepository.findById(notificareId).orElseThrow(
				() -> new ResourceNotFoundException("Notificare not found for this id :: " + notificareId));

		notificareRepository.delete(notificare);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
