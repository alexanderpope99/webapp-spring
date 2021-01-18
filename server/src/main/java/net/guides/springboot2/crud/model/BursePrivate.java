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

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "burseprivate")
public class BursePrivate  implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract")
	private Contract contract;

	@Column(name = "data")
	private LocalDate data;

	@Column(name = "cota")
	private Float cota;

	@Column(name = "suma")
	private Float suma;

	public BursePrivate() {

	}

	public BursePrivate(Contract contract, LocalDate data, Float cota, Float suma) {
		this.contract = contract;
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

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
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
