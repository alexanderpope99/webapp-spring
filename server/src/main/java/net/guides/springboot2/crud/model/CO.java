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

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "co")
public class CO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "tip")
	private String tip;

	@Column(name = "dela")
	private LocalDate dela;

	@Column(name = "panala")
	private LocalDate panala;

	@Column(name = "sporuripermanente")
	private Boolean sporuripermanente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract")
	private Contract contract;

	public CO() {
	}

	public CO(String tip, LocalDate dela, LocalDate panala, Boolean sporuripermanente, Contract contract) {
		this.tip = tip;
		this.dela = dela;
		this.panala = panala;
		this.sporuripermanente = sporuripermanente;
		this.contract = contract;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
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

	public Boolean isSporuripermanente() {
		return sporuripermanente;
	}

	public void setSporuripermanente(Boolean sporuripermanente) {
		this.sporuripermanente = sporuripermanente;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}
}
