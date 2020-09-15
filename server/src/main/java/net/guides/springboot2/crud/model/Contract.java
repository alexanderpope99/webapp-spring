package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "contract")
public class Contract {

    private long id;
    private String tip;
    private String nr;
    private String marca;
    private Date data;
    private Date dataincepere;
    private Integer idpunctlucru;
    private Integer idcentrucost;
    private Integer idechipa;
    private Integer iddepartament;
    private Boolean functiedebaza;
    private Boolean calculdeduceri;
    private Boolean studiisuperioare;
    private Integer normalucru;
    private Double salariutarifar;
    private String monedasalariu;
    private String modplata;
    private String conditiimunca;
    private Boolean pensieprivata;
    private Double cotizatiepensieprivata;
    private Double avans;
    private String monedaavans;
    private Integer zilecoan;
    private Date ultimazilucru;
    private String casasanatate;
    private String gradinvaliditate;
    private String functie;
    private String nivelstudii;
    private String cor;
    private Boolean sindicat;
    private Double cotizatiesindicat;
    private String spor;
    private Boolean pensionar;



    public Contract() {

    }

    public Contract(String tip, String nr, String marca, Date data, Date dataincepere, Integer idpunctlucru, Integer idcentrucost, Integer idechipa, Integer iddepartament, Boolean functiedebaza, Boolean calculdeduceri, Boolean studiisuperioare, Integer normalucru, Double salariutarifar, String monedasalariu, String modplata, String conditiimunca, Boolean pensieprivata, Double cotizatiepensieprivata, Double avans, String monedaavans, Integer zilecoan, Date ultimazilucru, String casasanatate, String gradinvaliditate, String functie, String nivelstudii, String cor, Boolean sindicat, Double cotizatiesindicat, String spor, Boolean pensionar) {
        this.tip = tip;
        this.nr = nr;
        this.marca = marca;
        this.data = data;
        this.dataincepere = dataincepere;
        this.idpunctlucru = idpunctlucru;
        this.idcentrucost = idcentrucost;
        this.idechipa = idechipa;
        this.iddepartament = iddepartament;
        this.functiedebaza = functiedebaza;
        this.calculdeduceri = calculdeduceri;
        this.studiisuperioare = studiisuperioare;
        this.normalucru = normalucru;
        this.salariutarifar = salariutarifar;
        this.monedasalariu = monedasalariu;
        this.modplata = modplata;
        this.conditiimunca = conditiimunca;
        this.pensieprivata = pensieprivata;
        this.cotizatiepensieprivata = cotizatiepensieprivata;
        this.avans = avans;
        this.monedaavans = monedaavans;
        this.zilecoan = zilecoan;
        this.ultimazilucru = ultimazilucru;
        this.casasanatate = casasanatate;
        this.gradinvaliditate = gradinvaliditate;
        this.functie = functie;
        this.nivelstudii = nivelstudii;
        this.cor = cor;
        this.sindicat = sindicat;
        this.cotizatiesindicat = cotizatiesindicat;
        this.spor = spor;
        this.pensionar = pensionar;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name="tip")
    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Column(name="nr")
    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    @Column(name="marca")
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Column(name="data")
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Column(name="dataincepere")
    public Date getDataincepere() {
        return dataincepere;
    }

    public void setDataincepere(Date dataincepere) {
        this.dataincepere = dataincepere;
    }

    @Column(name="idpunctlucru")
    public Integer getIdpunctlucru() {
        return idpunctlucru;
    }

    public void setIdpunctlucru(Integer idpunctlucru) {
        this.idpunctlucru = idpunctlucru;
    }

    @Column(name="idcentrucost")
    public Integer getIdcentrucost() {
        return idcentrucost;
    }

    public void setIdcentrucost(Integer idcentrucost) {
        this.idcentrucost = idcentrucost;
    }

    @Column(name="echipa")
    public Integer getIdechipa() {
        return idechipa;
    }

    public void setIdechipa(Integer idechipa) {
        this.idechipa = idechipa;
    }

    @Column(name="iddepartament")
    public Integer getIddepartament() {
        return iddepartament;
    }

    public void setIddepartament(Integer iddepartament) {
        this.iddepartament = iddepartament;
    }

    @Column(name="functiedebaza")
    public Boolean isFunctiedebaza() {
        return functiedebaza;
    }

    public void setFunctiedebaza(Boolean functiedebaza) {
        this.functiedebaza = functiedebaza;
    }

    @Column(name="calculdeduceri")
    public Boolean isCalculdeduceri() {
        return calculdeduceri;
    }

    public void setCalculdeduceri(Boolean calculdeduceri) {
        this.calculdeduceri = calculdeduceri;
    }

    @Column(name="studiisuperioare")
    public Boolean isStudiisuperioare() {
        return studiisuperioare;
    }

    public void setStudiisuperioare(Boolean studiisuperioare) {
        this.studiisuperioare = studiisuperioare;
    }

    @Column(name="normalucru")
    public Integer getNormalucru() {
        return normalucru;
    }

    public void setNormalucru(Integer normalucru) {
        this.normalucru = normalucru;
    }

    @Column(name="salariutarifar")
    public Double getSalariutarifar() {
        return salariutarifar;
    }

    public void setSalariutarifar(Double salariutarifar) {
        this.salariutarifar = salariutarifar;
    }

    @Column(name="monedasalariu")
    public String getMonedasalariu() {
        return monedasalariu;
    }

    public void setMonedasalariu(String monedasalariu) {
        this.monedasalariu = monedasalariu;
    }

    @Column(name="modplata")
    public String getModplata() {
        return modplata;
    }

    public void setModplata(String modplata) {
        this.modplata = modplata;
    }

    @Column(name="conditiimunca")
    public String getConditiimunca() {
        return conditiimunca;
    }

    public void setConditiimunca(String conditiimunca) {
        this.conditiimunca = conditiimunca;
    }

    @Column(name="pensieprivata")
    public Boolean isPensieprivata() {
        return pensieprivata;
    }

    public void setPensieprivata(Boolean pensieprivata) {
        this.pensieprivata = pensieprivata;
    }

    @Column(name="cotizatiepensieprivata")
    public Double getCotizatiepensieprivata() {
        return cotizatiepensieprivata;
    }

    public void setCotizatiepensieprivata(Double cotizatiepensieprivata) {
        this.cotizatiepensieprivata = cotizatiepensieprivata;
    }

    @Column(name="avans")
    public Double getAvans() {
        return avans;
    }

    public void setAvans(Double avans) {
        this.avans = avans;
    }

    @Column(name="monedaavans")
    public String getMonedaavans() {
        return monedaavans;
    }

    public void setMonedaavans(String monedaavans) {
        this.monedaavans = monedaavans;
    }

    @Column(name="zilecoan")
    public Integer getZilecoan() {
        return zilecoan;
    }

    public void setZilecoan(Integer zilecoan) {
        this.zilecoan = zilecoan;
    }

    @Column(name="ultimazilucru")
    public Date getUltimazilucru() {
        return ultimazilucru;
    }

    public void setUltimazilucru(Date ultimazilucru) {
        this.ultimazilucru = ultimazilucru;
    }

    @Column(name="casasanatate")
    public String getCasasanatate() {
        return casasanatate;
    }

    public void setCasasanatate(String casasanatate) {
        this.casasanatate = casasanatate;
    }

    @Column(name="gradinvaliditate")
    public String getGradinvaliditate() {
        return gradinvaliditate;
    }

    public void setGradinvaliditate(String gradinvaliditate) {
        this.gradinvaliditate = gradinvaliditate;
    }

    @Column(name="functie")
    public String getFunctie() {
        return functie;
    }

    public void setFunctie(String functie) {
        this.functie = functie;
    }

    @Column(name="nivelstudii")
    public String getNivelstudii() {
        return nivelstudii;
    }

    public void setNivelstudii(String nivelstudii) {
        this.nivelstudii = nivelstudii;
    }

    @Column(name="cor")
    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    @Column(name="sindicat")
    public Boolean isSindicat() {
        return sindicat;
    }

    public void setSindicat(Boolean sindicat) {
        this.sindicat = sindicat;
    }

    @Column(name="cotizatiesindicat")
    public Double getCotizatiesindicat() {
        return cotizatiesindicat;
    }

    public void setCotizatiesindicat(Double cotizatiesindicat) {
        this.cotizatiesindicat = cotizatiesindicat;
    }

    @Column(name="spor")
    public String getSpor() {
        return spor;
    }

    public void setSpor(String spor) {
        this.spor = spor;
    }

    @Column(name="pensionar")
    public Boolean isPensionar() {
        return pensionar;
    }

    public void setPensionar(Boolean pensionar) {
        this.pensionar = pensionar;
    }
}

