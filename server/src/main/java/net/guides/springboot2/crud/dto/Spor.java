package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.RealizariRetineri;

public class Spor {
	private int id;

	private String nume;

	private Double valoare;

	private Double procent;

	private String aplicare;

	private RealizariRetineri stat;

	public String getAplicare() {
		return aplicare;
	}

	public int getId() {
		return id;
	}

	public Integer getIdstat() {
		if (stat == null)
			return null;
		else
			return stat.getId();
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

	public void setStat(RealizariRetineri idstat) {
		this.stat = idstat;
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
