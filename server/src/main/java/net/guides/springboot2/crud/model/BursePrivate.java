package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "burseprivate")
public class BursePrivate {

    private long id;
    private Integer idcontract;
    private Date data;
    private Float cota;
    private Float suma;

    public BursePrivate() {

    }

    public BursePrivate(Integer idcontract, Date data, Float cota, Float suma) {
        this.idcontract = idcontract;
        this.data = data;
        this.cota = cota;
        this.suma = suma;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "idcontract")
    public Integer getIdcontract() {
        return idcontract;
    }

    public void setIdcontract(Integer idcontract) {
        this.idcontract = idcontract;
    }

    @Column(name = "data")
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Column(name = "cota")
    public Float getCota() {
        return cota;
    }

    public void setCota(Float cota) {
        this.cota = cota;
    }

    @Column(name = "suma")
    public Float getSuma() {
        return suma;
    }

    public void setSuma(Float suma) {
        this.suma = suma;
    }
}

