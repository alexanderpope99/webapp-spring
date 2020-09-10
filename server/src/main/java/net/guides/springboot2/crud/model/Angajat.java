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

    public Angajat() {

    }

    public Angajat(Long idcontract, Long idsocietate) {
        this.idcontract = idcontract;
        this.idsocietate = idsocietate;
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
}
