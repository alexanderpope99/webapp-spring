package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.ActIdentitate;
import net.guides.springboot2.crud.model.Adresa;

public class PersoanaDTO {
	private int id;

	private String gen;

	private String nume;

	private String prenume;

	private ActIdentitate actidentitate;

	private Adresa adresa;

	private String starecivila;

	private String email;

	private String telefon;

	private String cnp;

	public String getCnp() {
		return cnp;
	}

	public String getEmail() {
		return email;
	}

	public String getGen() {
		return gen;
	}

	public int getId() {
		return id;
	}

	public Integer getIdactidentitate() {
		if (actidentitate == null)
			return null;
		else
			return actidentitate.getId();
	}

	public Integer getIdadresa() {
		if (adresa == null)
			return null;
		else
			return adresa.getId();
	}

	public String getNume() {
		return nume;
	}

	public String getPrenume() {
		return prenume;
	}

	public String getStarecivila() {
		return starecivila;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setCnp(String cnp) {
		this.cnp = cnp;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGen(String gen) {
		this.gen = gen;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setActidentitate(ActIdentitate idactidentitate) {
		this.actidentitate = idactidentitate;
	}

	public void setAdresa(Adresa idadresa) {
		this.adresa = idadresa;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}

	public void setStarecivila(String starecivila) {
		this.starecivila = starecivila;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
}
