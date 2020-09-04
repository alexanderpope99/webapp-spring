package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "angajat")
public class Angajat {

    private long id;
    private int idpersoana;
    private int idcontract;
    private int idsocietate;
    private int co;
    private int cm;

    public Angajat() {

    }

    public Angajat(int idpersoana, int idcontract, int idsocietate, int co, int cm) {
        this.idpersoana = idpersoana;
        this.idcontract = idcontract;
        this.idsocietate = idsocietate;
        this.co = co;
        this.cm = cm;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "idpersoana")
    public int getIdpersoana() {
        return idpersoana;
    }

    public void setIdpersoana(int idpersoana) {
        this.idpersoana = idpersoana;
    }

    @Column(name = "idcontract")
    public int getIdcontract() {
        return idcontract;
    }

    public void setIdcontract(int idcontract) {
        this.idcontract = idcontract;
    }

    @Column(name = "idsocietate")
    public int getIdsocietate() {
        return idsocietate;
    }

    public void setIdsocietate(int idsocietate) {
        this.idsocietate = idsocietate;
    }

    @Column(name = "co")
    public int getCo() {
        return co;
    }

    public void setCo(int co) {
        this.co = co;
    }

    @Column(name = "cm")
    public int getCm() {
        return cm;
    }

    public void setCm(int cm) {
        this.cm = cm;
    }
}

