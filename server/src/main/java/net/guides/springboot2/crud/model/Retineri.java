package net.guides.springboot2.crud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "retineri")
public class Retineri implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "avansnet")
	private Integer avansnet;

	@Column(name = "pensiefacultativa")
	private Integer pensiefacultativa;

	@Column(name = "pensiealimentara")
	private Integer pensiealimentara;

	@Column(name = "popriri")
	private Integer popriri;

	@Column(name = "imprumuturi")
	private Integer imprumuturi;

	@JsonBackReference(value = "retinere-stat")
	@OneToOne
	@JoinColumn(name = "idstat")
	private RealizariRetineri stat;

	public Retineri() {
	}

	public Retineri(Integer avansnet, Integer pensiefacultativa, Integer pensiealimentara, Integer popriri,
			Integer imprumuturi, RealizariRetineri stat) {
		this.avansnet = avansnet;
		this.pensiefacultativa = pensiefacultativa;
		this.pensiealimentara = pensiealimentara;
		this.popriri = popriri;
		this.imprumuturi = imprumuturi;
		this.stat = stat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//! GETTERS
	public RealizariRetineri getStat() {
		return stat;
	}

	public Integer getAvansnet() {
		return avansnet;
	}

	public Integer getImprumuturi() {
		return imprumuturi;
	}

	public Integer getPensiealimentara() {
		return pensiealimentara;
	}

	public Integer getPensiefacultativa() {
		return pensiefacultativa;
	}

	public Integer getPopriri() {
		return popriri;
	}

	//! SETTERS
	public void setStat(RealizariRetineri stat) {
		this.stat = stat;
	}

	public void setAvansnet(Integer avansnet) {
		this.avansnet = avansnet;
	}

	public void setImprumuturi(Integer imprumuturi) {
		this.imprumuturi = imprumuturi;
	}

	public void setPensiealimentara(Integer pensiealimentara) {
		this.pensiealimentara = pensiealimentara;
	}

	public void setPensiefacultativa(Integer pensiefacultativa) {
		this.pensiefacultativa = pensiefacultativa;
	}

	public void setPopriri(Integer popriri) {
		this.popriri = popriri;
	}
}
