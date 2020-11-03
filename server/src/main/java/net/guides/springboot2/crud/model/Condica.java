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
@Table(name = "condica")
public class Condica {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "inceput")
	private String inceput;

	@Column(name = "sfarsit")
	private String sfarsit;

	@Column(name = "pauzamasa")
	private String pauzamasa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract")
	private Contract idcontract;

	public Condica() {

	}

	public Condica(String inceput, String sfarsit, String pauzamasa, Contract idcontract) {
		this.inceput = inceput;
		this.sfarsit = sfarsit;
		this.pauzamasa = pauzamasa;
		this.idcontract = idcontract;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInceput() {
		return inceput;
	}

	public void setInceput(String inceput) {
		this.inceput = inceput;
	}

	public String getSfarsit() {
		return sfarsit;
	}

	public void setSfarsit(String sfarsit) {
		this.sfarsit = sfarsit;
	}

	public String getPauzamasa() {
		return pauzamasa;
	}

	public void setPauzamasa(String pauzamasa) {
		this.pauzamasa = pauzamasa;
	}

	public Contract getIdcontract() {
		return idcontract;
	}

	public void setIdcontract(Contract idcontract) {
		this.idcontract = idcontract;
	}
}
