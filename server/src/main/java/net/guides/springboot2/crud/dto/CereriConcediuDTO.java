package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Societate;

public class CereriConcediuDTO {
	private int id;

	private Angajat pentru;

	private LocalDate dela;

	private LocalDate panala;

	private String tip;

	private String motiv;

	private String status;

	private Societate societate;

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

	public Integer getPentru() {
		if (pentru == null)
			return null;
		else
			return pentru.getPersoana().getId();
	}

	public void setPentru(Angajat pentru) {
		this.pentru = pentru;
	}

	public Integer getIdsocietate() {
		if (societate == null)
			return null;
		else
			return societate.getId();
	}

	public void setSocietate(Societate societate) {
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
