package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "altebeneficii")
public class AlteBeneficii {

    private long id;
    private String nume;
    private Float valoare;
    private Float procent;
    private String aplicare;
    private Integer idcontract;

    public AlteBeneficii() {

    }

    public AlteBeneficii(String nume, Float valoare, Float procent, String aplicare, Integer idcontract) {
        this.nume = nume;
        this.valoare = valoare;
        this.procent = procent;
        this.aplicare = aplicare;
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

    @Column(name = "nume")
    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }

    @Column(name = "valoare")
    public Float getValoare() {
        return valoare;
    }
    public void setValoare(Float valoare) {
        this.valoare = valoare;
    }

    @Column(name = "procent")
    public Float getProcent() {
        return procent;
    }
    public void setProcent(Float procent) {
        this.procent = procent;
    }

    @Column(name = "aplicare")
    public String getAplicare() {
        return aplicare;
    }
    public void setAplicare(String aplicare) {
        this.aplicare = aplicare;
    }

    @Column(name = "idcontract")
    public Integer getIdcontract() {
        return idcontract;
    }
    public void setIdcontract(Integer idcontract) {
        this.idcontract = idcontract;
    }
}

