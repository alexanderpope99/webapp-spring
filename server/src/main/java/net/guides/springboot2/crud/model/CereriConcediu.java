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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "pentru")
	private int pentru;

	@Column(name = "dela")
	private LocalDate dela;

	@Column(name = "panala")
	private LocalDate panala;

	@Column(name = "tip")
	private String tip;

	@Column(name = "motiv")
	private String motiv;

	@Column(name = "status")
	private String status;

	@Column(name = "societate")
	private int societate;

	public CereriConcediu() {
	}

	public CereriConcediu(int pentru, LocalDate dela, LocalDate panala, String tip, String motiv, String status,
			int societate) {
		this.pentru = pentru;
		this.dela = dela;
		this.panala = panala;
		this.tip = tip;
		this.motiv = motiv;
		this.status = status;
		this.societate = societate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPentru() {
		return pentru;
	}

	public void setPentru(int pentru) {
		this.pentru = pentru;
	}

	public LocalDate getDela() {
		return dela;
	}

	public void setDela(LocalDate dela) {
		this.dela = dela;
	}

	public LocalDate getPanala() {
		return panala;
	}

	public void setPanala(LocalDate panala) {
		this.panala = panala;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getMotiv() {
		return motiv;
	}

	public void setMotiv(String motiv) {
		this.motiv = motiv;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSocietate() {
		return societate;
	}

	public void setSocietate(int societate) {
		this.societate = societate;
	}

}
