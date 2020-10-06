package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "retineri")
public class Retineri {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
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

	@Column(name = "idstat")
	private Long idstat;

	public Retineri() {
	}

	public Retineri(Integer avansnet, Integer pensiefacultativa, Integer pensiealimentara, Integer popriri,
			Integer imprumuturi, Long idstat) {
		this.avansnet = avansnet;
		this.pensiefacultativa = pensiefacultativa;
		this.pensiealimentara = pensiealimentara;
		this.popriri = popriri;
		this.imprumuturi = imprumuturi;
		this.idstat = idstat;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// GETTERS
	public Long getIdstat() {
		return idstat;
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

	// SETTERS
	public void setIdstat(Long idstat) {
		this.idstat = idstat;
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
