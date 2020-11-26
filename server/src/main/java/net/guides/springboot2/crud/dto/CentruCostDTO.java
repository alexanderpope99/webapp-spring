package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Adresa;
import net.guides.springboot2.crud.model.Societate;

public class CentruCostDTO {
	private int id;

	private Adresa adresa;

	private Societate societate;

	private String nume;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getIdadresa() {
		if (adresa == null)
			return null;
		else
			return adresa.getId();
	}

	public void setAdresa(Adresa idadresa) {
		this.adresa = idadresa;
	}

	public Integer getIdsocietate() {
		if (societate == null)
			return null;
		else
			return societate.getId();
	}

	public void setSocietate(Societate idsocietate) {
		this.societate = idsocietate;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}
}
