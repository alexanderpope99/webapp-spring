package net.guides.springboot2.crud.model;

import java.io.Serializable;

public class ListaContBancarId implements Serializable {
    protected long idsocietate;
    protected String iban;

    public ListaContBancarId() {
    }

    public ListaContBancarId(long idsocietate, String iban) {
        this.idsocietate = idsocietate;
        this.iban = iban;
    }
}
