package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Table(name = "sarbatori")
public class Sarbatori implements Serializable {
	private static final long serialVersionUID = 1L;

	Sarbatori() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "dela", nullable = false)
	private LocalDate dela;

	@Column(name = "panala", nullable = false)
	private LocalDate panala;

	@Column(name = "nume")
	private String nume;

	Sarbatori(LocalDate dela, LocalDate panala, String nume) {
		this.dela = dela;
		this.panala = panala;
		this.nume = nume;
	}

	public Sarbatori(String dela, String panala, String nume) {
		this.dela = LocalDate.parse(dela);
		this.panala = LocalDate.parse(panala);
		this.nume = nume;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// GETTERS
	public LocalDate getDela() {
		return dela;
	}

	public LocalDate getPanala() {
		return panala;
	}

	public String getNume() {
		return nume;
	}

	// SETTERS
	public void setDela(LocalDate dela) {
		this.dela = dela;
	}

	public void setPanala(LocalDate panala) {
		this.panala = panala;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}
}
