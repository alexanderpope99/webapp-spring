package net.guides.springboot2.crud.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;

import net.guides.springboot2.crud.model.types.StatusCaiet;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "caiet", uniqueConstraints = {@UniqueConstraint(columnNames = "serie")})
public class Caiet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "serie", nullable = false)
	private String serie;

	@Column(name = "primulnumar")
	private Integer primulnumar;

	@Column(name = "ultimulnumar")
	private Integer ultimulnumar;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private StatusCaiet status = StatusCaiet.ACTIV;

	@ManyToOne
	@JoinColumn(name = "idsocietate", nullable = false)
	private Societate societate;

	@JsonBackReference(value = "factura-caiet")
	@OneToMany(mappedBy = "caiet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Factura> facturi;

	public Caiet() {
	}

	public Caiet(String serie, Integer primulnumar, Integer ultimulnumar, StatusCaiet status, Societate societate, List<Factura> facturi) {
		this.serie = serie;
		this.primulnumar = primulnumar;
		this.ultimulnumar = ultimulnumar;
		this.status = status;
		this.societate = societate;
		this.facturi = facturi;
	}

	// ! GETTERS, SETTERS

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSerie() {
		return this.serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Integer getPrimulnumar() {
		return this.primulnumar;
	}

	public void setPrimulnumar(Integer primulnumar) {
		this.primulnumar = primulnumar;
	}

	public Integer getUltimulnumar() {
		return this.ultimulnumar;
	}

	public void setUltimulnumar(Integer ultimulnumar) {
		this.ultimulnumar = ultimulnumar;
	}

	public Societate getSocietate() {
		return this.societate;
	}

	public void setSocietate(Societate societate) {
		this.societate = societate;
	}

	public List<Factura> getFacturi() {
		return this.facturi;
	}

	public void setFacturi(List<Factura> facturi) {
		this.facturi = facturi;
	}

	public StatusCaiet getStatus() {
		return status;
	}

	public void setStatus(StatusCaiet status) {
		this.status = status;
	}

	// ! OTHER

	public Caiet update(Caiet newCaiet) {
		this.serie = newCaiet.serie;
		this.primulnumar = newCaiet.primulnumar;
		this.ultimulnumar = newCaiet.ultimulnumar;
		this.status = newCaiet.status;

		return this;
	}

	public Caiet detachFacturi() {
		facturi.forEach(factura -> factura.setCaiet(null));
		return this;
	}
}
