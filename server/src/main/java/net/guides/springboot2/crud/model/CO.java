package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "co")
public class CO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "tip")
	private String tip;

	@Column(name = "dela")
	private LocalDate dela;

	@Column(name = "panala")
	private LocalDate panala;

	@Column(name = "sporuripermanente")
	private Boolean sporuripermanente;

	@Column(name = "idcontract")
	private Long idcontract;

	public CO() {
	}

	public CO(String tip, LocalDate dela, LocalDate panala, Boolean sporuripermanente, Long idcontract) {
		this.tip = tip;
		this.dela = dela;
		this.panala = panala;
		this.sporuripermanente = sporuripermanente;
		this.idcontract = idcontract;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
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

	public Boolean isSporuripermanente() {
		return sporuripermanente;
	}

	public void setSporuripermanente(Boolean sporuripermanente) {
		this.sporuripermanente = sporuripermanente;
	}

	public Long getIdcontract() {
		return idcontract;
	}

	public void setIdcontract(Long idcontract) {
		this.idcontract = idcontract;
	}
}
