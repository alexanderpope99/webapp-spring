package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

import net.guides.springboot2.crud.model.CentruCost;
import net.guides.springboot2.crud.model.Departament;
import net.guides.springboot2.crud.model.Echipa;
import net.guides.springboot2.crud.model.PunctDeLucru;

public class ContractDTO {
	private int id;

	private String tip;

	private String nr;

	private String marca;

	private LocalDate data;

	private LocalDate dataincepere;

	private PunctDeLucru punctlucru;

	private CentruCost centrucost;

	private Echipa echipa;

	private Departament departament;

	private Boolean functiedebaza;

	private Boolean calculdeduceri;

	private Boolean studiisuperioare;

	private Integer normalucru;

	private Float salariutarifar;

	private String monedasalariu;

	private String modplata;

	private String conditiimunca;

	private Boolean pensieprivata;

	private Double cotizatiepensieprivata;

	private Double avans;

	private String monedaavans;

	private Integer zilecoan;

	private LocalDate ultimazilucru;

	private String casasanatate;

	private String gradinvaliditate;

	private String functie;

	private String nivelstudii;

	private String cor;

	private Boolean sindicat;

	private Double cotizatiesindicat;

	private String spor;

	private Boolean pensionar;

	public Double getAvans() {
		return avans;
	}

	public Boolean getCalculdeduceri() {
		return calculdeduceri;
	}

	public String getCasasanatate() {
		return casasanatate;
	}

	public String getConditiimunca() {
		return conditiimunca;
	}

	public String getCor() {
		return cor;
	}

	public Double getCotizatiepensieprivata() {
		return cotizatiepensieprivata;
	}

	public Double getCotizatiesindicat() {
		return cotizatiesindicat;
	}

	public LocalDate getData() {
		return data;
	}

	public LocalDate getDataincepere() {
		return dataincepere;
	}

	public String getFunctie() {
		return functie;
	}

	public Boolean getFunctiedebaza() {
		return functiedebaza;
	}

	public String getGradinvaliditate() {
		return gradinvaliditate;
	}

	public int getId() {
		return id;
	}

	public Integer getIdcentrucost() {
		if (centrucost == null)
			return null;
		else
			return centrucost.getId();
	}

	public Integer getIddepartament() {
		if (departament == null)
			return null;
		else
			return departament.getId();
	}

	public Integer getIdechipa() {
		if (echipa == null)
			return null;
		else
			return echipa.getId();
	}

	public Integer getIdpunctlucru() {
		if (punctlucru == null)
			return null;
		else
			return punctlucru.getId();
	}

	public String getMarca() {
		return marca;
	}

	public String getModplata() {
		return modplata;
	}

	public String getMonedaavans() {
		return monedaavans;
	}

	public String getMonedasalariu() {
		return monedasalariu;
	}

	public String getNivelstudii() {
		return nivelstudii;
	}

	public Integer getNormalucru() {
		return normalucru;
	}

	public String getNr() {
		return nr;
	}

	public Boolean getPensieprivata() {
		return pensieprivata;
	}

	public Boolean getPensionar() {
		return pensionar;
	}

	public Float getSalariutarifar() {
		return salariutarifar;
	}

	public Boolean getSindicat() {
		return sindicat;
	}

	public String getSpor() {
		return spor;
	}

	public Boolean getStudiisuperioare() {
		return studiisuperioare;
	}

	public String getTip() {
		return tip;
	}

	public LocalDate getUltimazilucru() {
		return ultimazilucru;
	}

	public Integer getZilecoan() {
		return zilecoan;
	}

	public void setAvans(Double avans) {
		this.avans = avans;
	}

	public void setCalculdeduceri(Boolean calculdeduceri) {
		this.calculdeduceri = calculdeduceri;
	}

	public void setCasasanatate(String casasanatate) {
		this.casasanatate = casasanatate;
	}

	public void setConditiimunca(String conditiimunca) {
		this.conditiimunca = conditiimunca;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public void setCotizatiepensieprivata(Double cotizatiepensieprivata) {
		this.cotizatiepensieprivata = cotizatiepensieprivata;
	}

	public void setCotizatiesindicat(Double cotizatiesindicat) {
		this.cotizatiesindicat = cotizatiesindicat;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public void setDataincepere(LocalDate dataincepere) {
		this.dataincepere = dataincepere;
	}

	public void setFunctie(String functie) {
		this.functie = functie;
	}

	public void setFunctiedebaza(Boolean functiedebaza) {
		this.functiedebaza = functiedebaza;
	}

	public void setGradinvaliditate(String gradinvaliditate) {
		this.gradinvaliditate = gradinvaliditate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCentrucost(CentruCost idcentrucost) {
		this.centrucost = idcentrucost;
	}

	public void setDepartament(Departament iddepartament) {
		this.departament = iddepartament;
	}

	public void setEchipa(Echipa idechipa) {
		this.echipa = idechipa;
	}

	public void setPunctlucru(PunctDeLucru idpunctlucru) {
		this.punctlucru = idpunctlucru;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public void setModplata(String modplata) {
		this.modplata = modplata;
	}

	public void setMonedaavans(String monedaavans) {
		this.monedaavans = monedaavans;
	}

	public void setMonedasalariu(String monedasalariu) {
		this.monedasalariu = monedasalariu;
	}

	public void setNivelstudii(String nivelstudii) {
		this.nivelstudii = nivelstudii;
	}

	public void setNormalucru(Integer normalucru) {
		this.normalucru = normalucru;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	public void setPensieprivata(Boolean pensieprivata) {
		this.pensieprivata = pensieprivata;
	}

	public void setPensionar(Boolean pensionar) {
		this.pensionar = pensionar;
	}

	public void setSalariutarifar(Float salariutarifar) {
		this.salariutarifar = salariutarifar;
	}

	public void setSindicat(Boolean sindicat) {
		this.sindicat = sindicat;
	}

	public void setSpor(String spor) {
		this.spor = spor;
	}

	public void setStudiisuperioare(Boolean studiisuperioare) {
		this.studiisuperioare = studiisuperioare;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public void setUltimazilucru(LocalDate ultimazilucru) {
		this.ultimazilucru = ultimazilucru;
	}

	public void setZilecoan(Integer zilecoan) {
		this.zilecoan = zilecoan;
	}

}
