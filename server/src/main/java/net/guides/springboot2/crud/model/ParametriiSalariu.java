package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parametriisalariu")
public class ParametriiSalariu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "salariumin")
	private Integer salariumin;

	@Column(name = "salariuminstudiivechime")
	private Integer salariuminstudiivechime;

	@Column(name = "salariumediubrut")
	private Integer salariumediubrut;

	@Column(name = "impozit")
	private Float impozit;

	@Column(name = "cas")
	private Float cas;

	@Column(name = "cass")
	private Float cass;

	@Column(name = "cam")
	private Float cam;

	@Column(name = "valtichet")
	private Float valtichet;

	@Column(name = "date")
	private LocalDate date;

	public ParametriiSalariu() {
	}

	public ParametriiSalariu(Integer salariumin, Integer salariuminstudiivechime, Integer salariumediubrut, Float impozit,
			Float cas, Float cass, Float cam, Float valtichet, LocalDate date) {
		this.salariumin = salariumin;
		this.salariuminstudiivechime = salariuminstudiivechime;
		this.salariumediubrut = salariumediubrut;
		this.impozit = impozit;
		this.cas = cas;
		this.cass = cass;
		this.cam = cam;
		this.valtichet = valtichet;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// GETTERS
	public Float getCam() {
		return cam;
	}

	public Float getCas() {
		return cas;
	}

	public Float getCass() {
		return cass;
	}

	public Float getImpozit() {
		return impozit;
	}

	public Integer getSalariumediubrut() {
		return salariumediubrut;
	}

	public Integer getSalariumin() {
		return salariumin;
	}

	public Integer getSalariuminstudiivechime() {
		return salariuminstudiivechime;
	}

	public Float getValtichet() {
		return valtichet;
	}

	public LocalDate getDate() {
		return date;
	}

	// SETTERS
	public void setCam(Float cam) {
		this.cam = cam;
	}

	public void setCas(Float cas) {
		this.cas = cas;
	}

	public void setCass(Float cass) {
		this.cass = cass;
	}

	public void setImpozit(Float impozit) {
		this.impozit = impozit;
	}

	public void setSalariumediubrut(Integer salariumediubrut) {
		this.salariumediubrut = salariumediubrut;
	}

	public void setSalariumin(Integer salariumin) {
		this.salariumin = salariumin;
	}

	public void setSalariuminstudiivechime(Integer salariuminstudiivechime) {
		this.salariuminstudiivechime = salariuminstudiivechime;
	}

	public void setValtichet(Float valtichet) {
		this.valtichet = valtichet;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
