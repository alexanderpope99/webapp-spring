package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "echipa")
public class Echipa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "iddepartament")
	private int iddepartament;

	@Column(name = "nume")
	private String nume;

	public Echipa() {

	}

	public Echipa(int iddepartament, String nume) {
		this.iddepartament = iddepartament;
		this.nume = nume;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIddepartament() {
		return iddepartament;
	}

	public void setIddepartament(int iddepartament) {
		this.iddepartament = iddepartament;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}
}
