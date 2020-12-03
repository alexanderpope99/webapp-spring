package net.guides.springboot2.crud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.UserDTO;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.model.User;
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

	UserService() {}

	public List<UserDTO> findAll() {
		List<User> users = userRepository.findAll();
		return users.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
	}

	public UserDTO findById(int id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: ROLE_ANGAJAT not in database"));
		return modelMapper.map(user, UserDTO.class);
	}

	public List<UserDTO> findByIdsocietate(int idsocietate) {
		List<User> users = userRepository.findBySocietati_Id(idsocietate);
		return users.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
	}

	public UserDTO update(int id, UserDTO newUserDTO) {
		User user = modelMapper.map(newUserDTO, User.class);

		List<Societate> tmpSoc = new ArrayList<>();
		for(Integer idsocietate : newUserDTO.getSocietati().keySet()) {
			tmpSoc.add(societateRepository.findById(idsocietate).orElseThrow(() -> new RuntimeException("Societate not found for id :: " + idsocietate)));
		}
		user.setSocietati(tmpSoc);
		return newUserDTO;
	}
}
