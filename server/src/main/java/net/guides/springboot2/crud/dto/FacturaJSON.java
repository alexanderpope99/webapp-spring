package net.guides.springboot2.crud.dto;

public class FacturaJSON {
	private String observatii;
	private String codproiect;

	public FacturaJSON() {
	}

	public FacturaJSON(String observatii, String codproiect) {
		this.observatii = observatii;
		this.codproiect = codproiect;
	}

	public String getCodproiect() {
		return codproiect;
	}

	public void setCodproiect(String codproiect) {
		this.codproiect = codproiect;
	}

	public String getObservatii() {
		return observatii;
	}

	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}
}