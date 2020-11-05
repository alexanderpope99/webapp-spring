package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name = "societate")
public class Societate implements Serializable {
	private static final long serialVersionUID = 1L;

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

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idadresa", referencedColumnName = "id")
	private Adresa adresa;

	@Column(name = "email")
	private String email;

	@Column(name = "telefon")
	private String telefon;

	// @ManyToMany(fetch = FetchType.LAZY)
	// @JoinTable(name = "listacontbancar", joinColumns = @JoinColumn(name = "idsocietate"), inverseJoinColumns = @JoinColumn(name = "iban"))
	// private Set<ContBancar> iban = new HashSet<>();

	@JsonBackReference(value = "angajat-societate")
	@OneToMany(mappedBy = "societate")
	private List<Angajat> angajat;

	// @OneToMany(mappedBy = "societate")
	// private Set<CentruCost> centruCost;

	// @OneToMany(mappedBy = "societate")
	// private Set<CereriConcediu> cereriConcediu;

	// @OneToMany(mappedBy = "societate")
	// private Set<Departament> departamente;

	// @OneToOne(mappedBy = "societate")
	// private PunctDeLucru punctDeLucru;

	public Societate() {
	}

	public Societate(String nume, Long idcaen, String cif, Double capsoc, String regcom, Adresa adresa, String email,
			String telefon) {
		this.nume = nume;
		this.cif = cif;
		this.capsoc = capsoc;
		this.regcom = regcom;
		this.adresa = adresa;
		this.email = email;
		this.telefon = telefon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//! GETTERS
	public Double getCapsoc() {
		return capsoc;
	}

	public String getCif() {
		return cif;
	}

	public String getEmail() {
		return email;
	}

	public Adresa getAdresa() {
		return adresa;
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

	public List<Angajat> getAngajat() {
		return angajat;
	}

	//! SETTERS
	public void setCapsoc(Double capsoc) {
		this.capsoc = capsoc;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAdresa(Adresa adresa) {
		this.adresa = adresa;
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

	public void setAngajat(List<Angajat> angajat) {
		this.angajat = angajat;
	}
}
