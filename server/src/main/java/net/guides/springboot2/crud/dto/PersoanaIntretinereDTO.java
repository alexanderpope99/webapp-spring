package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

import net.guides.springboot2.crud.model.Angajat;

public class PersoanaIntretinereDTO {
	private int id;

	private String nume;

	private String prenume;

	private String cnp;

	private LocalDate datanasterii;

	private String grad;

	private String gradinvaliditate;

	private Boolean intretinut;

	private Boolean coasigurat;

	private Angajat angajat;

	public String getCnp() {
		return cnp;
	}

	public Boolean getCoasigurat() {
		return coasigurat;
	}

	public LocalDate getDatanasterii() {
		return datanasterii;
	}

	public String getGrad() {
		return grad;
	}

	public String getGradinvaliditate() {
		return gradinvaliditate;
	}

	public int getId() {
		return id;
	}

	public Integer getIdangajat() {
		if (angajat == null)
			return null;
		else
			return angajat.getIdpersoana();
	}

	public Boolean getIntretinut() {
		return intretinut;
	}

	public String getNume() {
		return nume;
	}

	public String getPrenume() {
		return prenume;
	}

	public void setCnp(String cnp) {
		this.cnp = cnp;
	}

	public void setCoasigurat(Boolean coasigurat) {
		this.coasigurat = coasigurat;
	}

	public void setDatanasterii(LocalDate datanasterii) {
		this.datanasterii = datanasterii;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public void setGradinvaliditate(String gradinvaliditate) {
		this.gradinvaliditate = gradinvaliditate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAngajat(Angajat idangajat) {
		this.angajat = idangajat;
	}

	public void setIntretinut(Boolean intretinut) {
		this.intretinut = intretinut;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}
}
