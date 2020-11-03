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
@Table(name = "condica")
public class Condica implements Serializable {
	private static final long serialVersionUID = 1L;
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
	private Contract contract;

	public Condica() {

	}

	public Condica(String inceput, String sfarsit, String pauzamasa, Contract contract) {
		this.inceput = inceput;
		this.sfarsit = sfarsit;
		this.pauzamasa = pauzamasa;
		this.contract = contract;
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

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}
}
