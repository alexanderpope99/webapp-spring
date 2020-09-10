package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "persoana")
public class Persoana {

    private long id;
    private String nume;
    private String prenume;
    private Long idactidentitate;
    private Long idadresa;
    private String starecivila;
    private String email;
    private String telefon;
    private String cnp;

    public Persoana() {
    }

    public Persoana(String nume, String prenume, Long idactidentitate, Long idadresa, String starecivila, String email,
            String telefon, String cnp) {
        this.nume = nume;
        this.prenume = prenume;
        this.idactidentitate = idactidentitate;
        this.idadresa = idadresa;
        this.starecivila = starecivila;
        this.email = email;
        this.email = email;
        this.telefon = telefon;
        this.cnp = cnp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "nume")
    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Column(name = "prenume")
    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    @Column(name = "idactidentitate")
    public Long getIdactidentitate() {
        return idactidentitate;
    }

    public void setIdactidentitate(Long idactidentitate) {
        this.idactidentitate = idactidentitate;
    }

    @Column(name = "idadresa")
    public Long getIdadresa() {
        return idadresa;
    }

    public void setIdadresa(Long idadresa) {
        this.idadresa = idadresa;
    }

    @Column(name = "starecivila")
    public String getStarecivila() {
        return starecivila;
    }

    public void setStarecivila(String starecivila) {
        this.starecivila = starecivila;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "telefon")
    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    @Column(name = "cnp")
    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

}
