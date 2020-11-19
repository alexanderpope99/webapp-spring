package net.guides.springboot2.crud.dto;

import java.util.List;

import net.guides.springboot2.crud.model.Adresa;
import net.guides.springboot2.crud.model.Angajat;

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

	private Integer nrangajati;

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

	public Adresa getAdresa() {
		return adresa;
	}

	public void setAdresa(Adresa adresa) {
		this.adresa = adresa;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Integer getNrangajati() {
		return nrangajati;
	}

	public void setNrangajati(Integer nrangajati) {
		this.nrangajati = nrangajati;
	}

	public void setNrangajati(List<Angajat> angajati) {
		this.nrangajati = angajati.size();
	}
}