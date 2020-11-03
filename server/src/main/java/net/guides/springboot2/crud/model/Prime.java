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
@Table(name = "prime")
public class Prime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "valoare")
	private Double valoare;

	@Column(name = "luna")
	private int luna;

	@Column(name = "an")
	private int an;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idstat")
	private RealizariRetineri idstat;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract")
	private Contract idcontract;

	public Prime() {
	}

	public Prime(Double valoare, RealizariRetineri idstat) {
		this.valoare = valoare;
		this.idstat = idstat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// GETTERS
	public RealizariRetineri getIdstat() {
		return idstat;
	}

	public Double getValoare() {
		return valoare;
	}

	// SETTERS
	public void setIdstat(RealizariRetineri idstat) {
		this.idstat = idstat;
	}

	public void setValoare(Double valoare) {
		this.valoare = valoare;
	}

	public int getAn() {
		return an;
	}

	public Contract getIdcontract() {
		return idcontract;
	}

	public int getLuna() {
		return luna;
	}

	public void setAn(int an) {
		this.an = an;
	}

	public void setIdcontract(Contract idcontract) {
		this.idcontract = idcontract;
	}

	public void setLuna(int luna) {
		this.luna = luna;
	}
}
