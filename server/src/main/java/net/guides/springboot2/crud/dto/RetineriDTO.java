package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.RealizariRetineri;

public class RetineriDTO {
	private int id;

	private int idstat;

	private Integer avansnet;

	private Integer pensiealimentara;

	private Integer popriri;

	private Integer imprumuturi;

	private RealizariRetineri stat;

	private Float curseurron;

	private Integer pensiefacangajat;

	private Integer pensiefacangajator;

	private Integer pensiefacangajatretinuta;

	private Integer pensiefacangajatordeductibila;

	private Integer pensiefacexcedent;

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

	public Integer getPopriri() {
		return popriri;
	}

	public Float getCurseurron() {
		return curseurron;
	}

	public Integer getPensiefacangajat() {
		return pensiefacangajat;
	}

	public Integer getPensiefacangajator() {
		return pensiefacangajator;
	}

	public Integer getPensiefacangajatordeductibila() {
		return pensiefacangajatordeductibila;
	}

	public Integer getPensiefacangajatretinuta() {
		return pensiefacangajatretinuta;
	}

	public Integer getPensiefacexcedent() {
		return pensiefacexcedent;
	}

	public RealizariRetineri getStat() {
		return stat;
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

	public void setPopriri(Integer popriri) {
		this.popriri = popriri;
	}

	public void setCurseurron(Float curseurron) {
		this.curseurron = curseurron;
	}

	public void setPensiefacangajat(Integer pensiefacangajat) {
		this.pensiefacangajat = pensiefacangajat;
	}

	public void setPensiefacangajator(Integer pensiefacangajator) {
		this.pensiefacangajator = pensiefacangajator;
	}

	public void setPensiefacangajatordeductibila(Integer pensiefacangajatordeductibila) {
		this.pensiefacangajatordeductibila = pensiefacangajatordeductibila;
	}

	public void setPensiefacangajatretinuta(Integer pensiefacangajatretinuta) {
		this.pensiefacangajatretinuta = pensiefacangajatretinuta;
	}

	public void setPensiefacexcedent(Integer pensiefacexcedent) {
		this.pensiefacexcedent = pensiefacexcedent;
	}

	public void setStat(RealizariRetineri stat) {
		this.stat = stat;
	}

	public int getTotalPensiiFacultativeRON() {
		return 
		pensiefacangajat
		+ pensiefacangajator
		+ pensiefacangajatretinuta
		+ pensiefacangajatordeductibila
		+ pensiefacexcedent;
	}

	public float getTotalPensiiFacultativeEUR() {
		return 
		(pensiefacangajat
		+ pensiefacangajator
		+ pensiefacangajatretinuta
		+ pensiefacangajatordeductibila
		+ pensiefacexcedent)/curseurron;
	}
}
