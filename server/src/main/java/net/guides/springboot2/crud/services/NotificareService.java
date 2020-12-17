package net.guides.springboot2.crud.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.NotificareDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Notificare;
import net.guides.springboot2.crud.model.User;
import net.guides.springboot2.crud.repository.NotificareRepository;
import net.guides.springboot2.crud.repository.UserRepository;

@Service
public class NotificareService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NotificareRepository notificareRepository;

	@Autowired
	private ModelMapper modelMapper;

	public Notificare save(NotificareDTO notificareDTO) throws ResourceNotFoundException {
		User user = userRepository.findById(notificareDTO.getIduser()).orElseThrow(
				() -> new ResourceNotFoundException("User not found for this user id :: " + notificareDTO.getIduser()));
		Notificare notificare = new Notificare();
		notificare = modelMapper.map(notificareDTO, Notificare.class);
		notificare.setUser(user);

		notificare = notificareRepository.save(notificare);
		return notificare;
	}
}
