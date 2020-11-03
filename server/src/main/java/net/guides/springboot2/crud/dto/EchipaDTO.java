package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Departament;

public class EchipaDTO {
	private int id;

	private Departament departament;

	private String nume;

	public int getId() {
		return id;
	}

	public Integer getIddepartament() {
		if (departament == null)
			return null;
		else
			return departament.getId();
	}

	public String getNume() {
		return nume;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDepartament(Departament iddepartament) {
		this.departament = iddepartament;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}
}
