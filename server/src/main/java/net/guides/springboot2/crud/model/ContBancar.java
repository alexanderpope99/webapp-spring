package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "contbancar")
public class ContBancar {

    private String iban;
    private String numebanca;

    public ContBancar() {

    }

    public ContBancar(String iban, String numebanca) {
        this.iban = iban;
        this.numebanca = numebanca;
    }

    @Id
    @Column(name = "iban",nullable=false)
    public String getIban() {
        return iban;
    }
    public void setIban(String iban) {
        this.iban = iban;
    }

    @Column(name = "numebanca")
    public String getNumebanca() {
        return numebanca;
    }
    public void setNumebanca(String adresa) {
        this.numebanca = adresa;
    }

}

