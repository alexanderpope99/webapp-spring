package net.guides.springboot2.crud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

	@JsonBackReference
	@OneToOne(mappedBy = "contbancar", fetch = FetchType.LAZY)
	private Contract contract;

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

}
