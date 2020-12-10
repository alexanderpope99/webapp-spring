package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "factura")
public class Factura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "denumirefurnizor")
	private String denumirefurnizor;

	@Column(name = "ciffurnizor")
	private String ciffurnizor;

	@Column(name = "nr")
	private String nr;

	@Column(name = "moneda")
	private String moneda;

	@Column(name = "sumafaratva")
	private double sumafaratva;

	@Column(name = "tipachizitie")
	private String tipachizitie;

	@Column(name = "descriereactivitati")
	private String descriereactivitati;

	@Column(name = "observatii")
	private String observatii;

	@Column(name = "codproiect")
	private String codproiect;

	@Column(name = "status")
	private String status;

	@Column(name = "data")
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = ISO.DATE)
	private LocalDate data;

	@Column(name = "termenscadenta")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate termenscadenta;

	@Column(name = "dataplatii")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataplatii;

	@Column(name = "sumaachitata")
	private double sumaachitata;

	@Column(name = "numefisier")
	private String numefisier;

	@Column(name = "dimensiunefisier")
	private Long dimensiunefisier;

	@JsonIgnore
	@Lob
	@Column(name = "fisier")
	private byte[] fisier;

	@ManyToOne
	@JoinColumn(name = "idaprobator", referencedColumnName = "idpersoana")
	private Angajat aprobator;

	@ManyToOne
	@JoinColumn(name = "idcentrucost", referencedColumnName = "id")
	private CentruCost centrucost;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "idsocietate", referencedColumnName = "id")
	private Societate societate;

	public Factura() {

	}

	public Factura(String denumirefurnizor, String ciffurnizor, String nr, LocalDate data, String moneda,
			double sumafaratva, LocalDate termenscadenta, String tipachizitie, String descriereactivitati,
			Angajat aprobator, String observatii, CentruCost centrucost, LocalDate dataplatii,
			double sumaachitata, Societate societate, String codproiect) {
		this.denumirefurnizor = denumirefurnizor;
		this.ciffurnizor = ciffurnizor;
		this.nr = nr;
		this.data = data;
		this.moneda = moneda;
		this.sumafaratva = sumafaratva;
		this.termenscadenta = termenscadenta;
		this.tipachizitie = tipachizitie;
		this.descriereactivitati = descriereactivitati;
		this.aprobator = aprobator;
		this.observatii = observatii;
		this.codproiect = codproiect;
		this.centrucost = centrucost;
		this.dataplatii = dataplatii;
		this.sumaachitata = sumaachitata;
		this.societate = societate;
	}

	public Angajat getAprobator() {
		return aprobator;
	}

	public void setAprobator(Angajat aprobator) {
		this.aprobator = aprobator;
	}

	public CentruCost getCentrucost() {
		return centrucost;
	}

	public void setCentrucost(CentruCost centrucost) {
		this.centrucost = centrucost;
	}

	public String getCiffurnizor() {
		return ciffurnizor;
	}

	public void setCiffurnizor(String ciffurnizor) {
		this.ciffurnizor = ciffurnizor;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalDate getDataplatii() {
		return dataplatii;
	}

	public void setDataplatii(LocalDate dataplatii) {
		this.dataplatii = dataplatii;
	}

	public String getDenumirefurnizor() {
		return denumirefurnizor;
	}

	public void setDenumirefurnizor(String denumirefurnizor) {
		this.denumirefurnizor = denumirefurnizor;
	}

	public String getDescriereactivitati() {
		return descriereactivitati;
	}

	public void setDescriereactivitati(String descriereactivitati) {
		this.descriereactivitati = descriereactivitati;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	public String getObservatii() {
		return observatii;
	}

	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}

	public double getSumaachitata() {
		return sumaachitata;
	}

	public void setSumaachitata(double sumaachitata) {
		this.sumaachitata = sumaachitata;
	}

	public double getSumafaratva() {
		return sumafaratva;
	}

	public void setSumafaratva(double sumafaratva) {
		this.sumafaratva = sumafaratva;
	}

	public LocalDate getTermenscadenta() {
		return termenscadenta;
	}

	public void setTermenscadenta(LocalDate termenscadenta) {
		this.termenscadenta = termenscadenta;
	}

	public String getTipachizitie() {
		return tipachizitie;
	}

	public void setTipachizitie(String tipachizitie) {
		this.tipachizitie = tipachizitie;
	}

	public Societate getSocietate() {
		return societate;
	}

	public void setSocietate(Societate societate) {
		this.societate = societate;
	}

	public byte[] getFisier() {
		return fisier;
	}

	public String getNumefisier() {
		return numefisier;
	}

	public void setFisier(byte[] fisier) {
		this.fisier = fisier;
	}

	public void setNumefisier(String numefisier) {
		this.numefisier = numefisier;
	}

	public Long getDimensiunefisier() {
		return dimensiunefisier;
	}

	public void setDimensiunefisier(Long dimensiunefisier) {
		this.dimensiunefisier = dimensiunefisier;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCodproiect() {
		return codproiect;
	}

	public void setCodproiect(String codproiect) {
		this.codproiect = codproiect;
	}

}
