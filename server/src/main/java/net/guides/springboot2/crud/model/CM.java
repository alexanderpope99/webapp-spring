package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "cm")
public class CM {
    private long id;
    private LocalDate dela;
    private LocalDate panala;
    private Boolean continuare;
    private LocalDate datainceput;
    private String serienrcertificat;
    private String dataeliberare;
    private String codurgenta;
    private Double procent;
    private String codboalainfcont;
    private Double bazacalcul;
    private Double bazacalculplafonata;
    private Integer zilebazacalcul;
    private Double mediezilnica;
    private Integer zilefirma;
    private Double indemnizatiefirma;
    private Integer zilefnuass;
    private Double indemnizatiefnuass;
    private String locprescriere;
    private String nravizmedic;
    private String codboala;
    private Boolean urgenta;
    private String conditii;
    private Long idcontract;

    public CM() { }

    public CM(LocalDate dela, LocalDate panala, Boolean continuare, LocalDate datainceput, String serienrcertificat, String dataeliberare, String codurgenta, Double procent, String codboalainfcont, Double bazacalcul, Double bazacalculplafonata, Integer zilebazacalcul, Double mediezilnica, Integer zilefirma, Double indemnizatiefirma, Integer zilefnuass, Double indemnizatiefnuass, String locprescriere, String nravizmedic, String codboala, Boolean urgenta, String conditii, Long idcontract) {
        this.dela = dela;
        this.panala = panala;
        this.continuare = continuare;
        this.datainceput = datainceput;
        this.serienrcertificat = serienrcertificat;
        this.dataeliberare = dataeliberare;
        this.codurgenta = codurgenta;
        this.procent = procent;
        this.codboalainfcont = codboalainfcont;
        this.bazacalcul = bazacalcul;
        this.bazacalculplafonata = bazacalculplafonata;
        this.zilebazacalcul = zilebazacalcul;
        this.mediezilnica = mediezilnica;
        this.zilefirma = zilefirma;
        this.indemnizatiefirma = indemnizatiefirma;
        this.zilefnuass = zilefnuass;
        this.indemnizatiefnuass = indemnizatiefnuass;
        this.locprescriere = locprescriere;
        this.nravizmedic = nravizmedic;
        this.codboala = codboala;
        this.urgenta = urgenta;
        this.conditii = conditii;
        this.idcontract = idcontract;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "dela")
    public LocalDate getDela() {
        return dela;
    }

    public void setDela(LocalDate dela) {
        this.dela = dela;
    }

    @Column(name = "panala")
    public LocalDate getPanala() {
        return panala;
    }

    public void setPanala(LocalDate panala) {
        this.panala = panala;
    }

    @Column(name = "continuare")
    public Boolean isContinuare() {
        return continuare;
    }

    public void setContinuare(Boolean continuare) {
        this.continuare = continuare;
    }

    @Column(name = "datainceput")
    public LocalDate getDatainceput() {
        return datainceput;
    }

    public void setDatainceput(LocalDate datainceput) {
        this.datainceput = datainceput;
    }

    @Column(name = "serienrcertificat")
    public String getSerienrcertificat() {
        return serienrcertificat;
    }

    public void setSerienrcertificat(String serienrcertificat) {
        this.serienrcertificat = serienrcertificat;
    }

    @Column(name = "dataeliberare")
    public String getDataeliberare() {
        return dataeliberare;
    }

    public void setDataeliberare(String dataeliberare) {
        this.dataeliberare = dataeliberare;
    }

    @Column(name = "codurgenta")
    public String getCodurgenta() {
        return codurgenta;
    }

    public void setCodurgenta(String codurgenta) {
        this.codurgenta = codurgenta;
    }

    @Column(name = "procent")
    public Double getProcent() {
        return procent;
    }

    public void setProcent(Double procent) {
        this.procent = procent;
    }

    @Column(name = "codboalainfcont")
    public String getCodboalainfcont() {
        return codboalainfcont;
    }

    public void setCodboalainfcont(String codboalainfcont) {
        this.codboalainfcont = codboalainfcont;
    }

    @Column(name = "bazacalcul")
    public Double getBazacalcul() {
        return bazacalcul;
    }

    public void setBazacalcul(Double bazacalcul) {
        this.bazacalcul = bazacalcul;
    }

    @Column(name = "bazacalculplafonata")
    public Double getBazacalculplafonata() {
        return bazacalculplafonata;
    }

    public void setBazacalculplafonata(Double bazacalculplafonata) {
        this.bazacalculplafonata = bazacalculplafonata;
    }

    @Column(name = "zilebazacalcul")
    public Integer getZilebazacalcul() {
        return zilebazacalcul;
    }

    public void setZilebazacalcul(Integer zilebazacalcul) {
        this.zilebazacalcul = zilebazacalcul;
    }

    @Column(name = "mediezilnica")
    public Double getMediezilnica() {
        return mediezilnica;
    }

    public void setMediezilnica(Double mediezilnica) {
        this.mediezilnica = mediezilnica;
    }

    @Column(name = "zilefirma")
    public Integer getZilefirma() {
        return zilefirma;
    }

    public void setZilefirma(Integer zilefirma) {
        this.zilefirma = zilefirma;
    }

    @Column(name = "indemnizatiefirma")
    public Double getIndemnizatiefirma() {
        return indemnizatiefirma;
    }

    public void setIndemnizatiefirma(Double indemnizatiefirma) {
        this.indemnizatiefirma = indemnizatiefirma;
    }

    @Column(name = "zilefnuass")
    public Integer getZilefnuass() {
        return zilefnuass;
    }

    public void setZilefnuass(Integer zilefnuass) {
        this.zilefnuass = zilefnuass;
    }

    @Column(name = "indemnizatiefnuass")
    public Double getIndemnizatiefnuass() {
        return indemnizatiefnuass;
    }

    public void setIndemnizatiefnuass(Double indemnizatiefnuass) {
        this.indemnizatiefnuass = indemnizatiefnuass;
    }

    @Column(name = "locprescriere")
    public String getLocprescriere() {
        return locprescriere;
    }

    public void setLocprescriere(String locprescriere) {
        this.locprescriere = locprescriere;
    }

    @Column(name = "nravizmedic")
    public String getNravizmedic() {
        return nravizmedic;
    }

    public void setNravizmedic(String nravizmedic) {
        this.nravizmedic = nravizmedic;
    }

    @Column(name = "codboala")
    public String getCodboala() {
        return codboala;
    }

    public void setCodboala(String codboala) {
        this.codboala = codboala;
    }

    @Column(name = "urgenta")
    public Boolean isUrgenta() {
        return urgenta;
    }

    public void setUrgenta(Boolean urgenta) {
        this.urgenta = urgenta;
    }

    @Column(name = "conditii")
    public String getConditii() {
        return conditii;
    }

    public void setConditii(String conditii) {
        this.conditii = conditii;
    }

    @Column(name = "idcontract")
    public Long getIdcontract() {
        return idcontract;
    }

    public void setIdcontract(Long idcontract) {
        this.idcontract = idcontract;
    }
}

