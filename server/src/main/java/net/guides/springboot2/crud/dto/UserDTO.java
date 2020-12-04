package net.guides.springboot2.crud.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Role;
import net.guides.springboot2.crud.model.Societate;

public class UserDTO {
	private int id;

	private String username;

	private String email;

	private List<Angajat> angajati;

	private List<Role> roles;

	private Map<Integer, String> societati;

	private boolean gen;

	public int getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getEmail() {
		return email;
	}
	public boolean isGen() {
		return gen;
	}
	public List<Angajat> getAngajati() {
		return angajati;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public Map<Integer, String> getSocietati() {
		return societati;
	}

	public void setId(int id) {
		this.id = id;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setGen(boolean gen) {
		this.gen = gen;
	}
	public void setAngajati(List<Angajat> angajati) {
		this.angajati = angajati;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public void setSocietati(Map<Integer, String> societati) {
		this.societati = societati;
	}
	public void setSocietatiClass(List<Societate> societati) {
		this.societati = new HashMap<>();
		societati.forEach(soc -> this.societati.put(soc.getId(), soc.getNume()));
	}

		
	
}
