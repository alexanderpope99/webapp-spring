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

@Entity
@Table(name = "bazacalcul")
public class Bazacalcul {
	public Bazacalcul() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "luna")
	private int luna;

	@Column(name = "an")
	private int an;

	@Column(name = "zilelucrate")
	private int zilelucrate;

	@Column(name = "salariurealizat")
	private int salariurealizat;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idangajat")
	private Angajat idangajat;

	public Bazacalcul(int luna, int an, int zilelucrate, int salariurealizat, Angajat idangajat) {
		this.luna = luna;
		this.an = an;
		this.zilelucrate = zilelucrate;
		this.salariurealizat = salariurealizat;
		this.idangajat = idangajat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAn() {
		return an;
	}

	public Angajat getIdangajat() {
		return idangajat;
	}

	// ! GETTERS
	public int getLuna() {
		return luna;
	}

	public int getSalariurealizat() {
		return salariurealizat;
	}

	public int getZilelucrate() {
		return zilelucrate;
	}

	// ! SETTERS
	public void setLuna(int luna) {
		this.luna = luna;
	}

	public void setSalariurealizat(int salariurealizat) {
		this.salariurealizat = salariurealizat;
	}

	public void setZilelucrate(int zilelucrate) {
		this.zilelucrate = zilelucrate;
	}

	public void setAn(int an) {
		this.an = an;
	}

	public void setIdangajat(Angajat idangajat) {
		this.idangajat = idangajat;
	}
}
