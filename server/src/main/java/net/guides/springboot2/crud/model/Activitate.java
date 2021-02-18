package net.guides.springboot2.crud.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "activitate")
public class Activitate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "nume")
	private String nume;

	@JsonBackReference(value = "activitate-societate")
	@ManyToOne
	@JoinColumn(name = "idsocietate", nullable = false)
	private Societate societate;

	@JsonBackReference(value = "proiect-activitate")
	@OneToMany(mappedBy = "activitate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Proiect> proiecte;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public Societate getSocietate() {
		return societate;
	}

	public void setSocietate(Societate societate) {
		this.societate = societate;
	}

	public List<Proiect> getProiecte() {
		return proiecte;
	}

	public void setProiecte(List<Proiect> proiecte) {
		this.proiecte = proiecte;
	}

	public Activitate update(Activitate na) {
		this.nume = na.nume;
		return this;
	}

	public Activitate detachProiecte() {
		proiecte.forEach(proiect -> proiect.detachFacturi());
		return this;
	}

}
