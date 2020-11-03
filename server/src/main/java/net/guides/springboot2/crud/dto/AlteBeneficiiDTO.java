package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Contract;

public class AlteBeneficiiDTO {
	private int id;

	private String nume;

	private Float valoare;

	private Float procent;

	private String aplicare;

	private Contract contract;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public Float getValoare() {
		return valoare;
	}

	public void setValoare(Float valoare) {
		this.valoare = valoare;
	}

	public String getAplicare() {
		return aplicare;
	}

	public void setAplicare(String aplicare) {
		this.aplicare = aplicare;
	}

	public Float getProcent() {
		return procent;
	}

	public void setProcent(Float procent) {
		this.procent = procent;
	}

	public Integer getIdcontract() {
		if (contract == null)
			return null;
		else
			return contract.getId();
	}

	public void setContract(Contract idcontract) {
		this.contract = idcontract;
	}
}
