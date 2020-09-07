package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "angajat")
public class Angajat {

    private Long idpersoana;
    private Long idcontract;
    private Long idsocietate;
    private Long co;
    private Long cm;

    public Angajat() {

    }

    public Angajat(Long idcontract, Long idsocietate, Long co, Long cm) {
        this.idcontract = idcontract;
        this.idsocietate = idsocietate;
        this.co = co;
        this.cm = cm;
    }

    @Id
    @Column(name = "idpersoana")
    public Long getIdpersoana() {
        return idpersoana;
    }

    public void setIdpersoana(Long idpersoana) {
        this.idpersoana = idpersoana;
    }

    @Column(name = "idcontract")
    public Long getIdcontract() {
        return idcontract;
    }

    public void setIdcontract(Long idcontract) {
        this.idcontract = idcontract;
    }

    @Column(name = "idsocietate")
    public Long getIdsocietate() {
        return idsocietate;
    }

    public void setIdsocietate(Long idsocietate) {
        this.idsocietate = idsocietate;
    }

    @Column(name = "co")
    public Long getCo() {
        return co;
    }

    public void setCo(Long co) {
        this.co = co;
    }

    @Column(name = "cm")
    public Long getCm() {
        return cm;
    }

    public void setCm(Long cm) {
        this.cm = cm;
    }
}
