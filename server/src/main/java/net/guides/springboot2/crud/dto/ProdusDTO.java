package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Proiect;

public class ProdusDTO {
	private int id;
	private String denumire;
	private String um;
	private int cantitate;
	private Float pretunitar;
	private Float valoarefaratva;
	private Float valoaretva;
	private int idproiect;

	private Proiect proiect;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDenumire() {
		return denumire;
	}

	public void setDenumire(String denumire) {
		this.denumire = denumire;
	}

	public Float getPretunitar() {
		return pretunitar;
	}

	public void setPretunitar(Float pretunitar) {
		this.pretunitar = pretunitar;
	}

	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	public Float getValoarefaratva() {
		return valoarefaratva;
	}

	public void setValoarefaratva(Float valoarefaratva) {
		this.valoarefaratva = valoarefaratva;
	}

	public Float getValoaretva() {
		return valoaretva;
	}

	public void setValoaretva(Float valoaretva) {
		this.valoaretva = valoaretva;
	}

	public Integer getIdproiect() {
		if (proiect == null)
			return idproiect;
		else
			return proiect.getId();
	}

	public void setProiect(Proiect proiect) {
		this.proiect = proiect;
	}
	public void setIdproiect(int idproiect) {
		this.idproiect = idproiect;
	}

	public int getCantitate() {
		return cantitate;
	}

	public void setCantitate(int cantitate) {
		this.cantitate = cantitate;
	}

}
