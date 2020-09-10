package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "bursePrivate")
public class BursePrivate {

    private long id;
    private int idcontract;
    private Date data;
    private double cota;
    private double suma;

    public BursePrivate() {

    }

    public BursePrivate(int idcontract, Date data, double cota, double suma) {
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
    public int getIdcontract() {
        return idcontract;
    }

    public void setIdcontract(int idcontract) {
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
    public double getCota() {
        return cota;
    }

    public void setCota(double cota) {
        this.cota = cota;
    }

    @Column(name = "suma")
    public double getSuma() {
        return suma;
    }

    public void setSuma(double suma) {
        this.suma = suma;
    }
}

