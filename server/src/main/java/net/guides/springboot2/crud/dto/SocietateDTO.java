package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Adresa;

public class SocietateDTO {

	private int id;

	private String nume;

	private Long caen;

	private String cif;

	private Double capsoc;

	private String regcom;

	private Adresa adresa;

	private String email;

	private String telefon;

	private String fax;

	public Double getCapsoc() {
		return capsoc;
	}

	public String getCif() {
		return cif;
	}

	public String getEmail() {
		return email;
	}

	public int getId() {
		return id;
	}

	public Long getCaen() {
		return caen;
	}

	public String getNume() {
		return nume;
	}

	public String getRegcom() {
		return regcom;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setCapsoc(Double capsoc) {
		this.capsoc = capsoc;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCaen(Long idcaen) {
		this.caen = idcaen;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public void setRegcom(String regcom) {
		this.regcom = regcom;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
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

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

}