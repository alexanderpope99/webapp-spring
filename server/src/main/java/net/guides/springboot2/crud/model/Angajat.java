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

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idpersoana", referencedColumnName = "id")
	@MapsId
	private Persoana persoana;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idcontract", referencedColumnName = "id")
	private Contract contract;

	@ManyToOne
	@JoinColumn(name = "idsocietate")
	private Societate societate;

	@ManyToOne
	@JoinColumn(name = "idsuperior")
	private Angajat superior;

	@JsonBackReference(value = "factura-aprobator")
	@OneToMany(mappedBy = "aprobator", cascade = CascadeType.ALL)
	private List<Factura> facturi;

	@JsonBackReference(value = "angajat-angajat")
	@OneToMany(mappedBy = "superior")
	private List<Angajat> subalterni;

	@JsonBackReference(value = "cerereconcediu-angajat")
	@OneToMany(mappedBy = "pentru", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CereriConcediu> cereriConcediu;

	@JsonBackReference(value = "persoanaintretinere-angajat")
	@OneToMany(mappedBy = "angajat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<PersoanaIntretinere> persoaneintretinere;

	@JsonBackReference(value = "bazacalcul-angajat")
	@OneToMany(mappedBy = "angajat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Bazacalcul> bazaCalcul;

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

	public Persoana getPersoana() {
		return persoana;
	}

	public void setPersoana(Persoana persoana) {
		this.persoana = persoana;
	}

	public List<PersoanaIntretinere> getPersoaneIntretinere() {
		return persoaneintretinere;
	}
}
