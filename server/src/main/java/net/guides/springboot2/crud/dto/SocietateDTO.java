package net.guides.springboot2.crud.dto;

import java.util.List;


import net.guides.springboot2.crud.model.Adresa;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.ContBancar;
import net.guides.springboot2.crud.model.Fisier;

public class SocietateDTO {

	private int id;

	private String nume;

	private Long idcaen;

	private String cif;

	private Double capsoc;

	private String regcom;

	private Adresa adresa;

	private String email;

	private String telefon;

	private String fax;

	private List<ContBancar> contbancar;

	private Integer nrangajati;

	private Integer idimagine;

	public Integer getIdimagine() {
		return idimagine;
	}

	public void setIdimagine(Integer idimagine) {
		this.idimagine = idimagine;
	}
	public void setIdangajat(Fisier imagine) {
		this.idimagine = imagine.getId();
	}

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

	public void setContbancar(List<ContBancar> contbancar) {
		this.contbancar = contbancar;
	}
	public List<ContBancar> getContbancar() {
		return contbancar;
	}
}