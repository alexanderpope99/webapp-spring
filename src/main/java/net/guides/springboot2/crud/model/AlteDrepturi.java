package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "alteDrepturi")
public class AlteDrepturi {

    private long id;
    private double valoare;
    private int idstat;

    public AlteDrepturi() {

    }

    public AlteDrepturi(double valoare, int idstat) {
        this.valoare = valoare;
        this.idstat = idstat;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "valoare")
    public double getValoare() {
        return valoare;
    }
    public void setValoare(double valoare) {
        this.valoare = valoare;
    }

    @Column(name = "idstat")
    public int getIdstat() {
        return idstat;
    }
    public void setIdstat(int idstat) {
        this.idstat = idstat;
    }
}

