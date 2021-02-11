package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Activitate;

public class ProiectDTO {
	private int id;
	private String nume;
	private int idactivitate;

	private Activitate activitate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public Integer getIdactivitate() {
		if (activitate == null)
			return idactivitate;
		else
			return activitate.getId();
	}

	public void setActivitate(Activitate activitate) {
		this.activitate = activitate;
	}
	public void setIdactivitate(int idactivitate) {
		this.idactivitate = idactivitate;
	}

}
