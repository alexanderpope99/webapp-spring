package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;

@Entity
@Table(name = "produs")
public class Produs implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "denumire")
	private String denumire;

	@Column(name = "um")
	private String um;

	@Column(name = "cantitate")
	private int cantitate;

	@Column(name = "pretunitar")
	private Float pretUnitar;

	@Column(name = "valoarefaratva")
	private Float valoareFaraTva;

	@Column(name = "valoaretva")
	private Float tva;

	@ManyToOne
	@JoinColumn(name = "idproiect", nullable = false)
	private Proiect proiect;

	@JsonBackReference(value = "factura-produse")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idfactura", referencedColumnName = "id")
	private Factura factura;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDenumire() {
		return denumire;
	}

	public void setDenumire(String denumire) {
		this.denumire = denumire;
	}

	public Float getPretunitar() {
		return pretUnitar;
	}

	public void setPretunitar(Float pretunitar) {
		this.pretUnitar = pretunitar;
	}

	public Proiect getProiect() {
		return proiect;
	}

	public void setProiect(Proiect proiect) {
		this.proiect = proiect;
	}

	public String getUm() {
		return um;
	}
	public void setUm(String um) {
		this.um = um;
	}

	public Float getValoarefaratva() {
		return valoareFaraTva;
	}

	public void setValoarefaratva(Float valoarefaratva) {
		this.valoareFaraTva = valoarefaratva;
	}

	public Float getTva() {
		return tva;
	}

	public void setTva(Float valoaretva) {
		this.tva = valoaretva;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public int getCantitate() {
		return cantitate;
	}

	public void setCantitate(int cantitate) {
		this.cantitate = cantitate;
	}
	
}
