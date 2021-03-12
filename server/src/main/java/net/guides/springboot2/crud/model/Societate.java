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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;

@Entity
@Table(name = "societate")
public class Societate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "nume", nullable = false)
	private String nume;

	@Column(name = "idcaen")
	private Integer idcaen;

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

	@Column(name = "fax")
	private String fax;

	@OneToMany(mappedBy = "societate", cascade = CascadeType.ALL)
	private List<ContBancar> contbancar;

	@JsonBackReference(value = "angajat-societate")
	@OneToMany(mappedBy = "societate", cascade = CascadeType.ALL)
	private List<Angajat> angajati;

	@JsonBackReference(value = "factura-societate")
	@OneToMany(mappedBy = "societate", cascade = CascadeType.ALL)
	private List<FacturaOld> facturi;

	@JsonBackReference(value = "centrucost-societate")
	@OneToMany(mappedBy = "societate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CentruCost> centreCost;

	@JsonBackReference(value = "user-societate")
	@ManyToMany(mappedBy = "societati", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<User> useri;

	@JsonBackReference(value = "client-societate")
	@OneToMany(mappedBy = "societate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Client> clienti;

	@JsonBackReference(value = "activitate-societate")
	@OneToMany(mappedBy = "societate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Activitate> activitati;

	@JsonBackReference(value = "caiete-societate")
	@OneToMany(mappedBy = "societate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Caiet> caiete;

	@JsonBackReference(value = "imagine-societate")
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
	@JoinColumn(name = "idimagine", referencedColumnName = "id")
	private Fisier imagine;

	@OneToMany(mappedBy = "societate",cascade = CascadeType.ALL)
	private List<LunaInchisa> luniInchise;

	public Societate() {
	}

	public Societate(String nume, Integer idcaen, String cif, Double capsoc, String regcom, Adresa adresa, String email, String telefon, String fax) {
		this.nume = nume;
		this.idcaen = idcaen;
		this.cif = cif;
		this.capsoc = capsoc;
		this.regcom = regcom;
		this.adresa = adresa;
		this.email = email;
		this.telefon = telefon;
		this.fax = fax;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// ! GETTERS
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

	public Integer getIdcaen() {
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

	public List<ContBancar> getContbancar() {
		return contbancar;
	}

	public List<Angajat> getAngajati() {
		return angajati;
	}

	public List<CentruCost> getCentreCost() {
		return centreCost;
	}

	public List<FacturaOld> getFacturi() {
		return facturi;
	}

	public List<User> getUseri() {
		return useri;
	}

	public List<Caiet> getCaiete() {
		return caiete;
	}

	// ! SETTERS
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

	public void setIdcaen(Integer idcaen) {
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

	public void setAngajati(List<Angajat> angajati) {
		this.angajati = angajati;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public void setContbancar(List<ContBancar> contbancar) {
		this.contbancar = contbancar;
	}

	public void setCentreCost(List<CentruCost> centreCost) {
		this.centreCost = centreCost;
	}

	public void setFacturi(List<FacturaOld> facturi) {
		this.facturi = facturi;
	}

	public void setUseri(List<User> useri) {
		this.useri = useri;
	}

	public List<Activitate> getActivitati() {
		return activitati;
	}

	public void setActivitati(List<Activitate> activitati) {
		this.activitati = activitati;
	}

	public List<Client> getClienti() {
		return clienti;
	}

	public void setClienti(List<Client> clienti) {
		this.clienti = clienti;
	}

	public void setCaiete(List<Caiet> caiete) {
		this.caiete = caiete;
	}

	public List<LunaInchisa> getLuniInchise() {
		return luniInchise;
	}

	public void setLuniInchise(List<LunaInchisa> luniInchise) {
		this.luniInchise = luniInchise;
	}

	// ! OTHER

	public void checkData() throws ResourceNotFoundException {
		if (nume == null)
			throw new ResourceNotFoundException("Numele nu poate fi null");
	}

	public void addContBancar(ContBancar contBancar) {
		this.contbancar.add(contBancar);
	}

	public void removeContBancar(ContBancar contBancar) {
		this.contbancar.remove(contBancar);
	}

	public Fisier getImagine() {
		return imagine;
	}

	public void setImagine(Fisier imagine) {
		this.imagine = imagine;
	}

	public Societate update(Societate newSoc) {
		this.nume=newSoc.nume;
		this.idcaen=newSoc.idcaen;
		this.cif=newSoc.cif;
		this.capsoc=newSoc.capsoc;
		this.regcom=newSoc.regcom;
		this.adresa=newSoc.adresa;
		this.email=newSoc.email;
		this.telefon=newSoc.telefon;
		this.fax=newSoc.fax;
		this.fax=newSoc.fax;
		this.imagine=newSoc.imagine;
		return this;
	}
}
