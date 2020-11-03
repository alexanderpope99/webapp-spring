package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Adresa;

public class SocietateDTO {

	private int id;

	private String nume;

	private Long idcaen;

	private String cif;

	private Double capsoc;

	private String regcom;

	private Integer idadresa;

	private String email;

	private String telefon;

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

	public Long getIdcaen() {
		return idcaen;
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

	public void setIdcaen(Long idcaen) {
		this.idcaen = idcaen;
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
		return idadresa;
	}

	public void setIdadresa(Adresa idadresa) {
		if (idadresa != null)
			this.idadresa = idadresa.getId();
		else
			this.idadresa = null;
	}

}