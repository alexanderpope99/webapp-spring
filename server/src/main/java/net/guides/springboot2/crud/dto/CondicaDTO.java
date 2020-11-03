package net.guides.springboot2.crud.dto;

public class CondicaDTO {
	private int id;

	private String inceput;

	private String sfarsit;

	private String pauzamasa;

	private int idcontract;

	public int getId() {
		return id;
	}

	public int getIdcontract() {
		return idcontract;
	}

	public String getInceput() {
		return inceput;
	}

	public String getPauzamasa() {
		return pauzamasa;
	}

	public String getSfarsit() {
		return sfarsit;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIdcontract(int idcontract) {
		this.idcontract = idcontract;
	}

	public void setInceput(String inceput) {
		this.inceput = inceput;
	}

	public void setPauzamasa(String pauzamasa) {
		this.pauzamasa = pauzamasa;
	}

	public void setSfarsit(String sfarsit) {
		this.sfarsit = sfarsit;
	}
}
