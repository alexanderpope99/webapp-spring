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

	@Column(name = "pensiealimentara")
	private Integer pensiealimentara;

	@Column(name = "popriri")
	private Integer popriri;

	@Column(name = "imprumuturi")
	private Integer imprumuturi;

	@Column(name = "curseurron")
	private Float curseurron;

	@Column(name = "pensiefacangajat")
	private Integer pensiefacangajat;

	@Column(name = "pensiefacangajator")
	private Integer pensiefacangajator;

	@Column(name = "pensiefacangajatretinuta")
	private Integer pensiefacangajatretinuta;

	@Column(name = "pensiefacangajatordeductibila")
	private Integer pensiefacangajatordeductibila;

	@Column(name = "pensiefacexcedent")
	private Integer pensiefacexcedent;

	@JsonBackReference(value = "retinere-stat")
	@OneToOne
	@JoinColumn(name = "idstat")
	private RealizariRetineri stat;

	public Retineri() {
	}

	public Retineri(Integer avansnet, Integer pensiefacultativa, Integer pensiealimentara, Integer popriri,
			Integer imprumuturi, RealizariRetineri stat, Float curseurron, Integer pensiefacangajat,
			Integer pensiefacangajator, Integer pensiefacangajatretinuta, Integer pensiefacangajatordeductibila,
			Integer pensiefacexcedent) {
		this.avansnet = avansnet;
		this.pensiealimentara = pensiealimentara;
		this.popriri = popriri;
		this.imprumuturi = imprumuturi;
		this.stat = stat;
		this.curseurron = curseurron;
		this.pensiefacangajat = pensiefacangajat;
		this.pensiefacangajator = pensiefacangajator;
		this.pensiefacangajatretinuta = pensiefacangajatretinuta;
		this.pensiefacangajatordeductibila = pensiefacangajatordeductibila;
		this.pensiefacexcedent = pensiefacexcedent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RealizariRetineri getStat() {
		return stat;
	}

	public void setStat(RealizariRetineri stat) {
		this.stat = stat;
	}

	public Integer getAvansnet() {
		return avansnet;
	}

	public void setAvansnet(Integer avansnet) {
		this.avansnet = avansnet;
	}

	public Integer getImprumuturi() {
		return imprumuturi;
	}

	public void setImprumuturi(Integer imprumuturi) {
		this.imprumuturi = imprumuturi;
	}

	public Integer getPensiealimentara() {
		return pensiealimentara;
	}

	public void setPensiealimentara(Integer pensiealimentara) {
		this.pensiealimentara = pensiealimentara;
	}

	public Integer getPopriri() {
		return popriri;
	}

	public void setPopriri(Integer popriri) {
		this.popriri = popriri;
	}

	public Float getCurseurron() {
		return curseurron;
	}

	public void setCurseurron(Float curseurron) {
		this.curseurron = curseurron;
	}

	public Integer getPensiefacangajat() {
		return pensiefacangajat;
	}

	public void setPensiefacangajat(Integer pensiefacangajat) {
		this.pensiefacangajat = pensiefacangajat;
	}

	public Integer getPensiefacangajator() {
		return pensiefacangajator;
	}

	public void setPensiefacangajator(Integer pensiefacangajator) {
		this.pensiefacangajator = pensiefacangajator;
	}

	public Integer getPensiefacangajatordeductibila() {
		return pensiefacangajatordeductibila;
	}

	public void setPensiefacangajatordeductibila(Integer pensiefacangajatordeductibila) {
		this.pensiefacangajatordeductibila = pensiefacangajatordeductibila;
	}

	public Integer getPensiefacangajatretinuta() {
		return pensiefacangajatretinuta;
	}

	public void setPensiefacangajatretinuta(Integer pensiefacangajatretinuta) {
		this.pensiefacangajatretinuta = pensiefacangajatretinuta;
	}

	public Integer getPensiefacexcedent() {
		return pensiefacexcedent;
	}

	public void setPensiefacexcedent(Integer pensiefacexcedent) {
		this.pensiefacexcedent = pensiefacexcedent;
	}

}
