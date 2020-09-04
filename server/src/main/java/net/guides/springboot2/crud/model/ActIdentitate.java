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
    private Date datanasterii;
    private String eliberatde;
    private String dataeliberarii;
    private String loculnasterii;

    public ActIdentitate() {

    }

    public ActIdentitate(String cnp, String tip, String serie, String numar, Date datanasterii, String eliberatde, String dataeliberarii, String loculnasterii) {
        this.cnp = cnp;
        this.tip = tip;
        this.serie = serie;
        this.numar = numar;
        this.datanasterii = datanasterii;
        this.eliberatde = eliberatde;
        this.dataeliberarii = dataeliberarii;
        this.loculnasterii = loculnasterii;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "cnp")
    public String getCnp() {
        return cnp;
    }
    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    @Column(name = "tip")
    public String getTip() {
        return tip;
    }
    public void setTip(String tip) {
        this.tip = tip;
    }

    @Column(name = "serie")
    public String getSerie() {
        return serie;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }

    @Column(name = "numar")
    public String getNumar() {
        return numar;
    }
    public void setNumar(String numar) {
        this.numar = numar;
    }

    @Column(name = "datanasterii")
    public Date getDataNasterii() {
        return datanasterii;
    }
    public void setDataNasterii(Date dataNasterii) {
        this.datanasterii = dataNasterii;
    }

    @Column(name = "eliberatde")
    public String getEliberatDe() {
        return eliberatde;
    }
    public void setEliberatDe(String eliberatDe) {
        this.eliberatde = eliberatDe;
    }

    @Column(name = "dataeliberarii")
    public String getDataEliberarii() {
        return dataeliberarii;
    }
    public void setDataEliberarii(String dataEliberarii) {
        this.dataeliberarii = dataEliberarii;
    }

    @Column(name = "loculnasterii")
    public String getLoculNasterii() {
        return loculnasterii;
    }
    public void setLoculNasterii(String loculNasterii) {
        this.loculnasterii = loculNasterii;
    }

}

