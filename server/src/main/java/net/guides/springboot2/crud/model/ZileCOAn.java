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
@Table(name = "zilecoan")
public class ZileCOAn implements Serializable {
	private static final long serialVersionUID = 1L;

	public ZileCOAn() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "an", nullable = false)
	private int an;

	@Column(name = "zilecoefectuat", nullable = false)
	private int zilecoefectuat = 0;

	@Column(name = "zileconeefectuat", nullable = false)
	private int zileconeefectuat = 0;

	@ManyToOne
	@JoinColumn(name = "idangajat")
	private Angajat angajat;

	public ZileCOAn(int an, int zilecoefectuat, int zileconeefectuat, Angajat angajat) {
		this.an = an;
		this.zilecoefectuat = zilecoefectuat;
		this.zileconeefectuat = zileconeefectuat;
		this.angajat = angajat;
	}


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAn() {
		return an;
	}

	public void setAn(int an) {
		this.an = an;
	}

	public Angajat getAngajat() {
		return angajat;
	}

	public void setAngajat(Angajat angajat) {
		this.angajat = angajat;
	}

	public int getZilecoefectuat() {
		return zilecoefectuat;
	}

	public void setZilecoefectuat(int zilecoefectuat) {
		this.zilecoefectuat = zilecoefectuat;
	}

	public int getZileconeefectuat() {
		return zileconeefectuat;
	}

	public void setZileconeefectuat(int zileconeefectuat) {
		this.zileconeefectuat = zileconeefectuat;
	}

}
