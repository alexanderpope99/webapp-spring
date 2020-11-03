package net.guides.springboot2.crud.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "societate")
public class Societate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "nume")
	private String nume;

	@Column(name = "idcaen")
	private Long idcaen;

	@Column(name = "cif")
	private String cif;

	@Column(name = "capsoc")
	private Double capsoc;

	@Column(name = "regcom")
	private String regcom;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "idadresa", referencedColumnName = "id")
	private Adresa idadresa;

	@Column(name = "email")
	private String email;

	@Column(name = "telefon")
	private String telefon;

	@OneToMany(mappedBy = "idsocietate")
	private Set<Angajat> angajat;

	@OneToMany(mappedBy = "idsocietate")
	private Set<CentruCost> centruCost;

	@OneToMany(mappedBy = "societate")
	private Set<CereriConcediu> cereriConcediu;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "listacontbancar", joinColumns = @JoinColumn(name = "idsocietate"), inverseJoinColumns = @JoinColumn(name = "iban"))
	private Set<ContBancar> iban = new HashSet<>();

	@OneToMany(mappedBy = "idsocietate")
	private Set<Departament> departamente;

	@OneToOne(mappedBy = "idsocietate", fetch = FetchType.LAZY)
	private PunctDeLucru punctDeLucru;

	public Societate() {
	}

	public Societate(String nume, Long idcaen, String cif, Double capsoc, String regcom, Adresa idadresa, String email,
			String telefon) {
		this.nume = nume;
		this.cif = cif;
		this.capsoc = capsoc;
		this.regcom = regcom;
		this.idadresa = idadresa;
		this.email = email;
		this.telefon = telefon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// GETTERS
	public Double getCapsoc() {
		return capsoc;
	}

	public String getCif() {
		return cif;
	}

	public String getEmail() {
		return email;
	}

	public Adresa getIdadresa() {
		return idadresa;
	}

	public Long getIdcaen() {
		return idcaen;
	}

	public String getNume() {
		return nume;
	}

	public String getRegcom() {
		return regcom;
	}

	public String getTelefon() {
		return telefon;
	}

	// SETTERS
	public void setCapsoc(Double capsoc) {
		this.capsoc = capsoc;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setIdadresa(Adresa idadresa) {
		this.idadresa = idadresa;
	}

	public void setIdcaen(Long idcaen) {
		this.idcaen = idcaen;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public void setRegcom(String regcom) {
		this.regcom = regcom;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public Set<Angajat> getAngajat() {
		return angajat;
	}

	public void setAngajat(Set<Angajat> angajat) {
		this.angajat = angajat;
	}

	public Set<CentruCost> getCentruCost() {
		return centruCost;
	}

	public void setCentruCost(Set<CentruCost> centruCost) {
		this.centruCost = centruCost;
	}

	public Set<CereriConcediu> getCereriConcediu() {
		return cereriConcediu;
	}

	public void setCereriConcediu(Set<CereriConcediu> cereriConcediu) {
		this.cereriConcediu = cereriConcediu;
	}

	public Set<ContBancar> getIban() {
		return iban;
	}

	public void setIban(Set<ContBancar> iban) {
		this.iban = iban;
	}

	public Set<Departament> getDepartamente() {
		return departamente;
	}

	public void setDepartamente(Set<Departament> departamente) {
		this.departamente = departamente;
	}

	public PunctDeLucru getPunctDeLucru() {
		return punctDeLucru;
	}

	public void setPunctDeLucru(PunctDeLucru punctDeLucru) {
		this.punctDeLucru = punctDeLucru;
	}
}
