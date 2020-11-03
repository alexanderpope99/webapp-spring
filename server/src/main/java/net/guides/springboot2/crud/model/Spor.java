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
@Table(name = "spor")
public class Spor implements Serializable {
	private static final long serialVersionUID = 1L;

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
	@JoinColumn(name = "idstat")
	private RealizariRetineri stat;

	public Spor() {
	}

	public Spor(String nume, Double valoare, Double procent, String aplicare, RealizariRetineri stat) {
		this.nume = nume;
		this.valoare = valoare;
		this.procent = procent;
		this.aplicare = aplicare;
		this.stat = stat;
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

	public RealizariRetineri getStat() {
		return stat;
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

	public void setStat(RealizariRetineri stat) {
		this.stat = stat;
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
}
