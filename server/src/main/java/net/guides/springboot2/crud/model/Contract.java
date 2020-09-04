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
    private int idpunctlucru;
    private int idcentrucost;
    private int idechipa;
    private int iddepartament;
    private boolean functiedebaza;
    private boolean calculdeduceri;
    private boolean studiisuperioare;
    private String normalucru;
    private double salariutarifar;
    private String monedasalariu;
    private String modplata;
    private String conditiimunca;
    private boolean pensieprivata;
    private double cotizatiepensieprivata;
    private double avans;
    private String monedaavans;
    private int zilecoan;
    private Date ultimazilucru;
    private String casasanatate;
    private String gradinvaliditate;
    private String functie;
    private String nivelstudii;
    private String cor;
    private boolean sindicat;
    private double cotizatiesindicat;
    private String spor;
    private boolean pensionar;



    public Contract() {

    }

    public Contract(String tip, String nr, String marca, Date data, Date dataincepere, int idpunctlucru, int idcentrucost, int idechipa, int iddepartament, boolean functiedebaza, boolean calculdeduceri, boolean studiisuperioare, String normalucru, double salariutarifar, String monedasalariu, String modplata, String conditiimunca, boolean pensieprivata, double cotizatiepensieprivata, double avans, String monedaavans, int zilecoan, Date ultimazilucru, String casasanatate, String gradinvaliditate, String functie, String nivelstudii, String cor, boolean sindicat, double cotizatiesindicat, String spor, boolean pensionar) {
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
    public int getIdpunctlucru() {
        return idpunctlucru;
    }

    public void setIdpunctlucru(int idpunctlucru) {
        this.idpunctlucru = idpunctlucru;
    }

    @Column(name="idcentrucost")
    public int getIdcentrucost() {
        return idcentrucost;
    }

    public void setIdcentrucost(int idcentrucost) {
        this.idcentrucost = idcentrucost;
    }

    @Column(name="echipa")
    public int getIdechipa() {
        return idechipa;
    }

    public void setIdechipa(int idechipa) {
        this.idechipa = idechipa;
    }

    @Column(name="iddepartament")
    public int getIddepartament() {
        return iddepartament;
    }

    public void setIddepartament(int iddepartament) {
        this.iddepartament = iddepartament;
    }

    @Column(name="functiedebaza")
    public boolean isFunctiedebaza() {
        return functiedebaza;
    }

    public void setFunctiedebaza(boolean functiedebaza) {
        this.functiedebaza = functiedebaza;
    }

    @Column(name="calculdeduceri")
    public boolean isCalculdeduceri() {
        return calculdeduceri;
    }

    public void setCalculdeduceri(boolean calculdeduceri) {
        this.calculdeduceri = calculdeduceri;
    }

    @Column(name="studiisuperioare")
    public boolean isStudiisuperioare() {
        return studiisuperioare;
    }

    public void setStudiisuperioare(boolean studiisuperioare) {
        this.studiisuperioare = studiisuperioare;
    }

    @Column(name="normalucru")
    public String getNormalucru() {
        return normalucru;
    }

    public void setNormalucru(String normalucru) {
        this.normalucru = normalucru;
    }

    @Column(name="salariutarifar")
    public double getSalariutarifar() {
        return salariutarifar;
    }

    public void setSalariutarifar(double salariutarifar) {
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
    public boolean isPensieprivata() {
        return pensieprivata;
    }

    public void setPensieprivata(boolean pensieprivata) {
        this.pensieprivata = pensieprivata;
    }

    @Column(name="cotizatiepensieprivata")
    public double getCotizatiepensieprivata() {
        return cotizatiepensieprivata;
    }

    public void setCotizatiepensieprivata(double cotizatiepensieprivata) {
        this.cotizatiepensieprivata = cotizatiepensieprivata;
    }

    @Column(name="avans")
    public double getAvans() {
        return avans;
    }

    public void setAvans(double avans) {
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
    public int getZilecoan() {
        return zilecoan;
    }

    public void setZilecoan(int zilecoan) {
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
    public boolean isSindicat() {
        return sindicat;
    }

    public void setSindicat(boolean sindicat) {
        this.sindicat = sindicat;
    }

    @Column(name="cotizatiesindicat")
    public double getCotizatiesindicat() {
        return cotizatiesindicat;
    }

    public void setCotizatiesindicat(double cotizatiesindicat) {
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
    public boolean isPensionar() {
        return pensionar;
    }

    public void setPensionar(boolean pensionar) {
        this.pensionar = pensionar;
    }
}

