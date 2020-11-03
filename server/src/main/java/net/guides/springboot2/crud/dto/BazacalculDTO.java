package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Angajat;

public class BazacalculDTO {
	private int id;

	private int luna;

	private int an;

	private int zilelucrate;

	private int salariurealizat;

	private Angajat angajat;

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

	public Integer getIdangajat() {
		if (angajat == null)
			return null;
		else
			return angajat.getIdpersoana();
	}

	public void setAngajat(Angajat idangajat) {
		this.angajat = idangajat;
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
