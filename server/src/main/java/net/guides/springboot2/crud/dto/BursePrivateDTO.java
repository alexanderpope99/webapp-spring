package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

import net.guides.springboot2.crud.model.Contract;

public class BursePrivateDTO {
	private int id;

	private Contract contract;

	private LocalDate data;

	private Float cota;

	private Float suma;

	public Float getCota() {
		return cota;
	}

	public LocalDate getData() {
		return data;
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

	public Float getSuma() {
		return suma;
	}

	public void setCota(Float cota) {
		this.cota = cota;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public void setSuma(Float suma) {
		this.suma = suma;
	}
}
