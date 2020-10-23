package net.guides.springboot2.crud.model;

import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Table(name = "sarbatori")
public class Sarbatori {
	Sarbatori() {}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "dela")
	private LocalDate dela;

	@Column(name = "panala")
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

	public long getId() {
		return id;
	}
	public void setId(long id) {
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
