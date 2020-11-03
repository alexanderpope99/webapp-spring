package net.guides.springboot2.crud.dto;

public class CentruCostDTO {
	private int id;

	private int idadresa;

	private int idsocietate;

	private String nume;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdadresa() {
		return idadresa;
	}

	public void setIdadresa(int idadresa) {
		this.idadresa = idadresa;
	}

	public int getIdsocietate() {
		return idsocietate;
	}

	public void setIdsocietate(int idsocietate) {
		this.idsocietate = idsocietate;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}
}
