package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "societate")
public class Oresuplimentare {

    private long id;
    @Column(name = "nr")
    private Long nr;
    @Column(name = "procent")
    private Double procent;
    @Column(name = "includenormale")
    private Boolean includenormale;
    @Column(name = "total")
    private Double total;
    @Column(name = "idcontract")
    private Long idcontract;


    public Oresuplimentare() { }

    public Oresuplimentare( Long nr, Double procent, Boolean includenormale, Double total, Long idcontract ) {
        this.nr = nr;
        this.procent = procent;
        this.includenormale = includenormale;
        this.total = total;
        this.idcontract = idcontract;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setIdcontract(Long idcontract) {
        this.idcontract = idcontract;
    }
    public Long getIdcontract() {
        return idcontract;
    }

    public void setProcent(Double procent) {
        this.procent = procent;
    }
    public Double getProcent() {
        return procent;
    }
    public void setNr(Long nr) {
        this.nr = nr;
    }
    public Long getNr() {
        return nr;
    }
    public Boolean getIncludenormale() {
        return includenormale;
    }
    public void setIncludenormale(Boolean includenormale) {
        this.includenormale = includenormale;
    }
    public Double getTotal() {
        return total;
    }
    public void setTotal(Double total) {
        this.total = total;
    }
}

