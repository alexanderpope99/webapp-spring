package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class Concediu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "dela")
	private LocalDate dela;

	@Column(name = "panala")
	private LocalDate panala;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract")
	private Contract contract;

	public Concediu() {
	}

	public Concediu(LocalDate dela, LocalDate panala, Contract contract) {
		this.dela = dela;
		this.panala = panala;
		this.contract = contract;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDela() {
		return this.dela;
	}

	public void setDela(LocalDate dela) {
		this.dela = dela;
	}

	public LocalDate getPanala() {
		return this.panala;
	}

	public void setPanala(LocalDate panala) {
		this.panala = panala;
	}

	public Contract getContract() {
		return this.contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}
}
