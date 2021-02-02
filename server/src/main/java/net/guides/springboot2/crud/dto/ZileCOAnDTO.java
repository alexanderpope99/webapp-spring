package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Angajat;

public class ZileCOAnDTO {
	private int id;

	private int an;

	private int zilecoefectuat;

	private int zileconeefectuat;

	private int idangajat;
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
			return idangajat;
		else
			return angajat.getPersoana().getId();
	}

	public void setAngajat(Angajat angajat) {
		this.angajat = angajat;
	}
	public void setIdangajat(int idangajat) {
		this.idangajat = idangajat;
	}
	public void setIdangajat(Angajat angajat) {
		this.idangajat = angajat.getPersoana().getId();
	}

	public int getZilecoefectuat() {
		return zilecoefectuat;
	}

	public void setZilecoefectuat(int zilecoefectuat) {
		this.zilecoefectuat = zilecoefectuat;
	}

	public int getZileconeefectuat() {
		return zileconeefectuat;
	}

	public void setZileconeefectuat(int zileconeefectuat) {
		this.zileconeefectuat = zileconeefectuat;
	}
}
