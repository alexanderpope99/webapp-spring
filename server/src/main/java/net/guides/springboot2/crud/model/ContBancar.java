package net.guides.springboot2.crud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "contbancar")
public class ContBancar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "iban", nullable = false)
	private String iban;

	@Column(name = "numebanca")
	private String numebanca;

	@OneToOne(mappedBy = "contbancar", fetch = FetchType.LAZY)
	private Contract contract;

	public ContBancar() {}

	public ContBancar(String iban, String numebanca) {
		this.iban = iban;
		this.numebanca = numebanca;
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
