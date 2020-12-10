package net.guides.springboot2.crud.dto;

import java.util.ArrayList;
import java.util.List;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Role;
import net.guides.springboot2.crud.model.Societate;

public class UserDTO {
	private int id;

	private String username;

	private String email;

	private List<Angajat> angajati;

	private List<Role> roles;

	private List<SocietateJSON> societati;

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
	public List<SocietateJSON> getSocietati() {
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
	public void setSocietati(List<SocietateJSON> societati) {
		this.societati = societati;
	}
	public void setSocietatiClass(List<Societate> societati) {
    this.societati = new ArrayList<>();
    societati.forEach(soc -> this.societati.add(new SocietateJSON(soc.getId(), soc.getNume())));
	}
}


