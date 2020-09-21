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
    @Column(name="gen")
    private String gen;
    @Column(name="nume")
    private String nume;
    @Column(name="prenume")
    private String prenume;
    @Column(name="idactidentitate")
    private Long idactidentitate;
    @Column(name="idadresa")
    private Long idadresa;
    @Column(name="starecivila")
    private String starecivila;
    @Column(name="email")
    private String email;
    @Column(name="telefon")
    private String telefon;
    @Column(name="cnp")
    private String cnp;

    public Persoana() {
    }

    public Persoana(String gen, String nume, String prenume, Long idactidentitate, Long idadresa, String starecivila, String email,
            String telefon, String cnp) {
        this.gen = gen;
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

    //! GETTERS
    public String getCnp() {
        return cnp;
    }
    public String getEmail() {
        return email;
    }
    public String getGen() {
        return gen;
    }
    public Long getIdactidentitate() {
        return idactidentitate;
    }
    public Long getIdadresa() {
        return idadresa;
    }
    public String getNume() {
        return nume;
    }
    public String getPrenume() {
        return prenume;
    }
    public String getStarecivila() {
        return starecivila;
    }
    public String getTelefon() {
        return telefon;
    }
    
    //! SETTERS
    public void setCnp(String cnp) {
        this.cnp = cnp;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setGen(String gen) {
        this.gen = gen;
    }
    public void setIdactidentitate(Long idactidentitate) {
        this.idactidentitate = idactidentitate;
    }
    public void setIdadresa(Long idadresa) {
        this.idadresa = idadresa;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }
    public void setStarecivila(String starecivila) {
        this.starecivila = starecivila;
    }
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

}
