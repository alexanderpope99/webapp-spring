package net.guides.springboot2.crud.dto;

public class ProdusDTO {
	private int id;
	private String denumire;
	private String um;
	private int cantitate;
	private Float pretUnitar;
	private Float valoareFaraTva;
	private Float tva;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDenumire() {
		return denumire;
	}

	public void setDenumire(String denumire) {
		this.denumire = denumire;
	}

	public Float getPretunitar() {
		return pretUnitar;
	}

	public void setPretunitar(Float pretunitar) {
		this.pretUnitar = pretunitar;
	}

	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	public Float getValoarefaratva() {
		return valoareFaraTva;
	}

	public void setValoarefaratva(Float valoarefaratva) {
		this.valoareFaraTva = valoarefaratva;
	}

	public Float getTva() {
		return tva;
	}

	public void setTva(Float valoaretva) {
		this.tva = valoaretva;
	}


	public int getCantitate() {
		return cantitate;
	}

	public void setCantitate(int cantitate) {
		this.cantitate = cantitate;
	}

}
