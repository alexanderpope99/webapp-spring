package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Contract;

public class ZileCODisponibileDTO {
	private int id;

	private Long nr;

	private Contract contract;

	public int getId() {
		return id;
	}

	public Integer getIdcontract() {
		if (contract == null)
			return null;
		else
			return contract.getId();
	}

	public Long getNr() {
		return nr;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setContract(Contract idcontract) {
		this.contract = idcontract;
	}

	public void setNr(Long nr) {
		this.nr = nr;
	}
}
