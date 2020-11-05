package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "cerericoncediu")
public class CereriConcediu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// @JsonManagedReference(value = "cerereconcediu-angajat")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pentru")
	private Angajat pentru;

	@Column(name = "dela")
	private LocalDate dela;

	@Column(name = "panala")
	private LocalDate panala;

	@Column(name = "tip")
	private String tip;

	@Column(name = "motiv")
	private String motiv;

	@Column(name = "status")
	private String status;

	// @JsonManagedReference(value = "cerericoncediu-societate")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idsocietate")
	private Societate societate;

	public CereriConcediu() {
	}

	public CereriConcediu(Angajat pentru, LocalDate dela, LocalDate panala, String tip, String motiv, String status,
			Societate societate) {
		this.pentru = pentru;
		this.dela = dela;
		this.panala = panala;
		this.tip = tip;
		this.motiv = motiv;
		this.status = status;
		this.societate = societate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Angajat getPentru() {
		return pentru;
	}

	public void setPentru(Angajat pentru) {
		this.pentru = pentru;
	}

	public LocalDate getDela() {
		return dela;
	}

	public void setDela(LocalDate dela) {
		this.dela = dela;
	}

	public LocalDate getPanala() {
		return panala;
	}

	public void setPanala(LocalDate panala) {
		this.panala = panala;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getMotiv() {
		return motiv;
	}

	public void setMotiv(String motiv) {
		this.motiv = motiv;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Societate getSocietate() {
		return societate;
	}

	public void setSocietate(Societate societate) {
		this.societate = societate;
	}

}
