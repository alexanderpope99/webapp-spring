package net.guides.springboot2.crud.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "contbancar")
public class ContBancar {
	@Id
	@Column(name = "iban", nullable = false)
	private String iban;

	@Column(name = "numebanca")
	private String numebanca;

	@ManyToMany(mappedBy = "iban")
	private Set<Societate> societati = new HashSet<>();

	public ContBancar() {

	}

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

	public Set<Societate> getSocietati() {
		return societati;
	}

	public void setSocietati(Set<Societate> societati) {
		this.societati = societati;
	}

}
