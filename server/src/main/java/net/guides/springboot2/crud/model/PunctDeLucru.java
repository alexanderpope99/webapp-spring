package net.guides.springboot2.crud.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "punctdelucru")
public class PunctDeLucru implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "idadresa", referencedColumnName = "id")
	private Adresa adresa;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "idsocietate", referencedColumnName = "id")
	private Societate societate;

	@Column(name = "nume")
	private String nume;

	// @OneToMany(mappedBy = "punctdelucru")
	// private Set<Contract> contracte;

	public PunctDeLucru() {
	}

	public PunctDeLucru(Adresa adresa, Societate societate, String nume) {
		this.adresa = adresa;
		this.societate = societate;
		this.nume = nume;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// GETTERS
	public Adresa getAdresa() {
		return adresa;
	}

	public Societate getSocietate() {
		return societate;
	}

	public String getNume() {
		return nume;
	}

	// SETTERS
	public void setAdresa(Adresa adresa) {
		this.adresa = adresa;
	}

	public void setSocietate(Societate societate) {
		this.societate = societate;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}
}
