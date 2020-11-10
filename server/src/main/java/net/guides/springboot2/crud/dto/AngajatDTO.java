package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.model.Societate;

public class AngajatDTO {
	private int idpersoana;

	private Persoana persoana;

	private Contract contract;

	private Societate societate;

	private Angajat superior;

	public int getIdpersoana() {
		if(persoana == null)
			return idpersoana;
		else return persoana.getId();
	}

	public Integer getIdcontract() {
		if (contract == null)
			return null;
		else
			return contract.getId();
	}

	public Integer getIdsocietate() {
		if (societate == null)
			return null;
		else
			return societate.getId();
	}

	public Integer getIduperior() {
		if (superior == null)
			return null;
		else
			return superior.getPersoana().getId();
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public void setIdpersoana(int idpersoana) {
		this.idpersoana = idpersoana;
	}

	public void setPersoana(Persoana persoana) {
		this.persoana = persoana;
	}

	public void setSocietate(Societate societate) {
		this.societate = societate;
	}

	public void setSuperior(Angajat superior) {
		this.superior = superior;
	}
}
