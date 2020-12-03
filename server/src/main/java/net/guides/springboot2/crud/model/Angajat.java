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

	@JsonBackReference(value = "user-angajat")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "iduser")
	private User user;

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

	public Angajat getSuperior() {
		return superior;
	}
	public void setSuperior(Angajat superior) {
		this.superior = superior;
	}

	public List<Bazacalcul> getBazaCalcul() {
		return bazaCalcul;
	}
	public List<CereriConcediu> getCereriConcediu() {
		return cereriConcediu;
	}
	public List<Factura> getFacturi() {
		return facturi;
	}
	public int getIdpersoana() {
		return idpersoana;
	}
	public List<PersoanaIntretinere> getPersoaneintretinere() {
		return persoaneintretinere;
	}
	public List<Angajat> getSubalterni() {
		return subalterni;
	}
	public User getUser() {
		return user;
	}

	public void setBazaCalcul(List<Bazacalcul> bazaCalcul) {
		this.bazaCalcul = bazaCalcul;
	}
	public void setCereriConcediu(List<CereriConcediu> cereriConcediu) {
		this.cereriConcediu = cereriConcediu;
	}
	public void setFacturi(List<Factura> facturi) {
		this.facturi = facturi;
	}
	public void setIdpersoana(int idpersoana) {
		this.idpersoana = idpersoana;
	}
	public void setPersoaneintretinere(List<PersoanaIntretinere> persoaneintretinere) {
		this.persoaneintretinere = persoaneintretinere;
	}
	public void setSubalterni(List<Angajat> subalterni) {
		this.subalterni = subalterni;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
