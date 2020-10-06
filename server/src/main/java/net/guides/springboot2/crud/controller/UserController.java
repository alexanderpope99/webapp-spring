package net.guides.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.dto.PersoanaDTO;
import net.guides.springboot2.crud.dto.RoleDTO;
import net.guides.springboot2.crud.dto.SocietateDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Role;
import net.guides.springboot2.crud.model.User;
import net.guides.springboot2.crud.repository.RoleRepository;
import net.guides.springboot2.crud.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@GetMapping
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/roles/{usrid}")
	public List<RoleDTO> getRolesByUserId(@PathVariable("usrid") long usrid) {
		return userRepository.getRolesByUserId(usrid);
	}

	@GetMapping("/societati/{usrid}")
	public List<SocietateDTO> getSocietiesByUserId(@PathVariable("usrid") long usrid) {
		return userRepository.getSocietiesByUserId(usrid);
	}

	@GetMapping("/persoana/{usrid}")
	public List<PersoanaDTO> getPersoanaByUserId(@PathVariable("usrid") long usrid) {
		return userRepository.getPersoanaByUserId(usrid);
	}

	@GetMapping("/societate/{usrid}")
	public List<SocietateDTO> getSocietateByUserId(@PathVariable("usrid") long usrid) {
		return userRepository.getSocietateByUserId(usrid);
	}

	@GetMapping("/superior/{usrid}")
	public List<PersoanaDTO> getSuperiorByUserId(@PathVariable("usrid") long usrid) {
		return userRepository.getSuperiorByUserId(usrid);
	}

	@PostMapping("/roles/{usrid}&{roleid}")
	public List<RoleDTO> getRolesByUserId(@PathVariable("usrid") long usrid, @PathVariable("roleid") long roleid) {
		User user = userRepository.findById(usrid).orElseThrow(() -> new RuntimeException("Error"));
		Set<Role> roles = user.getRoles();
		Role newRole = roleRepository.findById(roleid).orElseThrow(() -> new RuntimeException("Error"));
		roles.add(newRole);
		user.setRoles(roles);
		userRepository.save(user);
		return userRepository.getRolesByUserId(usrid);
	}

	@DeleteMapping("{id}")
	public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tichete not found for this id :: " + id));

		userRepository.delete(user);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
