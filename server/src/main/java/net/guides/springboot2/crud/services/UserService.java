package net.guides.springboot2.crud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.UserDTO;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.model.User;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;
import net.guides.springboot2.crud.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private SocietateRepository societateRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AngajatRepository angajatRepository;

	UserService() {
	}

	public List<UserDTO> findAll() {
		List<User> users = userRepository.findAll();
		modelMapper.typeMap(User.class, UserDTO.class).addMapping(User::getSocietati, UserDTO::setSocietatiClass);
		return users.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
	}

	public UserDTO findById(int id) {
		modelMapper.typeMap(User.class, UserDTO.class).addMapping(User::getSocietati, UserDTO::setSocietatiClass);
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Error: ROLE_ANGAJAT not in database"));
		return modelMapper.map(user, UserDTO.class);
	}

	public List<UserDTO> findByIdsocietate(int idsocietate) {
		modelMapper.typeMap(User.class, UserDTO.class).addMapping(User::getSocietati, UserDTO::setSocietatiClass);
		List<User> users = userRepository.findBySocietati_Id(idsocietate);
		return users.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
	}

	public UserDTO update(int id, UserDTO newUserDTO) {
		User newUser = modelMapper.map(newUserDTO, User.class);

		//* keep angajati connected to user <- on frontend: angajati.user is null
		// page making this request will not change angajat content, but can change the list
		// make new List of angajati with id's from request list
		List<Angajat> newAngajati = new ArrayList<>();
		newUserDTO.getAngajati().forEach(angajatOpt -> {
			// get from db and push to list
			angajatRepository.findById(angajatOpt.getIdpersoana()).ifPresent(newAngajati::add);
		});
		// set newUser with complete Angajati data
		newUser.setAngajati(newAngajati);

		// set societati
		List<Societate> newSocietati = new ArrayList<>();
		newUserDTO.getSocietati().forEach((idsocietate, nume) -> {
			societateRepository.findById(idsocietate).ifPresent(newSocietati::add);
		});
		newUser.setSocietati(newSocietati);

		userRepository.save(newUser);

		return newUserDTO;
	}
}
