package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

import net.guides.springboot2.crud.model.Contract;

public class CODTO {
	private int id;

	private String tip;

	private LocalDate dela;

	private LocalDate panala;

	private Boolean sporuripermanente;

	private Contract contract;

	public LocalDate getDela() {
		return dela;
	}

	public int getId() {
		return id;
	}

	public Integer getIdcontract() {
		if (contract == null)
			return null;
		else
			return contract.getId();
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

	public void setContract(Contract idcontract) {
		this.contract = idcontract;
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
