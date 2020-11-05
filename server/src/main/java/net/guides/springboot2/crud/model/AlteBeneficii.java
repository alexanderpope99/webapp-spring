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
@Table(name = "altebeneficii")
public class AlteBeneficii implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "nume")
	private String nume;

	@Column(name = "valoare")
	private Float valoare;

	@Column(name = "procent")
	private Float procent;

	@Column(name = "aplicare")
	private String aplicare;

	@ManyToOne
	@JoinColumn(name = "idcontract")
	private Contract contract;

	public AlteBeneficii() {

	}

	public AlteBeneficii(String nume, Float valoare, Float procent, String aplicare, Contract contract) {
		this.nume = nume;
		this.valoare = valoare;
		this.procent = procent;
		this.aplicare = aplicare;
		this.contract = contract;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public Float getValoare() {
		return valoare;
	}

	public void setValoare(Float valoare) {
		this.valoare = valoare;
	}

	public Float getProcent() {
		return procent;
	}

	public void setProcent(Float procent) {
		this.procent = procent;
	}

	public String getAplicare() {
		return aplicare;
	}

	public void setAplicare(String aplicare) {
		this.aplicare = aplicare;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}
}
