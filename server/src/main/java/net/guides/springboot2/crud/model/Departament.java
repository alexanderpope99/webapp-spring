package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "departament")
public class Departament {

    private long id;
    private Integer idadresa;
    private Integer idsocietate;
    private String nume;


    public Departament() {

    }

    public Departament(Integer idadresa, Integer idsocietate, String nume) {
        this.idadresa = idadresa;
        this.idsocietate = idsocietate;
        this.nume = nume;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "adresa")
    public Integer getIdadresa() {
        return idadresa;
    }
    public void setIdadresa(Integer idadresa) {
        this.idadresa = idadresa;
    }

    @Column(name = "idsocietate")
    public Integer getIdsocietate() {
        return idsocietate;
    }

    public void setIdsocietate(Integer idsocietate) {
        this.idsocietate = idsocietate;
    }

    @Column(name = "nume")
    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }
}

