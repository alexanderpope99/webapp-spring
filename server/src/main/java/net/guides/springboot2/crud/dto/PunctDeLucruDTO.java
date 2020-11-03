package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Adresa;
import net.guides.springboot2.crud.model.Societate;

public class PunctDeLucruDTO {
	private int id;

	private Adresa adresa;

	private Societate societate;

	private String nume;

	public int getId() {
		return id;
	}

	public Integer getIdadresa() {
		if (adresa == null)
			return null;
		else
			return adresa.getId();
	}

	public Integer getIdsocietate() {
		if (societate == null)
			return null;
		else
			return societate.getId();
	}

	public String getNume() {
		return nume;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAdresa(Adresa idadresa) {
		this.adresa = idadresa;
	}

	public void setSocietate(Societate idsocietate) {
		this.societate = idsocietate;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

}
