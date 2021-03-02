package net.guides.springboot2.crud.dto;

public class RRDetails {
	int idcontract;
	int luna;
	int an;
	int primaBruta;
	int nrTichete;
	int totalOreSuplimentare;
	int coNeefectuat;

	public RRDetails() {
	}

	public RRDetails(int idcontract, int luna, int an, int primaBruta, int nrTichete, int totalOreSuplimentare, int coNeefectuat) {
		this.idcontract = idcontract;
		this.luna = luna;
		this.an = an;
		this.primaBruta = primaBruta;
		this.nrTichete = nrTichete;
		this.totalOreSuplimentare = totalOreSuplimentare;
		this.coNeefectuat = coNeefectuat;
	}

	public int getIdcontract() {
		return this.idcontract;
	}

	public void setIdcontract(int idcontract) {
		this.idcontract = idcontract;
	}

	public int getLuna() {
		return this.luna;
	}

	public void setLuna(int luna) {
		this.luna = luna;
	}

	public int getAn() {
		return this.an;
	}

	public void setAn(int an) {
		this.an = an;
	}

	public int getPrimaBruta() {
		return this.primaBruta;
	}

	public void setPrimaBruta(int primaBruta) {
		this.primaBruta = primaBruta;
	}

	public int getNrTichete() {
		return this.nrTichete;
	}

	public void setNrTichete(int nrTichete) {
		this.nrTichete = nrTichete;
	}

	public int getTotalOreSuplimentare() {
		return this.totalOreSuplimentare;
	}

	public void setTotalOreSuplimentare(int totalOreSuplimentare) {
		this.totalOreSuplimentare = totalOreSuplimentare;
	}

	public int getCoNeefectuat() {
		return coNeefectuat;
	}

	public void setConeefectuat(int coNeefectuat) {
		this.coNeefectuat = coNeefectuat;
	}
}
