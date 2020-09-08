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
    public Date getDatanasterii() {
        return datanasterii;
    }
    public void setDatanasterii(Date datanasterii) {
        this.datanasterii = datanasterii;
    }

    @Column(name = "eliberatde")
    public String getEliberatde() {
        return eliberatde;
    }
    public void setEliberatde(String eliberatde) {
        this.eliberatde = eliberatde;
    }

    @Column(name = "dataeliberarii")
    public String getDataeliberarii() {
        return dataeliberarii;
    }
    public void setDataeliberarii(String dataeliberarii) {
        this.dataeliberarii = dataeliberarii;
    }

    @Column(name = "loculnasterii")
    public String getLoculnasterii() {
        return loculnasterii;
    }
    public void setLoculnasterii(String loculnasterii) {
        this.loculnasterii = loculnasterii;
    }

}

