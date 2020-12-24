package net.guides.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.dto.PersoanaDTO;
import net.guides.springboot2.crud.dto.RoleDTO;
import net.guides.springboot2.crud.dto.SocietateDTO;
import net.guides.springboot2.crud.dto.UserDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.ERole;
import net.guides.springboot2.crud.model.Role;
import net.guides.springboot2.crud.model.User;
import net.guides.springboot2.crud.repository.RoleRepository;
import net.guides.springboot2.crud.repository.UserRepository;
import net.guides.springboot2.crud.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserService userService;

	@GetMapping
	public List<UserDTO> getAllUsers() {
		return userService.findAll();
	}

	@GetMapping("{id}")
	public UserDTO getById(@PathVariable("id") int id) {
		return userService.findById(id);
	}

	@GetMapping("ids={ids}")
	public List<UserDTO> getByIdsocietate(@PathVariable("ids") int idsocietate) {
		return userService.findByIdsocietate(idsocietate);
	}

	@GetMapping("/roles/{usrid}")
	public List<RoleDTO> getRolesByUserId(@PathVariable("usrid") int usrid) {
		return userRepository.getRolesByUserId(usrid);
	}

	@GetMapping("/societati/{usrid}")
	public List<SocietateDTO> getSocietiesByUserId(@PathVariable("usrid") int usrid) {
		return userRepository.getSocietiesByUserId(usrid);
	}

	@GetMapping("/persoana/{usrid}")
	public List<PersoanaDTO> getPersoanaByUserId(@PathVariable("usrid") int usrid) {
		return userRepository.getPersoanaByUserId(usrid);
	}

	@GetMapping("/societate/{usrid}")
	public List<SocietateDTO> getSocietateByUserId(@PathVariable("usrid") int usrid) {
		return userRepository.getSocietateByUserId(usrid);
	}

	@GetMapping("/superior/{usrid}")
	public List<PersoanaDTO> getSuperiorByUserId(@PathVariable("usrid") int usrid) {
		return userRepository.getSuperiorByUserId(usrid);
	}

	@PostMapping("/roles/{usrid}&{roleid}")
	public List<RoleDTO> getRolesByUserId(@PathVariable("usrid") int usrid, @PathVariable("roleid") int roleid) {
		User user = userRepository.findById(usrid).orElseThrow(() -> new RuntimeException("Error"));
		List<Role> roles = user.getRoles();
		Role newRole = roleRepository.findById(roleid).orElseThrow(() -> new RuntimeException("Error"));
		roles.add(newRole);
		user.setRoles(roles);
		userRepository.save(user);
		return userRepository.getRolesByUserId(usrid);
	}

	@PostMapping("/roles/name/{usrid}&{roleid}")
	public List<RoleDTO> getRolesByUserId(@PathVariable("usrid") int usrid, @PathVariable("roleid") ERole role) {
		User user = userRepository.findById(usrid).orElseThrow(() -> new RuntimeException("Error"));
		List<Role> roles = user.getRoles();
		Role newRole = roleRepository.findByName(role).orElseThrow(() -> new RuntimeException("Error"));
		roles.add(newRole);
		user.setRoles(roles);
		userRepository.save(user);
		return userRepository.getRolesByUserId(usrid);
	}

	@PutMapping("{id}/ids={ids}")
	public UserDTO updateUser(@PathVariable("id") int id, @PathVariable("ids") int idsocietate, @RequestBody UserDTO newUserDTO)
			throws ResourceNotFoundException {
		return userService.update(newUserDTO, idsocietate);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteUser(@PathVariable(value = "id") int id) throws ResourceNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));

		userRepository.delete(user);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
