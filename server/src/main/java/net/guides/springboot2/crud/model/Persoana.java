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

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "persoana")
public class Persoana implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "gen")
	private String gen;
	@Column(name = "nume")
	private String nume;
	@Column(name = "prenume")
	private String prenume;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idadresa", referencedColumnName = "id")
	private Adresa adresa;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idactidentitate", referencedColumnName = "id")
	private ActIdentitate actidentitate;

	@Column(name = "starecivila")
	private String starecivila;
	@Column(name = "email")
	private String email;
	@Column(name = "telefon")
	private String telefon;
	@Column(name = "cnp")
	private String cnp;

	@JsonBackReference(value = "angajat-persoana")
	@OneToOne(mappedBy = "persoana", fetch = FetchType.LAZY)
	private Angajat angajat;

	public Persoana() {
	}

	public Persoana(String gen, String nume, String prenume, ActIdentitate idactidentitate, Adresa idadresa,
			String starecivila, String email, String telefon, String cnp) {
		this.gen = gen;
		this.nume = nume;
		this.prenume = prenume;
		this.actidentitate = idactidentitate;
		this.adresa = idadresa;
		this.starecivila = starecivila;
		this.email = email;
		this.telefon = telefon;
		this.cnp = cnp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// ! GETTERS
	public String getNumeIntreg() {
		return nume +' '+ prenume;
	}

	public String getCnp() {
		return cnp;
	}

	public String getEmail() {
		return email;
	}

	public String getGen() {
		return gen;
	}

	public ActIdentitate getActidentitate() {
		return actidentitate;
	}

	public Adresa getAdresa() {
		return adresa;
	}

	public String getNume() {
		return nume;
	}

	public String getPrenume() {
		return prenume;
	}

	public String getStarecivila() {
		return starecivila;
	}

	public String getTelefon() {
		return telefon;
	}

	public Angajat getAngajat() {
		return angajat;
	}

	// ! SETTERS
	public void setCnp(String cnp) {
		this.cnp = cnp;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGen(String gen) {
		this.gen = gen;
	}

	public void setActidentitate(ActIdentitate actidentitate) {
		this.actidentitate = actidentitate;
	}

	public void setAdresa(Adresa adresa) {
		this.adresa = adresa;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}

	public void setStarecivila(String starecivila) {
		this.starecivila = starecivila;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public void setAngajat(Angajat angajat) {
		this.angajat = angajat;
	}
}
