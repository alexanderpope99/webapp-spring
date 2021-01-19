package net.guides.springboot2.crud.model;

import java.io.Serializable;

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
public class Prime implements Serializable {
	private static final long serialVersionUID = 1L;

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
	private RealizariRetineri stat;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract")
	private Contract contract;

	public Prime() {
	}

	public Prime(Double valoare, RealizariRetineri stat) {
		this.valoare = valoare;
		this.stat = stat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// GETTERS
	public RealizariRetineri getStat() {
		return stat;
	}

	public Double getValoare() {
		return valoare;
	}

	// SETTERS
	public void setStat(RealizariRetineri stat) {
		this.stat = stat;
	}

	public void setValoare(Double valoare) {
		this.valoare = valoare;
	}

	public int getAn() {
		return an;
	}

	public Contract getContract() {
		return contract;
	}

	public int getLuna() {
		return luna;
	}

	public void setAn(int an) {
		this.an = an;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public void setLuna(int luna) {
		this.luna = luna;
	}
}
