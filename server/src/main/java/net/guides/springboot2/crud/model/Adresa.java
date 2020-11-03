package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "adresa")
public class Adresa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "adresa")
	private String adresa;

	@Column(name = "localitate")
	private String localitate;

	@Column(name = "judet")
	private String judet;

	@Column(name = "tara")
	private String tara;

	@OneToOne(mappedBy = "idadresa", fetch = FetchType.LAZY)
	private Persoana persoane;

	@OneToOne(mappedBy = "idadresa", fetch = FetchType.LAZY)
	private PunctDeLucru punctDeLucru;

	@OneToOne(mappedBy = "idadresa", fetch = FetchType.LAZY)
	private Societate societate;

	@OneToOne(mappedBy = "idadresa", fetch = FetchType.LAZY)
	private Departament departamente;

	@OneToOne(mappedBy = "idadresa", fetch = FetchType.LAZY)
	private CentruCost centruCost;

	public Adresa() {

	}

	public Adresa(String adresa, String localitate, String judet, String tara) {
		this.adresa = adresa;
		this.localitate = localitate;
		this.judet = judet;
		this.tara = tara;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getLocalitate() {
		return localitate;
	}

	public void setLocalitate(String localitate) {
		this.localitate = localitate;
	}

	public String getJudet() {
		return judet;
	}

	public void setJudet(String judet) {
		this.judet = judet;
	}

	public String getTara() {
		return tara;
	}

	public void setTara(String tara) {
		this.tara = tara;
	}

	public Persoana getPersoane() {
		return persoane;
	}

	public void setPersoane(Persoana persoane) {
		this.persoane = persoane;
	}

	public PunctDeLucru getPunctDeLucru() {
		return punctDeLucru;
	}

	public void setPunctDeLucru(PunctDeLucru punctDeLucru) {
		this.punctDeLucru = punctDeLucru;
	}

	public Societate getSocietate() {
		return societate;
	}

	public void setSocietate(Societate societate) {
		this.societate = societate;
	}

	public Departament getDepartamente() {
		return departamente;
	}

	public void setDepartamente(Departament departamente) {
		this.departamente = departamente;
	}

	public CentruCost getCentruCost() {
		return centruCost;
	}

	public void setCentruCost(CentruCost centruCost) {
		this.centruCost = centruCost;
	}
}
