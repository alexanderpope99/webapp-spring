package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Contract;

public class SponsorizariDTO {
	private int id;

	private Contract contract;

	private String denumire;

	private String cui;

	private Double cota;

	private Double suma;

	public Double getCota() {
		return cota;
	}

	public String getCui() {
		return cui;
	}

	public String getDenumire() {
		return denumire;
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

	public Double getSuma() {
		return suma;
	}

	public void setCota(Double cota) {
		this.cota = cota;
	}

	public void setCui(String cui) {
		this.cui = cui;
	}

	public void setDenumire(String denumire) {
		this.denumire = denumire;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setContract(Contract idcontract) {
		this.contract = idcontract;
	}

	public void setSuma(Double suma) {
		this.suma = suma;
	}
}
