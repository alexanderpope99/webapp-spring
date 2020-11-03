package net.guides.springboot2.crud.model;

import java.util.HashSet;
import java.util.Set;

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

@Entity
@Table(name = "angajat")
public class Angajat {

	@Id
	private int idpersoana;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract", referencedColumnName = "id")
	private Contract idcontract;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idsocietate")
	private Societate idsocietate;

	@ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "idsuperior")
	private Angajat superior;

	@OneToMany(mappedBy = "superior")
	private Set<Angajat> subalterni = new HashSet<Angajat>();

	@OneToMany(mappedBy = "pentru")
	private Set<CereriConcediu> cereriConcediu;

	@OneToMany(mappedBy = "idangajat")
	private Set<PersoanaIntretinere> persoaneIntretinere;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idpersoana")
	@MapsId
	private Persoana persoana;

	@OneToMany(mappedBy = "idangajat")
	private Set<Bazacalcul> bazaCalcul;

	public Angajat() {

	}

	public Angajat(Contract idcontract, Societate idsocietate) {
		this.idcontract = idcontract;
		this.idsocietate = idsocietate;
	}

	public int getIdpersoana() {
		return idpersoana;
	}

	public void setIdpersoana(int idpersoana) {
		this.idpersoana = idpersoana;
	}

	public Contract getIdcontract() {
		return idcontract;
	}

	public void setIdcontract(Contract idcontract) {
		this.idcontract = idcontract;
	}

	public Societate getIdsocietate() {
		return idsocietate;
	}

	public void setIdsocietate(Societate idsocietate) {
		this.idsocietate = idsocietate;
	}

	public Set<Angajat> getSubalterni() {
		return subalterni;
	}

	public void setSubalterni(Set<Angajat> subalterni) {
		this.subalterni = subalterni;
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

	public Set<CereriConcediu> getCereriConcediu() {
		return cereriConcediu;
	}

	public void setCereriConcediu(Set<CereriConcediu> cereriConcediu) {
		this.cereriConcediu = cereriConcediu;
	}

	public Set<PersoanaIntretinere> getPersoaneIntretinere() {
		return persoaneIntretinere;
	}

	public void setPersoaneIntretinere(Set<PersoanaIntretinere> persoaneIntretinere) {
		this.persoaneIntretinere = persoaneIntretinere;
	}

	public Set<Bazacalcul> getBazaCalcul() {
		return bazaCalcul;
	}

	public void setBazaCalcul(Set<Bazacalcul> bazaCalcul) {
		this.bazaCalcul = bazaCalcul;
	}

}
