package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

public class CereriConcediuDTO {
	private int id;

	private int pentru;

	private LocalDate dela;

	private LocalDate panala;

	private String tip;

	private String motiv;

	private String status;

	private int societate;

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

	public int getPentru() {
		return pentru;
	}

	public void setPentru(int pentru) {
		this.pentru = pentru;
	}

	public int getSocietate() {
		return societate;
	}

	public void setSocietate(int societate) {
		this.societate = societate;
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
}
