package net.guides.springboot2.crud.model;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@IdClass(ListaContBancarId.class)
@Table(name = "listacontbancar")
public class ListaContBancar {

	@Id
	@Column(name = "idsocietate")
	private long idsocietate;
	@Id
	@Column(name = "iban")
	private String iban;

	public ListaContBancar() {

	}

	public ListaContBancar(long idsocietate, String iban) {
		this.idsocietate = idsocietate;
		this.iban = iban;
	}

	public long getIdsocietate() {
		return idsocietate;
	}

	public void setIdsocietate(long idsocietate) {
		this.idsocietate = idsocietate;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}
}

class ListaContBancarId implements Serializable {
	private static final long serialVersionUID = 1L;
	protected long idsocietate;
	protected String iban;

	public ListaContBancarId() {
	}

	public ListaContBancarId(long idsocietate, String iban) {
		this.idsocietate = idsocietate;
		this.iban = iban;
	}
}