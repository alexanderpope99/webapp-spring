package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "co")
public class CO extends Concediu {
	private static final long serialVersionUID = 1L;

	@Column(name = "tip")
	private String tip;

	@Column(name = "sporuripermanente")
	private Boolean sporuripermanente;

	public CO() {
	}

	public CO(String tip, LocalDate dela, LocalDate panala, Boolean sporuripermanente, Contract contract) {
		super(dela, panala, contract);
		this.tip = tip;
		this.sporuripermanente = sporuripermanente;
	}

	public String getTip() {
		return this.tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public Boolean isSporuripermanente() {
		return this.sporuripermanente;
	}

	public Boolean getSporuripermanente() {
		return this.sporuripermanente;
	}

	public void setSporuripermanente(Boolean sporuripermanente) {
		this.sporuripermanente = sporuripermanente;
	}

}
