package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Societate;

public class ActivitateDTO {
	private int id;
	private String nume;
	private int idsocietate;

	private Societate societate;

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

	public Integer getIdsocietate() {
		if (societate == null)
			return idsocietate;
		else
			return societate.getId();
	}

	public void setSocietate(Societate societate) {
		this.societate = societate;
	}
	public void setIdsocietate(int idsocietate) {
		this.idsocietate = idsocietate;
	}

}
