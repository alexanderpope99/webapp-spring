package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "altedrepturi")
public class AlteDrepturi {

    private long id;
    private Float valoare;
    private Integer idstat;

    public AlteDrepturi() {

    }

    public AlteDrepturi(Float valoare, Integer idstat) {
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
    public Float getValoare() {
        return valoare;
    }
    public void setValoare(Float valoare) {
        this.valoare = valoare;
    }

    @Column(name = "idstat")
    public Integer getIdstat() {
        return idstat;
    }
    public void setIdstat(Integer idstat) {
        this.idstat = idstat;
    }
}

