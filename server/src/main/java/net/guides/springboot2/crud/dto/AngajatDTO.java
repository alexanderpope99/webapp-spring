package net.guides.springboot2.crud.dto;

public class AngajatDTO {
	private int idpersoana;

	private int idcontract;

	private int idsocietate;

	private int superior;

	public int getIdcontract() {
		return idcontract;
	}

	public void setIdcontract(int idcontract) {
		this.idcontract = idcontract;
	}

	public int getIdpersoana() {
		return idpersoana;
	}

	public void setIdpersoana(int idpersoana) {
		this.idpersoana = idpersoana;
	}

	public int getIdsocietate() {
		return idsocietate;
	}

	public void setIdsocietate(int idsocietate) {
		this.idsocietate = idsocietate;
	}

	public int getSuperior() {
		return superior;
	}

	public void setSuperior(int superior) {
		this.superior = superior;
	}
}
