package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Contract;

public class CondicaDTO {
	private int id;

	private String inceput;

	private String sfarsit;

	private String pauzamasa;

	private Contract contract;

	public int getId() {
		return id;
	}

	public Integer getIdcontract() {
		if (contract == null)
			return null;
		else
			return contract.getId();
	}

	public String getInceput() {
		return inceput;
	}

	public String getPauzamasa() {
		return pauzamasa;
	}

	public String getSfarsit() {
		return sfarsit;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setContract(Contract idcontract) {
		this.contract = idcontract;
	}

	public void setInceput(String inceput) {
		this.inceput = inceput;
	}

	public void setPauzamasa(String pauzamasa) {
		this.pauzamasa = pauzamasa;
	}

	public void setSfarsit(String sfarsit) {
		this.sfarsit = sfarsit;
	}
}
