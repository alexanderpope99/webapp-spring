package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.RealizariRetineri;

public class PrimeDTO {
	private int id;

	private Double valoare;

	private int luna;

	private int an;

	private RealizariRetineri stat;

	private Contract contract;

	public int getAn() {
		return an;
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

	public Integer getIdstat() {
		if (stat == null)
			return null;
		else
			return stat.getId();
	}

	public int getLuna() {
		return luna;
	}

	public Double getValoare() {
		return valoare;
	}

	public void setAn(int an) {
		this.an = an;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setContract(Contract idcontract) {
		this.contract = idcontract;
	}

	public void setStat(RealizariRetineri idstat) {
		this.stat = idstat;
	}

	public void setLuna(int luna) {
		this.luna = luna;
	}

	public void setValoare(Double valoare) {
		this.valoare = valoare;
	}
}
