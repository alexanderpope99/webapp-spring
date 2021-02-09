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
@Table(name = "adresa")
public class Adresa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "adresa")
	private String adresa;
	@Column(name = "localitate")
	private String localitate;
	@Column(name = "judet")
	private String judet;

	@JsonBackReference(value = "adresa-societate")
	@OneToOne(mappedBy = "adresa", fetch = FetchType.LAZY)
	private Societate societati;

	@JsonBackReference(value = "adresa-persoana")
	@OneToOne(mappedBy = "adresa", fetch = FetchType.LAZY)
	private Persoana persoane;

	public Adresa() {
	}

	public Adresa(String adresa, String localitate, String judet) {
		this.adresa = adresa;
		this.localitate = localitate;
		this.judet = judet;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAdresa() {
		return adresa == null ? "" : adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getLocalitate() {
		return localitate == null ? "" : localitate;
	}

	public void setLocalitate(String localitate) {
		this.localitate = localitate;
	}

	public String getJudet() {
		return judet == null ? "" : judet;
	}

	public void setJudet(String judet) {
		this.judet = judet;
	}

	public Persoana getPersoane() {
		return persoane;
	}

	public Societate getSocietati() {
		return societati;
	}

	public void setPersoane(Persoana persoane) {
		this.persoane = persoane;
	}

	public void setSocietati(Societate societati) {
		this.societati = societati;
	}

	public boolean isCapitala() {
		if(judet == null || judet.isEmpty()) return false;
		return judet.substring(0, 2).compareTo("SE") == 0;
	}

	@Override
	public String toString() {
		String j = judet == null ? "" : judet + ' ';
		String l = localitate == null ? "" : localitate + ' ';
		String a = adresa == null ? "" : adresa;
		return j + l + a;
	}

	public String toString(String format) {
		String j = judet == null ? "" : judet;
		String l = localitate == null ? "" : localitate;
		String a = adresa == null ? "" : adresa;
		if (format == null || !format.isEmpty() && format.length() != 3)
			return j + l + a;

		switch (format) {
		case "ajl":
			return a +", "+ j +", "+ l;
		case "alj":
			return a +", "+ l +", "+ j;
		case "jal":
			return j +", "+ a +", "+ l;
		case "jla":
			return j +", "+ l +", "+ a;
		case "laj":
			return l +", "+ a +", "+ j;
		case "lja":
			return l +", "+ j +", "+ a;
		default:
			return j +", "+ l +", "+ a;
		}
	}
}
