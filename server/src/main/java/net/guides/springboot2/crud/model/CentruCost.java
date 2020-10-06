package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "centrucost")
public class CentruCost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "idadresa")
	private int idadresa;

	@Column(name = "idsocietate")
	private int idsocietate;

	@Column(name = "nume")
	private String nume;

	public CentruCost() {

	}

	public CentruCost(int idadresa, int idsocietate, String nume) {
		this.idadresa = idadresa;
		this.idsocietate = idsocietate;
		this.nume = nume;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIdadresa() {
		return idadresa;
	}

	public void setIdadresa(int idadresa) {
		this.idadresa = idadresa;
	}

	public int getIdsocietate() {
		return idsocietate;
	}

	public void setIdsocietate(int idsocietate) {
		this.idsocietate = idsocietate;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}
}
