package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "retineri")
public class Retineri {

    private long id;
    @Column(name = "valoare")
    private Double valoare;
    @Column(name = "idstat")
    private Long idstat;

    public Retineri() { }

    public Retineri( Double valoare, Long idstat ) {
        this.valoare = valoare;
        this.idstat = idstat;
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
    public Long getIdstat() {
        return idstat;
    }
    public Double getValoare() {
        return valoare;
    }

    // SETTERS
    public void setIdstat(Long idstat) {
        this.idstat = idstat;
    }
    public void setValoare(Double valoare) {
        this.valoare = valoare;
    }
}

