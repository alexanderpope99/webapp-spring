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

@Entity
@Table(name = "sporpermanent")
public class SporPermanent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "nume")
	private String nume;

	@Column(name = "valoare")
	private Double valoare;

	@Column(name = "procent")
	private Double procent;

	@Column(name = "aplicare")
	private String aplicare;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract")
	private Contract idcontract;

	public SporPermanent() {
	}

	public SporPermanent(String nume, Double valoare, Double procent, String aplicare, Contract idcontract) {
		this.nume = nume;
		this.valoare = valoare;
		this.procent = procent;
		this.aplicare = aplicare;
		this.idcontract = idcontract;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// GETTERS
	public String getAplicare() {
		return aplicare;
	}

	public Contract getIdstat() {
		return idcontract;
	}

	public String getNume() {
		return nume;
	}

	public Double getProcent() {
		return procent;
	}

	public Double getValoare() {
		return valoare;
	}

	// SETTERS
	public void setAplicare(String aplicare) {
		this.aplicare = aplicare;
	}

	public void setIdstat(Contract idcontract) {
		this.idcontract = idcontract;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public void setProcent(Double procent) {
		this.procent = procent;
	}

	public void setValoare(Double valoare) {
		this.valoare = valoare;
	}

	public Contract getIdcontract() {
		return idcontract;
	}

	public void setIdcontract(Contract idcontract) {
		this.idcontract = idcontract;
	}
}
