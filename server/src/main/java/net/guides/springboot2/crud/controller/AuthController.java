package net.guides.springboot2.crud.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.model.ERole;
import net.guides.springboot2.crud.model.Role;
import net.guides.springboot2.crud.model.User;
import net.guides.springboot2.crud.payload.request.ChangePasswordRequest;
import net.guides.springboot2.crud.payload.request.LoginRequest;
import net.guides.springboot2.crud.payload.request.SignupRequest;
import net.guides.springboot2.crud.payload.request.UpdateProfileRequest;
import net.guides.springboot2.crud.payload.response.JwtResponse;
import net.guides.springboot2.crud.payload.response.MessageResponse;
import net.guides.springboot2.crud.repository.RoleRepository;
import net.guides.springboot2.crud.repository.UserRepository;
import net.guides.springboot2.crud.security.jwt.JwtUtils;
import net.guides.springboot2.crud.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
				userDetails.getEmail(), roles, userDetails.isGen()));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));
		user.setGen(signUpRequest.isGen());

		Set<Role> roles = new HashSet<>();

		Role angajatRole = roleRepository.findByName(ERole.ROLE_ANGAJAT)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(angajatRole);

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PutMapping("/change-password/{uid}")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
			@PathVariable("uid") Long uid) {

		User user = userRepository.findById(uid).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

		// String reqPassword = encoder.encode(changePasswordRequest.getPassword());

		if (!encoder.matches(changePasswordRequest.getPassword(), user.getPassword()))
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Parolă actuală incorectă!"));

		String newPassword = encoder.encode(changePasswordRequest.getNewpassword());
		user.setPassword(newPassword);

		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("Password changed!"));
	}

	@PutMapping("update-profile/{uid}")
	public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest updateProfileReq,
			@PathVariable("uid") Long uid) {
		User user = userRepository.findById(uid).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		user.setEmail(updateProfileReq.getEmail());
		user.setGen(updateProfileReq.isGen());

		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("Updated profile!"));
	}
}
