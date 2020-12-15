package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "centrucost")
public class CentruCost implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idadresa", referencedColumnName = "id")
	private Adresa adresa;

	@JsonBackReference(value = "societate-centrucost")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idsocietate", referencedColumnName = "id")
	private Societate societate;

	@Column(name = "nume")
	private String nume;

	@JsonBackReference(value = "factura-centrucost")
	@OneToMany(fetch = FetchType.LAZY)
	private List<Factura> facturi;

	public CentruCost() {

	}

	public CentruCost(Adresa adresa, Societate societate, String nume) {
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

	public Adresa getAdresa() {
		return adresa;
	}

	public void setAdresa(Adresa adresa) {
		this.adresa = adresa;
	}

	public Societate getSocietate() {
		return societate;
	}

	public void setSocietate(Societate societate) {
		this.societate = societate;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public List<Factura> getFacturi() {
		return facturi;
	}

	public void setFacturi(List<Factura> facturi) {
		this.facturi = facturi;
	}
}
