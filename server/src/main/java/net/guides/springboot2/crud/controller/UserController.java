package net.guides.springboot2.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.model.User;
import net.guides.springboot2.crud.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/roles/{usrid}")
	public List<String> getRolesByUserId(@PathVariable("usrid") long usrid) {
		return userRepository.getRolesByUserId(usrid);
	}

	@GetMapping("/societati/{usrid}")
	public List<String> getSocietiesByUserId(@PathVariable("usrid") long usrid) {
		return userRepository.getSocietiesByUserId(usrid);
	}

	@GetMapping("/numeprenume/{usrid}")
	public List<String> getNumePrenumeByUserId(@PathVariable("usrid") long usrid) {
		return userRepository.getNumePrenumeByUserId(usrid);
	}

	@GetMapping("/societate/{usrid}")
	public List<String> getSocietateByUserId(@PathVariable("usrid") long usrid) {
		return userRepository.getSocietateByUserId(usrid);
	}

	@GetMapping("/superior/{usrid}")
	public List<String> getSuperiorByUserId(@PathVariable("usrid") long usrid) {
		return userRepository.getSuperiorByUserId(usrid);
	}
}
