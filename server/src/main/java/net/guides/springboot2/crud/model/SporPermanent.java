package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sporpermanent")
public class SporPermanent {

    private long id;
    @Column(name = "nume")
    private String nume;
    @Column(name = "valoare")
    private Double valoare;
    @Column(name = "procent")
    private Double procent;
    @Column(name = "aplicare")
    private String aplicare;
    @Column(name = "idcontract")
    private Long idcontract;

    public SporPermanent() { }

    public SporPermanent( String nume, Double valoare, Double procent, String aplicare, Long idcontract ) {
        this.nume = nume;
        this.valoare = valoare;
        this.procent = procent;
        this.aplicare = aplicare;
        this.idcontract = idcontract;
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
    public String getAplicare() {
        return aplicare;
    }
    public Long getIdstat() {
        return idcontract;
    }
    public String getNume() {
        return nume;
    }
    public Double getProcent() {
        return procent;
    }
    public Double getValoare() {
        return valoare;
    }
    

    // SETTERS
    public void setAplicare(String aplicare) {
        this.aplicare = aplicare;
    }
    public void setIdstat(Long idcontract) {
        this.idcontract = idcontract;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public void setProcent(Double procent) {
        this.procent = procent;
    }
    public void setValoare(Double valoare) {
        this.valoare = valoare;
    }
} 

