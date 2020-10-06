package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "altebeneficii")
public class AlteBeneficii {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "nume")
	private String nume;

	@Column(name = "valoare")
	private Float valoare;

	@Column(name = "procent")
	private Float procent;

	@Column(name = "aplicare")
	private String aplicare;

	@Column(name = "idcontract")
	private Long idcontract;

	public AlteBeneficii() {

	}

	public AlteBeneficii(String nume, Float valoare, Float procent, String aplicare, Long idcontract) {
		this.nume = nume;
		this.valoare = valoare;
		this.procent = procent;
		this.aplicare = aplicare;
		this.idcontract = idcontract;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	@Column(name = "idcontract")
	public Long getIdcontract() {
		return idcontract;
	}

	public void setIdcontract(Long idcontract) {
		this.idcontract = idcontract;
	}
}
