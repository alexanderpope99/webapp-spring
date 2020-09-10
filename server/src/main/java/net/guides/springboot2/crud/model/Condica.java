package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "condica")
public class Condica {

    private long id;
    private String inceput;
    private String sfarsit;
    private String pauzamasa;
    private int idcontract;

    public Condica() {

    }

    public Condica(String inceput, String sfarsit, String pauzamasa, int idcontract) {
        this.inceput = inceput;
        this.sfarsit = sfarsit;
        this.pauzamasa = pauzamasa;
        this.idcontract = idcontract;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "inceput")
    public String getInceput() {
        return inceput;
    }

    public void setInceput(String inceput) {
        this.inceput = inceput;
    }

    @Column(name = "sfarsit")
    public String getSfarsit() {
        return sfarsit;
    }

    public void setSfarsit(String sfarsit) {
        this.sfarsit = sfarsit;
    }

    @Column(name = "pauzamasa")
    public String getPauzamasa() {
        return pauzamasa;
    }

    public void setPauzamasa(String pauzamasa) {
        this.pauzamasa = pauzamasa;
    }

    @Column(name = "idcontract")
    public int getIdcontract() {
        return idcontract;
    }

    public void setIdcontract(int idcontract) {
        this.idcontract = idcontract;
    }
}

