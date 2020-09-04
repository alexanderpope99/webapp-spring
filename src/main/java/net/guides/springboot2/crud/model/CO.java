package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "co")
public class CO {

    private long id;
    private String tip;
    private Date dela;
    private Date panala;
    private boolean sporuripermanente;
    private int idcontract;

    public CO() {

    }

    public CO(String tip, Date dela, Date panala, boolean sporuripermanente, int idcontract) {
        this.tip = tip;
        this.dela = dela;
        this.panala = panala;
        this.sporuripermanente = sporuripermanente;
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

    @Column(name = "tip")
    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Column(name = "dela")
    public Date getDela() {
        return dela;
    }

    public void setDela(Date dela) {
        this.dela = dela;
    }

    @Column(name = "panala")
    public Date getPanala() {
        return panala;
    }

    public void setPanala(Date panala) {
        this.panala = panala;
    }

    @Column(name = "sporuripermanente")
    public boolean isSporuripermanente() {
        return sporuripermanente;
    }

    public void setSporuripermanente(boolean sporuripermanente) {
        this.sporuripermanente = sporuripermanente;
    }

    @Column(name = "idcontract")
    public int getIdcontract() {
        return idcontract;
    }

    public void setIdcontract(int idcontract) {
        this.idcontract = idcontract;
    }
}

