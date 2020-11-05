package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.RealizariRetineri;

public class RetineriDTO {
	private int id;

	private int idstat;
	
	private Integer avansnet;

	private Integer pensiefacultativa;

	private Integer pensiealimentara;

	private Integer popriri;

	private Integer imprumuturi;

	private RealizariRetineri stat;

	public Integer getAvansnet() {
		return avansnet;
	}

	public int getId() {
		return id;
	}

	public Integer getIdstat() {
		if (stat == null)
			return idstat;
		else
			return stat.getId();
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

	public void setAvansnet(Integer avansnet) {
		this.avansnet = avansnet;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIdtat(int idstat) {
		this.idstat = idstat;
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
