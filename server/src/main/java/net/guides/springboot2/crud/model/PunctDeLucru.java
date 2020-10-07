package net.guides.springboot2.crud.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "punctdelucru")
public class PunctDeLucru {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idadresa", referencedColumnName = "id")
	private Adresa idadresa;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idsocietate", referencedColumnName = "id")
	private Societate idsocietate;

	@Column(name = "nume")
	private String nume;

	@OneToMany(mappedBy = "idpunctlucru")
	private Set<Contract> contracte;

	public PunctDeLucru() {
	}

	public PunctDeLucru(Adresa idadresa, Societate idsocietate, String nume) {
		this.idadresa = idadresa;
		this.idsocietate = idsocietate;
		this.nume = nume;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// GETTERS
	public Adresa getIdadresa() {
		return idadresa;
	}

	public Societate getIdsocietate() {
		return idsocietate;
	}

	public String getNume() {
		return nume;
	}

	// SETTERS
	public void setIdadresa(Adresa idadresa) {
		this.idadresa = idadresa;
	}

	public void setIdsocietate(Societate idsocietate) {
		this.idsocietate = idsocietate;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public Set<Contract> getContracte() {
		return contracte;
	}

	public void setContracte(Set<Contract> contracte) {
		this.contracte = contracte;
	}
}
