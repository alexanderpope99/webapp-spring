package net.guides.springboot2.crud.model;

import java.io.Serializable;
// import java.util.HashSet;
// import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
// import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "angajat")
public class Angajat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idpersoana;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idpersoana", referencedColumnName = "id")
	@MapsId
	private Persoana persoana;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract", referencedColumnName = "id")
	private Contract contract;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idsocietate")
	private Societate societate;

	@ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "idsuperior")
	private Angajat superior;

	// @OneToMany(mappedBy = "superior")
	// private Set<Angajat> subalterni = new HashSet<Angajat>();

	// @OneToMany(mappedBy = "pentru")
	// private Set<CereriConcediu> cereriConcediu;

	// @OneToMany(mappedBy = "angajat")
	// private Set<PersoanaIntretinere> persoaneIntretinere;

	// @OneToMany(mappedBy = "angajat")
	// private Set<Bazacalcul> bazaCalcul;

	public Angajat() {

	}

	public Angajat(Contract contract, Societate societate) {
		this.contract = contract;
		this.societate = societate;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Societate getSocietate() {
		return societate;
	}

	public void setSocietate(Societate societate) {
		this.societate = societate;
	}

	public Angajat getSuperior() {
		return superior;
	}

	public void setSuperior(Angajat superior) {
		this.superior = superior;
	}

	public Persoana getPersoana() {
		return persoana;
	}

	public void setPersoana(Persoana persoana) {
		this.persoana = persoana;
	}
}
