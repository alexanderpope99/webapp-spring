package net.guides.springboot2.crud.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
	private Float totalfaratva;

	@Column(name = "tva")
	private Float tva;

	@ManyToOne
	@JoinColumn(name = "idclient")
	private Client client;

	@OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
	private List<Produs> produse;

	@ManyToOne
	@JoinColumn(name = "idproiect")
	private Proiect proiect;

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

	public Proiect getProiect() {
		return proiect;
	}

	public void setProiect(Proiect proiect) {
		this.proiect = proiect;
	}

	public String getTitlu() {
		return titlu;
	}

	public void setTitlu(String titlu) {
		this.titlu = titlu;
	}

	public Float getTotalfaratva() {
		return totalfaratva;
	}

	public void setTotalfaratva(Float totalfaratva) {
		this.totalfaratva = totalfaratva;
	}

	public Float getTva() {
		return tva;
	}

	public void setTva(Float tva) {
		this.tva = tva;
	}

	public Float getTotalcutva() {
		return totalfaratva + tva;
	}

	public List<Produs> getProduse() {
		return produse;
	}

	public void setProduse(List<Produs> produse) {
		this.produse = produse;
	}

	public Factura update(Factura newFactura) {
		this.serie = newFactura.serie;
		this.numar = newFactura.numar;
		this.nravizinsotire = newFactura.nravizinsotire;
		this.client = newFactura.client;
		this.titlu = newFactura.titlu;
		this.produse = newFactura.produse;
		this.dataexpedierii = newFactura.dataexpedierii;
		this.oraexpedierii = newFactura.oraexpedierii;
		this.totalfaratva = newFactura.totalfaratva;
		this.tva = newFactura.tva;

		return this;
	}
}