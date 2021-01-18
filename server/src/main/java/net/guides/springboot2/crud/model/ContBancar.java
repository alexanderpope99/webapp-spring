package net.guides.springboot2.crud.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "contbancar")
public class ContBancar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "iban")
	private String iban;

	@Column(name = "numebanca")
	private String numebanca;

	@JsonBackReference(value = "contract-contbancar")
	@OneToOne(mappedBy = "contbancar", fetch = FetchType.LAZY)
	private Contract contract;

	@JsonBackReference(value = "societate-contbancar")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Societate societate;

	public ContBancar() {}

	public ContBancar(String iban, String numebanca) {
		this.iban = iban;
		this.numebanca = numebanca;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Contract getContract() {
		return contract;
	}
	
	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getNumebanca() {
		return numebanca;
	}

	public void setNumebanca(String adresa) {
		this.numebanca = adresa;
	}

	public Societate getSocietate() {
		return societate;
	}

	public void setSocietate(Societate societate) {
		this.societate = societate;
	}

}
