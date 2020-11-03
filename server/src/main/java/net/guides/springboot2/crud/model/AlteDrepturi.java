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
@Table(name = "altedrepturi")
public class AlteDrepturi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "valoare")
	private Float valoare;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idstat")
	private RealizariRetineri stat;

	public AlteDrepturi() {

	}

	public AlteDrepturi(Float valoare, RealizariRetineri stat) {
		this.valoare = valoare;
		this.stat = stat;
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

	public RealizariRetineri getStat() {
		return stat;
	}

	public void setStat(RealizariRetineri stat) {
		this.stat = stat;
	}
}
