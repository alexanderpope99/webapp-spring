package net.guides.springboot2.crud.dto;

public class AlteBeneficiiDTO {
	private int id;

	private String nume;

	private Float valoare;

	private Float procent;

	private String aplicare;

	private int idcontract;

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

	public int getIdcontract() {
		return idcontract;
	}

	public void setIdcontract(int idcontract) {
		this.idcontract = idcontract;
	}
}
