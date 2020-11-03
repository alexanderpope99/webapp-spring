package net.guides.springboot2.crud.dto;

public class ContBancarDTO {
	private String iban;

	private String numebanca;

	public String getIban() {
		return iban;
	}

	public String getNumebanca() {
		return numebanca;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public void setNumebanca(String numebanca) {
		this.numebanca = numebanca;
	}
}
