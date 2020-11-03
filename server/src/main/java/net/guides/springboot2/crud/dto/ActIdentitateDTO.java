package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

public class ActIdentitateDTO {

	private int id;

	private String cnp;

	private String tip;

	private String serie;

	private String numar;

	private LocalDate datanasterii;

	private String eliberatde;

	private String dataeliberarii;

	private String loculnasterii;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCnp() {
		return cnp;
	}

	public void setCnp(String cnp) {
		this.cnp = cnp;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getNumar() {
		return numar;
	}

	public void setNumar(String numar) {
		this.numar = numar;
	}

	public LocalDate getDatanasterii() {
		return datanasterii;
	}

	public void setDatanasterii(LocalDate datanasterii) {
		this.datanasterii = datanasterii;
	}

	public String getEliberatde() {
		return eliberatde;
	}

	public void setEliberatde(String eliberatde) {
		this.eliberatde = eliberatde;
	}

	public String getDataeliberarii() {
		return dataeliberarii;
	}

	public void setDataeliberarii(String dataeliberarii) {
		this.dataeliberarii = dataeliberarii;
	}

	public String getLoculnasterii() {
		return loculnasterii;
	}

	public void setLoculnasterii(String loculnasterii) {
		this.loculnasterii = loculnasterii;
	}

}
