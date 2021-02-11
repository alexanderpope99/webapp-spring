package net.guides.springboot2.crud.model;

import javax.persistence.CascadeType;
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
	private int id;

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

	@JsonBackReference(value = "produs-factura")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "idfactura", referencedColumnName = "id")
	private Factura factura;


	public Produs() {
	}

	public Produs(String denumire, String um, int cantitate, Float pretUnitar, Float valoareFaraTva, Float tva, Factura factura) {
		this.denumire = denumire;
		this.um = um;
		this.cantitate = cantitate;
		this.pretUnitar = pretUnitar;
		this.valoareFaraTva = valoareFaraTva;
		this.tva = tva;
		this.factura = factura;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDenumire() {
		return this.denumire;
	}

	public void setDenumire(String denumire) {
		this.denumire = denumire;
	}

	public String getUm() {
		return this.um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	public int getCantitate() {
		return this.cantitate;
	}

	public void setCantitate(int cantitate) {
		this.cantitate = cantitate;
	}

	public Float getPretUnitar() {
		return this.pretUnitar;
	}

	public void setPretUnitar(Float pretUnitar) {
		this.pretUnitar = pretUnitar;
	}

	public Float getValoareFaraTva() {
		return this.valoareFaraTva;
	}

	public void setValoareFaraTva(Float valoareFaraTva) {
		this.valoareFaraTva = valoareFaraTva;
	}

	public Float getTva() {
		return this.tva;
	}

	public void setTva(Float tva) {
		this.tva = tva;
	}

	public Factura getFactura() {
		return this.factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	
	
}
