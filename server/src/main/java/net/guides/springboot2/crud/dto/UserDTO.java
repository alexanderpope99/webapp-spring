package net.guides.springboot2.crud.dto;

import java.util.HashSet;
import java.util.Set;

import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Role;
import net.guides.springboot2.crud.model.Societate;

public class UserDTO {
	private Long id;

	private String username;

	private String email;

	private String password;

	private Angajat angajat;

	private Set<Role> roles;

	private Set<Societate> societati;

	private boolean gen;

	public String getEmail() {
		return email;
	}

	public Long getId() {
		return id;
	}

	public Integer getIdangajat() {
		if (angajat == null)
			return null;
		else
			return angajat.getIdpersoana();
	}

	public String getPassword() {
		return password;
	}

	public Set<Integer> getRoles() {
		Set<Integer> rolesIds = new HashSet<Integer>();
		for (Role role : roles)
			rolesIds.add(role.getId());
		return rolesIds;
	}

	public Set<Integer> getSocietati() {
		Set<Integer> societatiIds = new HashSet<Integer>();
		for (Societate societate : societati)
			societatiIds.add(societate.getId());
		return societatiIds;
	}

	public String getUsername() {
		return username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGen(boolean gen) {
		this.gen = gen;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIdangajat(Angajat angajat) {
		this.angajat = angajat;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void setSocietati(Set<Societate> societati) {
		this.societati = societati;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isGen() {
		return gen;
	}

}
