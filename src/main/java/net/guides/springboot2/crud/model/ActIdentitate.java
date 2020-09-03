package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "actidentitate")
public class ActIdentitate {

    private long id;
    private String cnp;
    private String tip;
    private String serie;
    private String numar;
    private Date dataNasterii;
    private String eliberatDe;
    private String dataEliberarii;
    private String loculNasterii;

    public ActIdentitate() {

    }

    public ActIdentitate(String cnp, String tip, String serie, String numar, Date dataNasterii, String eliberatDe, String dataEliberarii, String loculNasterii) {
        this.cnp = cnp;
        this.tip = tip;
        this.serie = serie;
        this.numar = numar;
        this.dataNasterii = dataNasterii;
        this.eliberatDe = eliberatDe;
        this.dataEliberarii = dataEliberarii;
        this.loculNasterii = loculNasterii;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "cnp", nullable = false)
    public String getCnp() {
        return cnp;
    }
    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    @Column(name = "tip", nullable = false)
    public String getTip() {
        return tip;
    }
    public void setTip(String tip) {
        this.tip = tip;
    }

    @Column(name = "serie", nullable = false)
    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }

    @Column(name = "numar", nullable = false)
    public String getNumar() {
        return numar;
    }
    public void setNumar(String numar) {
        this.numar = numar;
    }

    @Column(name = "datanasterii", nullable = false)
    public Date getDataNasterii() {
        return dataNasterii;
    }
    public void setDataNasterii(Date dataNasterii) {
        this.dataNasterii = dataNasterii;
    }

    @Column(name = "eliberatde", nullable = false)
    public String getEliberatDe() {
        return eliberatDe;
    }
    public void setEliberatDe(String eliberatDe) {
        this.eliberatDe = eliberatDe;
    }

    @Column(name = "dataeliberarii", nullable = false)
    public String getDataEliberarii() {
        return dataEliberarii;
    }
    public void setDataEliberarii(String dataEliberarii) {
        this.dataEliberarii = dataEliberarii;
    }

    @Column(name = "loculnasterii", nullable = false)
    public String getLoculNasterii() {
        return loculNasterii;
    }
    public void setLoculNasterii(String loculNasterii) {
        this.loculNasterii = loculNasterii;
    }

}

