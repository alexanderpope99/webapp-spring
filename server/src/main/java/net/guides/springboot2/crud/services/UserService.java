package net.guides.springboot2.crud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.UserDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.ERole;
import net.guides.springboot2.crud.model.Role;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.model.User;
import net.guides.springboot2.crud.payload.request.SignupRequest;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.RoleRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;
import net.guides.springboot2.crud.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	private SocietateRepository societateRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AngajatRepository angajatRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private InitService initService;

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
		List<User> users = userRepository.findBySocietati_IdOrderByUsernameAsc(idsocietate);

		modelMapper.typeMap(User.class, UserDTO.class).addMapping(User::getSocietati, UserDTO::setSocietatiClass);
		return users.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
	}

	public void createNewUser(SignupRequest signUpRequest) {
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));
		user.setGen(signUpRequest.isGen());

		List<Role> roles = new ArrayList<>();

		Role angajatRole = roleRepository.findByName(ERole.ROLE_ANGAJAT)
				.orElseThrow(() -> new RuntimeException("Error: ROLE_ANGAJAT not in database"));
		roles.add(angajatRole);

		user.setRoles(roles);
		userRepository.save(user);
	}

	public UserDTO save(UserDTO newUserDTO) throws ResourceNotFoundException {
		if (userRepository.existsByUsername(newUserDTO.getUsername())) {
			throw new ResourceNotFoundException("Error: Username is already in use!");
		}

		if (userRepository.existsByEmail(newUserDTO.getEmail())) {
			throw new ResourceNotFoundException("Error: Email is already in use!");
		}

		// convert from dto to model :: sets Roles
		User newUser = modelMapper.map(newUserDTO, User.class);

		// set basic password cripted from "parola" :: this needs to be changed by user
		newUser.setPassword(encoder.encode("parola"));

		// set angajat
		List<Angajat> newAngajati = new ArrayList<>();
		newUserDTO.getAngajati().forEach(angajatOpt -> {
			// get from db and push to list
			Angajat tmpAngajat = angajatRepository.findById(angajatOpt.getIdpersoana()).get();

			// angajat needs to point to this user
			tmpAngajat.setUser(newUser);

			newAngajati.add(tmpAngajat);
		});
		// set newUser with complete Angajati list
		newUser.setAngajati(newAngajati);

		// convert from UserDTO.SocietateJSON to User.Societate
		List<Societate> newSocietati = new ArrayList<>();
		newUserDTO.getSocietati()
				.forEach(societate -> societateRepository.findById(societate.getId()).ifPresent(newSocietati::add));
		newUser.setSocietati(newSocietati);

		int newUserId = userRepository.save(newUser).getId();
		newUserDTO.setId(newUserId);
		return newUserDTO;
	} // save

	public UserDTO update(UserDTO newUserDTO, int idsocietate) throws ResourceNotFoundException {
		// unassign angajat.user
		User oldUser = userRepository.findById(newUserDTO.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + newUserDTO.getId()));

		// keep old password :: newUserDTO doesn't have password
		User newUser = modelMapper.map(newUserDTO, User.class);
		newUser.setPassword(oldUser.getPassword());

		// remove angajat from current societate
		oldUser.getAngajati().forEach(angajat -> {
			if (angajat.getSocietate().getId() == idsocietate) {
				angajat.setUser(null);
				angajatRepository.save(angajat);
			}
		});

		// * keep angajati connected to user :: on frontend, angajati.user is null
		// page making this request will not change angajat content
		// make new List of angajati with id's from request list
		List<Angajat> newAngajati = new ArrayList<>();
		newUserDTO.getAngajati().forEach(angajatOpt -> {
			// get from db and push to list
			Angajat tmpAngajat = angajatRepository.findById(angajatOpt.getIdpersoana()).get();

			// angajat needs to point to this user
			tmpAngajat.setUser(newUser);

			newAngajati.add(tmpAngajat);
		});
		// set newUser with complete Angajati list
		newUser.setAngajati(newAngajati);

		// set societati
		List<Societate> newSocietati = new ArrayList<>();
		newUserDTO.getSocietati()
				.forEach(societate -> societateRepository.findById(societate.getId()).ifPresent(newSocietati::add));
		newUser.setSocietati(newSocietati);

		// save to db
		userRepository.save(newUser);

		return newUserDTO;
	} // update

	public void delete(int userId) throws ResourceNotFoundException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

		user.getAngajati().forEach(angajat -> angajat.setUser(null));

		userRepository.delete(user);
	}

	public void init(SignupRequest signUpRequest) {
		if (userRepository.count() > 0)
			return;

		// init database
		initService.init();

		// Create new user with roles admin, director, contabil
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));
		user.setGen(signUpRequest.isGen());

		List<Role> roles = new ArrayList<>();

		Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
				.orElseThrow(() -> new RuntimeException("Error: ROLE_ADMIN not in database"));
		Role directorRole = roleRepository.findByName(ERole.ROLE_DIRECTOR)
				.orElseThrow(() -> new RuntimeException("Error: ROLE_DIRECTOR not in database"));
		Role contabilRole = roleRepository.findByName(ERole.ROLE_CONTABIL)
				.orElseThrow(() -> new RuntimeException("Error: ROLE_CONTABIL not in database"));

		roles.add(adminRole);
		roles.add(directorRole);
		roles.add(contabilRole);

		user.setRoles(roles);
		userRepository.save(user);
	}
}
