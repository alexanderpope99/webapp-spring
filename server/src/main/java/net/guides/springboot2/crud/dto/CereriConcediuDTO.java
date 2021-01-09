package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

public class CereriConcediuDTO {
	private int id;

	private LocalDate dela;

	private LocalDate panala;

	private String tip;

	private String motiv;

	private String status;

	private int idsocietate;

	private int iduser;

	private String numeuser;

	public LocalDate getDela() {
		return dela;
	}

	public void setDela(LocalDate dela) {
		this.dela = dela;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMotiv() {
		return motiv;
	}

	public void setMotiv(String motiv) {
		this.motiv = motiv;
	}

	public LocalDate getPanala() {
		return panala;
	}

	public void setPanala(LocalDate panala) {
		this.panala = panala;
	}

	public int getIdsocietate() {
		return idsocietate;
	}

	public void setIdsocietate(int idsocietate) {
		this.idsocietate = idsocietate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public int getIduser() {
		return iduser;
	}

	public void setIduser(int iduser) {
		this.iduser = iduser;
	}

	public String getNumeuser() {
		return numeuser;
	}

	public void setNumeuser(String numeuser) {
		this.numeuser = numeuser;
	}
}
