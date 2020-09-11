package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "adresa")
public class Adresa {

    private long id;
    private String adresa;
    private String localitate;
    private String judet;
    private String tara;

    public Adresa() {

    }

    public Adresa(String adresa, String localitate, String judet, String tara) {
        this.adresa = adresa;
        this.localitate = localitate;
        this.judet = judet;
        this.tara = tara;
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
    public String getAdresa() {
        return adresa;
    }
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    @Column(name = "localitate")
    public String getLocalitate() {
        return localitate;
    }
    public void setLocalitate(String localitate) {
        this.localitate = localitate;
    }

    @Column(name = "judet")
    public String getJudet() {
        return judet;
    }
    public void setJudet(String judet) {
        this.judet = judet;
    }

    @Column(name = "tara")
    public String getTara() {
        return tara;
    }
    public void setTara(String tara) {
        this.tara = tara;
    }
}

