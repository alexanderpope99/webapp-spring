package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "deduceri")
public class Deduceri {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "dela")
	private Integer dela;

	@Column(name = "panala")
	private Integer panala;

	@Column(name = "zero")
	private Integer zero;

	@Column(name = "una")
	private Integer una;

	@Column(name = "doua")
	private Integer doua;

	@Column(name = "trei")
	private Integer trei;

	@Column(name = "patru")
	private Integer patru;

	public Deduceri() {
	}

	public Deduceri(Integer dela, Integer panala, Integer zero, Integer una, Integer doua, Integer trei,
			Integer patru) {
		this.dela = dela;
		this.panala = panala;
		this.zero = zero;
		this.una = una;
		this.doua = doua;
		this.trei = trei;
		this.patru = patru;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// GETTERS
	public Integer getDela() {
		return dela;
	}

	public Integer getDoua() {
		return doua;
	}

	public Integer getPanala() {
		return panala;
	}

	public Integer getPatru() {
		return patru;
	}

	public Integer getTrei() {
		return trei;
	}

	public Integer getUna() {
		return una;
	}

	public Integer getZero() {
		return zero;
	}

	// SETTERS
	public void setDela(Integer dela) {
		this.dela = dela;
	}

	public void setDoua(Integer doua) {
		this.doua = doua;
	}

	public void setPanala(Integer panala) {
		this.panala = panala;
	}

	public void setPatru(Integer patru) {
		this.patru = patru;
	}

	public void setTrei(Integer trei) {
		this.trei = trei;
	}

	public void setUna(Integer una) {
		this.una = una;
	}

	public void setZero(Integer zero) {
		this.zero = zero;
	}
}
