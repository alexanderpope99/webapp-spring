package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Contract;

public class SporPermanentDTO {
	private int id;

	private String nume;

	private Double valoare;

	private Double procent;

	private String aplicare;

	private Contract contract;

	public String getAplicare() {
		return aplicare;
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

	public String getNume() {
		return nume;
	}

	public Double getProcent() {
		return procent;
	}

	public Double getValoare() {
		return valoare;
	}

	public void setAplicare(String aplicare) {
		this.aplicare = aplicare;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setContract(Contract idcontract) {
		this.contract = idcontract;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public void setProcent(Double procent) {
		this.procent = procent;
	}

	public void setValoare(Double valoare) {
		this.valoare = valoare;
	}
}
