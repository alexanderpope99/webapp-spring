package net.guides.springboot2.crud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bazacalcul")
public class Bazacalcul implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "luna", nullable = false)
	private int luna;

	@Column(name = "an", nullable = false)
	private int an;

	@Column(name = "zilelucrate", nullable = false)
	private int zilelucrate = 0;

	@Column(name = "salariurealizat", nullable = false)
	private int salariurealizat = 0;

	@ManyToOne
	@JoinColumn(name = "idangajat")
	private Angajat angajat;

  public Bazacalcul() {
	}

	public Bazacalcul(int luna, int an, int zilelucrate, int salariurealizat, Angajat angajat) {
		this.luna = luna;
		this.an = an;
		this.zilelucrate = zilelucrate;
		this.salariurealizat = salariurealizat;
		this.angajat = angajat;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLuna() {
		return this.luna;
	}

	public void setLuna(int luna) {
		this.luna = luna;
	}

	public int getAn() {
		return this.an;
	}

	public void setAn(int an) {
		this.an = an;
	}

	public int getZilelucrate() {
		return this.zilelucrate;
	}

	public void setZilelucrate(int zilelucrate) {
		this.zilelucrate = zilelucrate;
	}

	public int getSalariurealizat() {
		return this.salariurealizat;
	}

	public void setSalariurealizat(int salariurealizat) {
		this.salariurealizat = salariurealizat;
	}

	public Angajat getAngajat() {
		return this.angajat;
	}

	public void setAngajat(Angajat angajat) {
		this.angajat = angajat;
	}

}
