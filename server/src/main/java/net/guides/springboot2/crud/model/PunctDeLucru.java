package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "punctdelucru")
public class PunctDeLucru {

    private long id;
    @Column(name = "idadresa")
    private Long idadresa;
    @Column(name = "idsocietate")
    private Long idsocietate;
    @Column(name = "nume")
    private String nume;

    public PunctDeLucru() { }

    public PunctDeLucru( Long idadresa, Long idsocietate, String nume ) {
        this.idadresa = idadresa;
        this.idsocietate = idsocietate;
        this.nume = nume;
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
    public Long getIdadresa() {
        return idadresa;
    }
    public Long getIdsocietate() {
        return idsocietate;
    }
    public String getNume() {
        return nume;
    }

    // SETTERS
    public void setIdadresa(Long idadresa) {
        this.idadresa = idadresa;
    }
    public void setIdsocietate(Long idsocietate) {
        this.idsocietate = idsocietate;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
}

