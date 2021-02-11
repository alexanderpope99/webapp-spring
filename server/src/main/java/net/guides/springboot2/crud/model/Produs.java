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
	private Float pretunitar;

	@Column(name = "valoarefaratva")
	private Float valoarefaratva;

	@Column(name = "valoaretva")
	private Float valoaretva;

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
		return pretunitar;
	}

	public void setPretunitar(Float pretunitar) {
		this.pretunitar = pretunitar;
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
		return valoarefaratva;
	}

	public void setValoarefaratva(Float valoarefaratva) {
		this.valoarefaratva = valoarefaratva;
	}

	public Float getValoaretva() {
		return valoaretva;
	}

	public void setValoaretva(Float valoaretva) {
		this.valoaretva = valoaretva;
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
