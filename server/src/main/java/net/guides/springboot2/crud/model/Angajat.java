package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "angajat")
public class Angajat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idpersoana;

	// @JsonManagedReference(value = "angajat-persoana")
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idpersoana", referencedColumnName = "id")
	@MapsId
	private Persoana persoana;

	// @JsonManagedReference(value = "angajat-contract")
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idcontract", referencedColumnName = "id")
	private Contract contract;

	// @JsonManagedReference(value = "angajat-societate")
	@ManyToOne
	@JoinColumn(name = "idsocietate")
	private Societate societate;

	// @JsonManagedReference(value = "angajat-angajat")
	@ManyToOne
	@JoinColumn(name = "idsuperior")
	private Angajat superior;

	// @OneToMany(mappedBy = "superior")
	// private Set<Angajat> subalterni = new HashSet<Angajat>();

	@JsonBackReference(value = "cerereconcediu-angajat")
	@OneToMany(mappedBy = "pentru", fetch = FetchType.LAZY)
	private List<CereriConcediu> cereriConcediu;

	@JsonBackReference(value = "persoanaintretinere-angajat")
	@OneToMany(mappedBy = "angajat", fetch = FetchType.LAZY)
	private List<PersoanaIntretinere> persoaneIntretinere;

	@JsonBackReference(value = "bazacalcul-angajat")
	@OneToMany(mappedBy = "angajat", fetch = FetchType.LAZY)
	private List<Bazacalcul> bazaCalcul;

	public Angajat() {}

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

	public Persoana getPersoana() {
		return persoana;
	}

	public void setPersoana(Persoana persoana) {
		this.persoana = persoana;
	}

	public List<PersoanaIntretinere> getPersoaneIntretinere() {
		return persoaneIntretinere;
	}
}
