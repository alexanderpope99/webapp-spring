package net.guides.springboot2.crud.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "centrucost")
public class CentruCost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idadresa", referencedColumnName = "id")
	private Adresa idadresa;

	@ManyToOne
	@JoinColumn(name = "idsocietate")
	private Societate idsocietate;

	@Column(name = "nume")
	private String nume;

	@OneToMany(mappedBy = "idcentrucost")
	private Set<Contract> contracte;

	public CentruCost() {

	}

	public CentruCost(Adresa idadresa, Societate idsocietate, String nume) {
		this.idadresa = idadresa;
		this.idsocietate = idsocietate;
		this.nume = nume;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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
}
