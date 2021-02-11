package net.guides.springboot2.crud.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "factura")
public class Factura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "serie")
	private String serie;

	@Column(name = "numar")
	private String numar;

	@Column(name = "nravizinsotire")
	private String nravizinsotire;

	@Column(name = "titlu")
	private String titlu;

	@Column(name = "dataexpedierii")
	private LocalDate dataexpedierii;

	@Column(name = "oraexpedierii")
	private String oraexpedierii;

	@Column(name = "totalfaratva")
	private Float totalFaraTva;

	@Column(name = "tva")
	private Float tva;

	@ManyToOne
	@JoinColumn(name = "idclient", nullable = false)
	private Client client;

	@JsonBackReference(value = "produs-factura")
	@OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
	private List<Produs> produse;

	@JsonBackReference(value = "factura-proiect")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "factura_proiect", joinColumns = @JoinColumn(name = "factura_id"), inverseJoinColumns = @JoinColumn(name = "proiect_id"))
	private List<Proiect> proiecte;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getNumar() {
		return numar;
	}

	public void setNumar(String numar) {
		this.numar = numar;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public LocalDate getDataexpedierii() {
		return dataexpedierii;
	}

	public void setDataexpedierii(LocalDate dataexpedierii) {
		this.dataexpedierii = dataexpedierii;
	}

	public String getNravizinsotire() {
		return nravizinsotire;
	}

	public void setNravizinsotire(String nravizinsotire) {
		this.nravizinsotire = nravizinsotire;
	}

	public String getOraexpedierii() {
		return oraexpedierii;
	}

	public void setOraexpedierii(String oraexpedierii) {
		this.oraexpedierii = oraexpedierii;
	}

	public List<Proiect> getProiecte() {
		return proiecte;
	}

	public void setProiecte(List<Proiect> proiecte) {
		this.proiecte = proiecte;
	}

	public String getTitlu() {
		return titlu;
	}

	public void setTitlu(String titlu) {
		this.titlu = titlu;
	}

	public Float getTotalFaraTva() {
		return totalFaraTva;
	}

	public void setTotalFaraTva(Float totalFaraTva) {
		this.totalFaraTva = totalFaraTva;
	}

	public Float getTva() {
		return tva;
	}

	public void setTva(Float tva) {
		this.tva = tva;
	}

	public Float getTotal(){
		return totalFaraTva+tva;
	}

	public List<Produs> getProduse() {
		return produse;
	}

	public void setProduse(List<Produs> produse) {
		this.produse = produse;
	}

}
