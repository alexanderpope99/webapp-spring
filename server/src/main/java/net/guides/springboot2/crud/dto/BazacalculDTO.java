package net.guides.springboot2.crud.dto;

public class BazacalculDTO {
	private int id;

	private int luna;

	private int an;

	private int zilelucrate;

	private int salariurealizat;

	private int idangajat;

	public int getAn() {
		return an;
	}

	public void setAn(int an) {
		this.an = an;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdangajat() {
		return idangajat;
	}

	public void setIdangajat(int idangajat) {
		this.idangajat = idangajat;
	}

	public int getLuna() {
		return luna;
	}

	public void setLuna(int luna) {
		this.luna = luna;
	}

	public int getSalariurealizat() {
		return salariurealizat;
	}

	public void setSalariurealizat(int salariurealizat) {
		this.salariurealizat = salariurealizat;
	}

	public int getZilelucrate() {
		return zilelucrate;
	}

	public void setZilelucrate(int zilelucrate) {
		this.zilelucrate = zilelucrate;
	}
}
