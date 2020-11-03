package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.Societate;

public class AngajatDTO {
	private int idpersoana;

	private Contract contract;

	private Societate societate;

	private Angajat superior;

	public Integer getIdcontract() {
		if (contract == null)
			return null;
		else
			return contract.getId();
	}

	public void setContract(Contract idcontract) {
		this.contract = idcontract;
	}

	public int getIdpersoana() {
		return idpersoana;
	}

	public void setIdpersoana(int idpersoana) {
		this.idpersoana = idpersoana;
	}

	public Integer getIdsocietate() {
		if (societate == null)
			return null;
		else
			return societate.getId();
	}

	public void setSocietate(Societate idsocietate) {
		this.societate = idsocietate;
	}

	public Integer getIduperior() {
		if (superior == null)
			return null;
		else
			return superior.getPersoana().getId();
	}

	public void setSuperior(Angajat superior) {
		this.superior = superior;
	}
}
