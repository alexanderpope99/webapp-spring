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
	@Column(name = "tara")
	private String tara;

	@JsonBackReference(value = "adresa-societate")
	@OneToOne(mappedBy = "adresa", fetch = FetchType.LAZY)
	private Societate societati;

	@JsonBackReference(value = "adresa-persoana")
	@OneToOne(mappedBy = "adresa", fetch = FetchType.LAZY)
	private Persoana persoane;

	public Adresa() { }

	public Adresa(String adresa, String localitate, String judet, String tara) {
		this.adresa = adresa;
		this.localitate = localitate;
		this.judet = judet;
		this.tara = tara;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getLocalitate() {
		return localitate;
	}

	public void setLocalitate(String localitate) {
		this.localitate = localitate;
	}

	public String getJudet() {
		return judet;
	}

	public void setJudet(String judet) {
		this.judet = judet;
	}

	public String getTara() {
		return tara;
	}

	public void setTara(String tara) {
		this.tara = tara;
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
}
