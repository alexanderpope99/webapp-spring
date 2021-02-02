package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.model.ERole;
import net.guides.springboot2.crud.model.Role;
import net.guides.springboot2.crud.repository.RoleRepository;

@Service
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;

	public void init() {
		if (roleRepository.count() == 0) {
			roleRepository.save(new Role(ERole.ROLE_ADMIN));
			roleRepository.save(new Role(ERole.ROLE_DIRECTOR));
			roleRepository.save(new Role(ERole.ROLE_CONTABIL));
			roleRepository.save(new Role(ERole.ROLE_ANGAJAT));
			roleRepository.save(new Role(ERole.ROLE_OPERATOR));
		} else addOperator();
	}

	public void addOperator() {
		if(roleRepository.count() < 5)
			roleRepository.save(new Role(ERole.ROLE_OPERATOR));
	}
}