package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "alteBeneficii")
public class AlteBeneficii {

    private long id;
    private String nume;
    private double valoare;
    private double procent;
    private String aplicare;
    private int idcontract;

    public AlteBeneficii() {

    }

    public AlteBeneficii(String nume, double valoare, double procent, String aplicare, int idcontract) {
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

    @Column(name = "nume", nullable = false)
    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }

    @Column(name = "valoare", nullable = false)
    public double getValoare() {
        return valoare;
    }
    public void setValoare(double valoare) {
        this.valoare = valoare;
    }

    @Column(name = "procent", nullable = false)
    public double getProcent() {
        return procent;
    }
    public void setProcent(double procent) {
        this.procent = procent;
    }

    @Column(name = "aplicare", nullable = false)
    public String getAplicare() {
        return aplicare;
    }
    public void setAplicare(String aplicare) {
        this.aplicare = aplicare;
    }

    @Column(name = "idcontract", nullable = false)
    public int getIdcontract() {
        return idcontract;
    }
    public void setIdcontract(int idcontract) {
        this.idcontract = idcontract;
    }
}

