package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "echipa")
public class Echipa {

    private long id;
    private int iddepartament;
    private String nume;


    public Echipa() {

    }

    public Echipa(int iddepartament, String nume) {
        this.iddepartament = iddepartament;
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

    @Column(name = "iddepartament")
    public int getIddepartament() {
        return iddepartament;
    }

    public void setIddepartament(int iddepartament) {
        this.iddepartament = iddepartament;
    }

    @Column(name = "nume")
    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }
}

