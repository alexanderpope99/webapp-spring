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

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "cerericoncediu")
public class CereriConcediu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "dela", nullable = false)
	private LocalDate dela;

	@Column(name = "panala", nullable = false)
	private LocalDate panala;

	@Column(name = "tip", nullable = false)
	private String tip;

	@Column(name = "motiv")
	private String motiv;

	@Column(name = "status", nullable = false)
	private String status;

	@JsonBackReference(value = "cerericoncediu-societate")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idsocietate")
	private Societate societate;

	@JsonBackReference(value = "cerericoncediu-user")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iduser")
	private User user;

	public CereriConcediu() {
	}

	public CereriConcediu(LocalDate dela, LocalDate panala, String tip, String motiv, String status,
			Societate societate, User user) {
		this.dela = dela;
		this.panala = panala;
		this.tip = tip;
		this.motiv = motiv;
		this.status = status;
		this.societate = societate;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Societate getSocietate() {
		return societate;
	}

	public void setSocietate(Societate societate) {
		this.societate = societate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
