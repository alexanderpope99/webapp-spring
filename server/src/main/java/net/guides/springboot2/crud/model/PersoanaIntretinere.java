package net.guides.springboot2.crud.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "persoanaintretinere")
public class PersoanaIntretinere {

    private long id;
    @Column(name = "nume")
    private String nume;
    @Column(name = "prenume")
    private String prenume;
    @Column(name = "cnp")
    private String cnp;
    @Column(name = "datanasterii")
    private Date datanasterii;
    @Column(name = "grad")
    private String grad;
    @Column(name = "gradinvaliditate")
    private String gradinvaliditate;
    @Column(name = "intretinut")
    private Boolean intretinut;
    @Column(name = "coasigurat")
    private Boolean coasigurat;
    @Column(name = "idangajat")
    private Long idaangajat;

    public PersoanaIntretinere() { }

    public PersoanaIntretinere( String nume, String prenume, String cnp, Date datanasterii, String grad, String gradinvaliditate, Boolean intretinut, Boolean coasigurat, Long idangajat ) {
        this.nume = nume;
        this.prenume = prenume;
        this.cnp = cnp;
        this.datanasterii = datanasterii;
        this.grad = grad;
        this.gradinvaliditate = gradinvaliditate;
        this.intretinut = intretinut;
        this.coasigurat = coasigurat;
        this.idaangajat = idangajat;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getCnp() {
        return cnp;
    }
    public Boolean getCoasigurat() {
        return coasigurat;
    }
    public Date getDatanasterii() {
        return datanasterii;
    }
    public String getGrad() {
        return grad;
    }
    public String getGradinvaliditate() {
        return gradinvaliditate;
    }
    public Long getIdaangajat() {
        return idaangajat;
    }
    public Boolean getIntretinut() {
        return intretinut;
    }
    public String getNume() {
        return nume;
    }
    public String getPrenume() {
        return prenume;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }
    public void setCoasigurat(Boolean coasigurat) {
        this.coasigurat = coasigurat;
    }
    public void setDatanasterii(Date datanasterii) {
        this.datanasterii = datanasterii;
    }
    public void setGrad(String grad) {
        this.grad = grad;
    }
    public void setGradinvaliditate(String gradinvaliditate) {
        this.gradinvaliditate = gradinvaliditate;
    }
    public void setIdaangajat(Long idaangajat) {
        this.idaangajat = idaangajat;
    }
    public void setIntretinut(Boolean intretinut) {
        this.intretinut = intretinut;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }
}

