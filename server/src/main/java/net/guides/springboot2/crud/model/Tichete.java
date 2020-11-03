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
@Table(name = "tichete")
public class Tichete {

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
	private RealizariRetineri idstat;

	public Tichete() {
	}

	public Tichete(String tip, Long nr, Long restituite, Double valoare, Boolean impozabil, RealizariRetineri idstat) {
		this.tip = tip;
		this.nr = nr;
		this.restituite = restituite;
		this.valoare = valoare;
		this.impozabil = impozabil;
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
	public void setIdstat(RealizariRetineri idstat) {
		this.idstat = idstat;
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
