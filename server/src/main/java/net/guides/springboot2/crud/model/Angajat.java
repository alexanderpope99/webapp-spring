package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "angajat")
public class Angajat {

    private Integer idpersoana;
    private Integer idcontract;
    private Integer idsocietate;
    private Integer co;
    private Integer cm;

    public Angajat() {

    }

    public Angajat(Integer idcontract, Integer idsocietate, Integer co, Integer cm) {
        this.idcontract = idcontract;
        this.idsocietate = idsocietate;
        this.co = co;
        this.cm = cm;
    }

    @Id
    @Column(name = "idpersoana")
    public Integer getIdpersoana() {
        return idpersoana;
    }

    public void setIdpersoana(Integer idpersoana) {
        this.idpersoana = idpersoana;
    }

    @Column(name = "idcontract")
    public Integer getIdcontract() {
        return idcontract;
    }

    public void setIdcontract(Integer idcontract) {
        this.idcontract = idcontract;
    }

    @Column(name = "idsocietate")
    public Integer getIdsocietate() {
        return idsocietate;
    }

    public void setIdsocietate(Integer idsocietate) {
        this.idsocietate = idsocietate;
    }

    @Column(name = "co")
    public Integer getCo() {
        return co;
    }

    public void setCo(Integer co) {
        this.co = co;
    }

    @Column(name = "cm")
    public Integer getCm() {
        return cm;
    }

    public void setCm(Integer cm) {
        this.cm = cm;
    }
}
