package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

public class CODTO {
	private int id;

	private String tip;

	private LocalDate dela;

	private LocalDate panala;

	private Boolean sporuripermanente;

	private int idcontract;

	public LocalDate getDela() {
		return dela;
	}

	public int getId() {
		return id;
	}

	public int getIdcontract() {
		return idcontract;
	}

	public LocalDate getPanala() {
		return panala;
	}

	public Boolean getSporuripermanente() {
		return sporuripermanente;
	}

	public String getTip() {
		return tip;
	}

	public void setDela(LocalDate dela) {
		this.dela = dela;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIdcontract(int idcontract) {
		this.idcontract = idcontract;
	}

	public void setPanala(LocalDate panala) {
		this.panala = panala;
	}

	public void setSporuripermanente(Boolean sporuripermanente) {
		this.sporuripermanente = sporuripermanente;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

}
