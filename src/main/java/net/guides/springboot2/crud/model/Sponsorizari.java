package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sponsorizari")
public class Sponsorizari {

    private long id;
    @Column(name = "idcontract")
    private long idcontract;
    @Column(name = "denumire")
    private String denumire;
    @Column(name = "cui")
    private String cui;
    @Column(name = "cota")
    private Double cota;
    @Column(name = "suma")
    private Double suma;

    public Sponsorizari() { }

    public Sponsorizari( Long idcontract, String denumire, String cui, Double cota, Double suma ) {
        this.idcontract = idcontract;
        this.denumire = denumire;
        this.cui = cui;
        this.cota = cota;
        this.suma = suma;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    // GETTERS
    public Double getCota() {
        return cota;
    }
    public String getCui() {
        return cui;
    }
    public String getDenumire() {
        return denumire;
    }
    public long getIdcontract() {
        return idcontract;
    }
    public Double getSuma() {
        return suma;
    }
    

    // SETTERS
    public void setCota(Double cota) {
        this.cota = cota;
    }
    public void setCui(String cui) {
        this.cui = cui;
    }
    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }
    public void setIdcontract(long idcontract) {
        this.idcontract = idcontract;
    }
    public void setSuma(Double suma) {
        this.suma = suma;
    }
} 

