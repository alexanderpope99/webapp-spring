package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

public class BursePrivateDTO {
	private int id;

	private int idcontract;

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

	public int getIdcontract() {
		return idcontract;
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

	public void setIdcontract(int idcontract) {
		this.idcontract = idcontract;
	}

	public void setSuma(Float suma) {
		this.suma = suma;
	}
}
