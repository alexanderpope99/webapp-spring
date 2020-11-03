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
@Table(name = "altedrepturi")
public class AlteDrepturi {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "valoare")
	private Float valoare;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idstat")
	private RealizariRetineri idstat;

	public AlteDrepturi() {

	}

	public AlteDrepturi(Float valoare, RealizariRetineri idstat) {
		this.valoare = valoare;
		this.idstat = idstat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Float getValoare() {
		return valoare;
	}

	public void setValoare(Float valoare) {
		this.valoare = valoare;
	}

	public RealizariRetineri getIdstat() {
		return idstat;
	}

	public void setIdstat(RealizariRetineri idstat) {
		this.idstat = idstat;
	}
}
