package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "societate")
public class Societate {

    private long id;
    @Column(name = "nume")
    private String nume;
    @Column(name = "idcaen")
    private Long idcaen;
    @Column(name = "cif")
    private String cif;
    @Column(name = "capsoc")
    private Double capsoc;
    @Column(name = "regcom")
    private String regcom;
    @Column(name = "idadresa")
    private Long idadresa;
    @Column(name = "email")
    private String email;


    public Societate() { }

    public Societate( String nume, Long idcaen, String cif, Double capsoc, String regcom, Long idadresa, String email ) {
        this.nume = nume;
        this.cif = cif;
        this.capsoc = capsoc;
        this.regcom = regcom;
        this.idadresa = idadresa;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }

    public Long getIdcaen() {
        return idcaen;
    }
    public void setIdcaen(Long idcaen) {
        this.idcaen = idcaen;
    }

    public Double getCapsoc() {
        return capsoc;
    }
    public void setCapsoc(Double capsoc) {
        this.capsoc = capsoc;
    }

    public String getCif() {
        return cif;
    }
    public void setCif(String cif) {
        this.cif = cif;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Long getIdadresa() {
        return idadresa;
    }
    public void setIdadresa(Long idadresa) {
        this.idadresa = idadresa;
    }
    
    public String getRegcom() {
        return regcom;
    }
    public void setRegcom(String regcom) {
        this.regcom = regcom;
    }
}

