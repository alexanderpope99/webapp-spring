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
@Table(name = "tichete")
public class Tichete implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "tip")
	private String tip;

	@Column(name = "nr")
	private Long nr;

	@Column(name = "restituite")
	private Long restituite;

	@Column(name = "valoare")
	private Double valoare;

	@Column(name = "impozabil")
	private Boolean impozabil;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idstat")
	private RealizariRetineri stat;

	public Tichete() {
	}

	public Tichete(String tip, Long nr, Long restituite, Double valoare, Boolean impozabil, RealizariRetineri stat) {
		this.tip = tip;
		this.nr = nr;
		this.restituite = restituite;
		this.valoare = valoare;
		this.impozabil = impozabil;
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

	public Boolean getImpozabil() {
		return impozabil;
	}

	public Long getNr() {
		return nr;
	}

	public Long getRestituite() {
		return restituite;
	}

	public String getTip() {
		return tip;
	}

	public Double getValoare() {
		return valoare;
	}

	// SETTERS
	public void setStat(RealizariRetineri stat) {
		this.stat = stat;
	}

	public void setImpozabil(Boolean impozabil) {
		this.impozabil = impozabil;
	}

	public void setNr(Long nr) {
		this.nr = nr;
	}

	public void setRestituite(Long restituite) {
		this.restituite = restituite;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public void setValoare(Double valoare) {
		this.valoare = valoare;
	}
}
