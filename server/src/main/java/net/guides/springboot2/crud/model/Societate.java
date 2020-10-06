package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "societate")
public class Societate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

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

	@Column(name = "idadresa")
	private Long idadresa;

	@Column(name = "email")
	private String email;

	@Column(name = "telefon")
	private String telefon;

	public Societate() {
	}

	public Societate(String nume, Long idcaen, String cif, Double capsoc, String regcom, Long idadresa, String email,
			String telefon) {
		this.nume = nume;
		this.cif = cif;
		this.capsoc = capsoc;
		this.regcom = regcom;
		this.idadresa = idadresa;
		this.email = email;
		this.telefon = telefon;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Long getIdadresa() {
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

	public void setIdadresa(Long idadresa) {
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
}
