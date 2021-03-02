package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "suspendare")
public class Suspendare implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "dela", nullable = false)
	private LocalDate dela;

	@Column(name = "panala", nullable = true)
	private LocalDate panala;

	@JsonBackReference(value="suspendare-contract")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract", nullable = false)
	private Contract contract;

	public Suspendare() {
	}

	public Suspendare(LocalDate dela, LocalDate panala,Contract contract) {
		this.dela = dela;
		this.panala = panala;
		this.contract=contract;
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

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Suspendare update(Suspendare suspendare) {
		dela=suspendare.dela;
		panala=suspendare.panala;
		return this;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}