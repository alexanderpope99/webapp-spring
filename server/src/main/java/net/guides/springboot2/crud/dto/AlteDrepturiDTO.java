package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.RealizariRetineri;

public class AlteDrepturiDTO {
	private int id;

	private Float valoare;

	private RealizariRetineri stat;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Float getValoare() {
		return valoare;
	}

	public void setValoare(Float valoare) {
		this.valoare = valoare;
	}

	public Integer getIdstat() {
		if (stat == null)
			return null;
		else
			return stat.getId();
	}

	public void setStat(RealizariRetineri idstat) {
		this.stat = idstat;
	}
}
