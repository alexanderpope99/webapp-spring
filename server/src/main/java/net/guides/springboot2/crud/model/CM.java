package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "cm")
public class CM {

    private long id;
    private String tip;
    private Date dela;
    private Date panala;
    private boolean continuare;
    private Date datainceput;
    private String serienrcertificat;
    private String dataeliberare;
    private String codurgenta;
    private double procent;
    private String codboalainfcont;
    private double bazacalcul;
    private double bazacalculplafonata;
    private int zilebazacalcul;
    private double mediezilnica;
    private int zilefirma;
    private double indemnizatiefirma;
    private int zilefnuass;
    private double indemnizatiefnuass;
    private String locprescriere;
    private String nravizmedic;
    private String codboala;
    private boolean urgenta;
    private String conditii;
    private int idcontract;



    public CM() {

    }

    public CM(String tip, Date dela, Date panala, boolean continuare, Date datainceput, String serienrcertificat, String dataeliberare, String codurgenta, double procent, String codboalainfcont, double bazacalcul, double bazacalculplafonata, int zilebazacalcul, double mediezilnica, int zilefirma, double indemnizatiefirma, int zilefnuass, double indemnizatiefnuass, String locprescriere, String nravizmedic, String codboala, boolean urgenta, String conditii, int idcontract) {
        this.tip = tip;
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

    @Column(name = "tip")
    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Column(name = "dela")
    public Date getDela() {
        return dela;
    }

    public void setDela(Date dela) {
        this.dela = dela;
    }

    @Column(name = "panala")
    public Date getPanala() {
        return panala;
    }

    public void setPanala(Date panala) {
        this.panala = panala;
    }

    @Column(name = "contiuare")
    public boolean isContinuare() {
        return continuare;
    }

    public void setContinuare(boolean continuare) {
        this.continuare = continuare;
    }

    @Column(name = "datainceput")
    public Date getDatainceput() {
        return datainceput;
    }

    public void setDatainceput(Date datainceput) {
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
    public double getProcent() {
        return procent;
    }

    public void setProcent(double procent) {
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
    public double getBazacalcul() {
        return bazacalcul;
    }

    public void setBazacalcul(double bazacalcul) {
        this.bazacalcul = bazacalcul;
    }

    @Column(name = "bazacalculplafonata")
    public double getBazacalculplafonata() {
        return bazacalculplafonata;
    }

    public void setBazacalculplafonata(double bazacalculplafonata) {
        this.bazacalculplafonata = bazacalculplafonata;
    }

    @Column(name = "zilebazacalcul")
    public int getZilebazacalcul() {
        return zilebazacalcul;
    }

    public void setZilebazacalcul(int zilebazacalcul) {
        this.zilebazacalcul = zilebazacalcul;
    }

    @Column(name = "mediezilnica")
    public double getMediezilnica() {
        return mediezilnica;
    }

    public void setMediezilnica(double mediezilnica) {
        this.mediezilnica = mediezilnica;
    }

    @Column(name = "zilefirma")
    public int getZilefirma() {
        return zilefirma;
    }

    public void setZilefirma(int zilefirma) {
        this.zilefirma = zilefirma;
    }

    @Column(name = "indemnizatiefirma")
    public double getIndemnizatiefirma() {
        return indemnizatiefirma;
    }

    public void setIndemnizatiefirma(double indemnizatiefirma) {
        this.indemnizatiefirma = indemnizatiefirma;
    }

    @Column(name = "zilefnuass")
    public int getZilefnuass() {
        return zilefnuass;
    }

    public void setZilefnuass(int zilefnuass) {
        this.zilefnuass = zilefnuass;
    }

    @Column(name = "indemnizatiefnuass")
    public double getIndemnizatiefnuass() {
        return indemnizatiefnuass;
    }

    public void setIndemnizatiefnuass(double indemnizatiefnuass) {
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

    @Column(name = "isurgenta")
    public boolean isUrgenta() {
        return urgenta;
    }

    public void setUrgenta(boolean urgenta) {
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
    public int getIdcontract() {
        return idcontract;
    }

    public void setIdcontract(int idcontract) {
        this.idcontract = idcontract;
    }
}

