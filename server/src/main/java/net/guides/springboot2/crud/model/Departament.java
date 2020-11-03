package net.guides.springboot2.crud.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "departament")
public class Departament {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "idadresa", referencedColumnName = "id")
	private Adresa idadresa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idsocietate")
	private Societate idsocietate;

	@Column(name = "nume")
	private String nume;

	@OneToMany(mappedBy = "iddepartament")
	private Set<Echipa> echipe;

	public Departament() {

	}

	public Departament(Adresa idadresa, Societate idsocietate, String nume) {
		this.idadresa = idadresa;
		this.idsocietate = idsocietate;
		this.nume = nume;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Adresa getIdadresa() {
		return idadresa;
	}

	public void setIdadresa(Adresa idadresa) {
		this.idadresa = idadresa;
	}

	public Societate getIdsocietate() {
		return idsocietate;
	}

	public void setIdsocietate(Societate idsocietate) {
		this.idsocietate = idsocietate;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public Set<Echipa> getEchipe() {
		return echipe;
	}

	public void setEchipe(Set<Echipa> echipe) {
		this.echipe = echipe;
	}
}
