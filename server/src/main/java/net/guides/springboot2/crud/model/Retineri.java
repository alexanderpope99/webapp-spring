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
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "retineri")
public class Retineri implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "avansnet")
	private Integer avansnet = 0;

	@Column(name = "pensiealimentara")
	private Integer pensiealimentara = 0;

	@Column(name = "popriri")
	private Integer popriri = 0;

	@Column(name = "imprumuturi")
	private Integer imprumuturi = 0;

	@Column(name = "curseurron")
	private Float curseurron = 0f;

	@Column(name = "pensiefacangajat")
	private Integer pensiefacangajat = 0;

	@Column(name = "pensiefacangajator")
	private Integer pensiefacangajator = 0;

	@Column(name = "pensiefacangajatretinuta")
	private Integer pensiefacangajatretinuta = 0;

	@Column(name = "pensiefacangajatordeductibila")
	private Integer pensiefacangajatordeductibila = 0;

	@Column(name = "pensiefacexcedent")
	private Integer pensiefacexcedent = 0;

	@JsonBackReference(value = "retinere-stat")
	@OneToOne
	@JoinColumn(name = "idstat")
	private RealizariRetineri stat;

	public Retineri() {
	}

	public Retineri(RealizariRetineri stat) {
		this.stat = stat;
		this.avansnet = 0;
		this.pensiealimentara = 0;
		this.popriri = 0;
		this.imprumuturi = 0;
		this.curseurron = 0f;

		this.pensiefacangajat = 0;
		this.pensiefacangajator = 0;
		this.pensiefacangajatretinuta = 0;
		this.pensiefacangajatordeductibila = 0;
		this.pensiefacexcedent = 0;
	}

	public Retineri(RealizariRetineri stat, Integer avansnet, Integer pensiealimentara, Integer popriri,
			Integer imprumuturi, Float curseurron, Integer pensiefacangajat, Integer pensiefacangajator,
			Integer pensiefacangajatretinuta, Integer pensiefacangajatordeductibila, Integer pensiefacexcedent) {
		this.stat = stat;
		this.avansnet = avansnet;
		this.pensiealimentara = pensiealimentara;
		this.popriri = popriri;
		this.imprumuturi = imprumuturi;
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

	@JsonIgnore
	public int getTotalPensiiFacultativeRON() {
		return pensiefacangajat + pensiefacangajator + pensiefacangajatretinuta + pensiefacangajatordeductibila
				+ pensiefacexcedent;
	}

	@JsonIgnore
	public float getTotalPensiiFacultativeEUR() {
		return (pensiefacangajat + pensiefacangajator + pensiefacangajatretinuta + pensiefacangajatordeductibila
				+ pensiefacexcedent) / curseurron;
	}

}
