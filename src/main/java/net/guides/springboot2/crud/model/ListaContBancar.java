package net.guides.springboot2.crud.model;

import javax.persistence.*;

@Entity
@IdClass(ListaContBancarId.class)
@Table(name = "listacontbancar")
public class ListaContBancar {

    @Id
    private long idsocietate;
    @Id
    private String iban;


    public ListaContBancar() {

    }

    public ListaContBancar(long idsocietate, String iban) {
        this.idsocietate = idsocietate;
        this.iban = iban;
    }

    @Column(name="idsocietate")
    public long getIdsocietate() {
        return idsocietate;
    }

    public void setIdsocietate(long idsocietate) {
        this.idsocietate = idsocietate;
    }


    @Column(name="iban")
    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}

