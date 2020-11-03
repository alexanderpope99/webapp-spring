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

import java.time.LocalDate;

@Entity
@Table(name = "burseprivate")
public class BursePrivate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract")
	private Contract idcontract;

	@Column(name = "data")
	private LocalDate data;

	@Column(name = "cota")
	private Float cota;

	@Column(name = "suma")
	private Float suma;

	public BursePrivate() {

	}

	public BursePrivate(Contract idcontract, LocalDate data, Float cota, Float suma) {
		this.idcontract = idcontract;
		this.data = data;
		this.cota = cota;
		this.suma = suma;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Contract getIdcontract() {
		return idcontract;
	}

	public void setIdcontract(Contract idcontract) {
		this.idcontract = idcontract;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Float getCota() {
		return cota;
	}

	public void setCota(Float cota) {
		this.cota = cota;
	}

	public Float getSuma() {
		return suma;
	}

	public void setSuma(Float suma) {
		this.suma = suma;
	}
}
