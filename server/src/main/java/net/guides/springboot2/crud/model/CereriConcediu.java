package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "cerericoncediu")
public class CereriConcediu {

	private Long id;
	private Long pentru;
	private LocalDate dela;
	private LocalDate panala;
	private String tip;
	private String motiv;

	public CereriConcediu() {
	}

	public CereriConcediu(Long pentru, LocalDate dela, LocalDate panala, String tip, String motiv) {
		this.pentru = pentru;
		this.dela = dela;
		this.panala = panala;
		this.tip = tip;
		this.motiv = motiv;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "pentru")
	public Long getPentru() {
		return pentru;
	}

	public void setPentru(Long pentru) {
		this.pentru = pentru;
	}

	@Column(name = "dela")
	public LocalDate getDela() {
		return dela;
	}

	public void setDela(LocalDate dela) {
		this.dela = dela;
	}

	@Column(name = "panala")
	public LocalDate getPanala() {
		return panala;
	}

	public void setPanala(LocalDate panala) {
		this.panala = panala;
	}

	@Column(name = "tip")
	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	@Column(name = "motiv")
	public String getMotiv() {
		return motiv;
	}

	public void setMotiv(String motiv) {
		this.motiv = motiv;
	}

}
