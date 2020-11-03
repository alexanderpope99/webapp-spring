package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "contract")
public class Contract {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "tip")
	private String tip;

	@Column(name = "nr")
	private String nr;

	@Column(name = "marca")
	private String marca;

	@Column(name = "data")
	private Date data;

	@Column(name = "dataincepere")
	private Date dataincepere;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idpunctlucru")
	private PunctDeLucru idpunctlucru;

	@ManyToOne
	@JoinColumn(name = "idcentrucost")
	private CentruCost idcentrucost;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idechipa")
	private Echipa idechipa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iddepartament")
	private Departament iddepartament;

	@Column(name = "functiedebaza")
	private Boolean functiedebaza;

	@Column(name = "calculdeduceri")
	private Boolean calculdeduceri;

	@Column(name = "studiisuperioare")
	private Boolean studiisuperioare;

	@Column(name = "normalucru")
	private Integer normalucru;

	@Column(name = "salariutarifar")
	private Float salariutarifar;

	@Column(name = "monedasalariu")
	private String monedasalariu;

	@Column(name = "modplata")
	private String modplata;

	@Column(name = "conditiimunca")
	private String conditiimunca;

	@Column(name = "pensieprivata")
	private Boolean pensieprivata;

	@Column(name = "cotizatiepensieprivata")
	private Double cotizatiepensieprivata;

	@Column(name = "avans")
	private Double avans;

	@Column(name = "monedaavans")
	private String monedaavans;

	@Column(name = "zilecoan")
	private Integer zilecoan;

	@Column(name = "ultimazilucru")
	private Date ultimazilucru;

	@Column(name = "casasanatate")
	private String casasanatate;

	@Column(name = "gradinvaliditate")
	private String gradinvaliditate;

	@Column(name = "functie")
	private String functie;

	@Column(name = "nivelstudii")
	private String nivelstudii;

	@Column(name = "cor")
	private String cor;

	@Column(name = "sindicat")
	private Boolean sindicat;

	@Column(name = "cotizatiesindicat")
	private Double cotizatiesindicat;

	@Column(name = "spor")
	private String spor;

	@Column(name = "pensionar")
	private Boolean pensionar;

	@OneToMany(mappedBy = "idcontract")
	private Set<AlteBeneficii> alteBeneficii;

	@OneToOne(mappedBy = "idcontract", fetch = FetchType.LAZY)
	private Angajat angajati;

	@OneToMany(mappedBy = "idcontract")
	private Set<BursePrivate> bursePrivate;

	@OneToMany(mappedBy = "idcontract")
	private Set<CM> concediiMedicale;

	@OneToMany(mappedBy = "idcontract")
	private Set<CO> concediiDeOdihna;

	@OneToMany(mappedBy = "idcontract")
	private Set<Condica> condici;

	@OneToMany(mappedBy = "idcontract")
	private Set<Prime> prime;

	@OneToMany(mappedBy = "idcontract")
	private Set<RealizariRetineri> realizariRetineri;

	@OneToMany(mappedBy = "idcontract")
	private Set<Sponsorizari> sponsorizari;

	@OneToMany(mappedBy = "idcontract")
	private Set<SporPermanent> sporPermanente;

	@OneToMany(mappedBy = "idcontract")
	private Set<ZileCODisponibile> zileCODisponibile;

	public Contract() {

	}

	public Contract(String tip, String nr, String marca, Date data, Date dataincepere, PunctDeLucru idpunctlucru,
			CentruCost idcentrucost, Departament iddepartament, Boolean functiedebaza, Boolean calculdeduceri,
			Boolean studiisuperioare, Integer normalucru, Float salariutarifar, String monedasalariu, String modplata,
			String conditiimunca, Boolean pensieprivata, Double cotizatiepensieprivata, Double avans,
			String monedaavans, Integer zilecoan, Date ultimazilucru, String casasanatate, String gradinvaliditate,
			String functie, String nivelstudii, String cor, Boolean sindicat, Double cotizatiesindicat, String spor,
			Boolean pensionar) {
		this.tip = tip;
		this.nr = nr;
		this.marca = marca;
		this.data = data;
		this.dataincepere = dataincepere;
		this.idpunctlucru = idpunctlucru;
		this.idcentrucost = idcentrucost;
		// this.idechipa = idechipa;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataincepere() {
		return dataincepere;
	}

	public void setDataincepere(Date dataincepere) {
		this.dataincepere = dataincepere;
	}

	public PunctDeLucru getIdpunctlucru() {
		return idpunctlucru;
	}

	public void setIdpunctlucru(PunctDeLucru idpunctlucru) {
		this.idpunctlucru = idpunctlucru;
	}

	public CentruCost getIdcentrucost() {
		return idcentrucost;
	}

	public void setIdcentrucost(CentruCost idcentrucost) {
		this.idcentrucost = idcentrucost;
	}

	public Echipa getIdechipa() {
		return idechipa;
	}

	public void setIdechipa(Echipa idechipa) {
		this.idechipa = idechipa;
	}

	public Departament getIddepartament() {
		return iddepartament;
	}

	public void setIddepartament(Departament iddepartament) {
		this.iddepartament = iddepartament;
	}

	public Boolean isFunctiedebaza() {
		return functiedebaza;
	}

	public void setFunctiedebaza(Boolean functiedebaza) {
		this.functiedebaza = functiedebaza;
	}

	public Boolean isCalculdeduceri() {
		return calculdeduceri;
	}

	public void setCalculdeduceri(Boolean calculdeduceri) {
		this.calculdeduceri = calculdeduceri;
	}

	public Boolean isStudiisuperioare() {
		return studiisuperioare;
	}

	public void setStudiisuperioare(Boolean studiisuperioare) {
		this.studiisuperioare = studiisuperioare;
	}

	public Integer getNormalucru() {
		return normalucru;
	}

	public void setNormalucru(Integer normalucru) {
		this.normalucru = normalucru;
	}

	public Float getSalariutarifar() {
		return salariutarifar;
	}

	public void setSalariutarifar(Float salariutarifar) {
		this.salariutarifar = salariutarifar;
	}

	public String getMonedasalariu() {
		return monedasalariu;
	}

	public void setMonedasalariu(String monedasalariu) {
		this.monedasalariu = monedasalariu;
	}

	public String getModplata() {
		return modplata;
	}

	public void setModplata(String modplata) {
		this.modplata = modplata;
	}

	public String getConditiimunca() {
		return conditiimunca;
	}

	public void setConditiimunca(String conditiimunca) {
		this.conditiimunca = conditiimunca;
	}

	public Boolean isPensieprivata() {
		return pensieprivata;
	}

	public void setPensieprivata(Boolean pensieprivata) {
		this.pensieprivata = pensieprivata;
	}

	public Double getCotizatiepensieprivata() {
		return cotizatiepensieprivata;
	}

	public void setCotizatiepensieprivata(Double cotizatiepensieprivata) {
		this.cotizatiepensieprivata = cotizatiepensieprivata;
	}

	public Double getAvans() {
		return avans;
	}

	public void setAvans(Double avans) {
		this.avans = avans;
	}

	public String getMonedaavans() {
		return monedaavans;
	}

	public void setMonedaavans(String monedaavans) {
		this.monedaavans = monedaavans;
	}

	public Integer getZilecoan() {
		return zilecoan;
	}

	public void setZilecoan(Integer zilecoan) {
		this.zilecoan = zilecoan;
	}

	public Date getUltimazilucru() {
		return ultimazilucru;
	}

	public void setUltimazilucru(Date ultimazilucru) {
		this.ultimazilucru = ultimazilucru;
	}

	public String getCasasanatate() {
		return casasanatate;
	}

	public void setCasasanatate(String casasanatate) {
		this.casasanatate = casasanatate;
	}

	public String getGradinvaliditate() {
		return gradinvaliditate;
	}

	public void setGradinvaliditate(String gradinvaliditate) {
		this.gradinvaliditate = gradinvaliditate;
	}

	public String getFunctie() {
		return functie;
	}

	public void setFunctie(String functie) {
		this.functie = functie;
	}

	public String getNivelstudii() {
		return nivelstudii;
	}

	public void setNivelstudii(String nivelstudii) {
		this.nivelstudii = nivelstudii;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public Boolean isSindicat() {
		return sindicat;
	}

	public void setSindicat(Boolean sindicat) {
		this.sindicat = sindicat;
	}

	public Double getCotizatiesindicat() {
		return cotizatiesindicat;
	}

	public void setCotizatiesindicat(Double cotizatiesindicat) {
		this.cotizatiesindicat = cotizatiesindicat;
	}

	public String getSpor() {
		return spor;
	}

	public void setSpor(String spor) {
		this.spor = spor;
	}

	public Boolean isPensionar() {
		return pensionar;
	}

	public void setPensionar(Boolean pensionar) {
		this.pensionar = pensionar;
	}

	public Set<AlteBeneficii> getAlteBeneficii() {
		return alteBeneficii;
	}

	public void setAlteBeneficii(Set<AlteBeneficii> alteBeneficii) {
		this.alteBeneficii = alteBeneficii;
	}

	public Angajat getAngajati() {
		return angajati;
	}

	public void setAngajati(Angajat angajat) {
		this.angajati = angajat;
	}

	public Set<BursePrivate> getBursePrivate() {
		return bursePrivate;
	}

	public Boolean getCalculdeduceri() {
		return calculdeduceri;
	}

	public Boolean getFunctiedebaza() {
		return functiedebaza;
	}

	public Boolean getPensieprivata() {
		return pensieprivata;
	}

	public Boolean getPensionar() {
		return pensionar;
	}

	public Boolean getSindicat() {
		return sindicat;
	}

	public Boolean getStudiisuperioare() {
		return studiisuperioare;
	}

	public Set<CM> getConcediiMedicale() {
		return concediiMedicale;
	}

	public void setBursePrivate(Set<BursePrivate> bursePrivate) {
		this.bursePrivate = bursePrivate;
	}

	public void setConcediiMedicale(Set<CM> concediiMedicale) {
		this.concediiMedicale = concediiMedicale;
	}

	public Set<CO> getConcediiDeOdihna() {
		return concediiDeOdihna;
	}

	public void setConcediiDeOdihna(Set<CO> concediiDeOdihna) {
		this.concediiDeOdihna = concediiDeOdihna;
	}

	public Set<Condica> getCondici() {
		return condici;
	}

	public void setCondici(Set<Condica> condici) {
		this.condici = condici;
	}

	public Set<Prime> getPrime() {
		return prime;
	}

	public void setPrime(Set<Prime> prime) {
		this.prime = prime;
	}

	public Set<RealizariRetineri> getRealizariRetineri() {
		return realizariRetineri;
	}

	public void setRealizariRetineri(Set<RealizariRetineri> realizariRetineri) {
		this.realizariRetineri = realizariRetineri;
	}

	public Set<Sponsorizari> getSponsorizari() {
		return sponsorizari;
	}

	public void setSponsorizari(Set<Sponsorizari> sponsorizari) {
		this.sponsorizari = sponsorizari;
	}

	public Set<SporPermanent> getSporPermanente() {
		return sporPermanente;
	}

	public void setSporPermanente(Set<SporPermanent> sporPermanente) {
		this.sporPermanente = sporPermanente;
	}

	public Set<ZileCODisponibile> getZileCODisponibile() {
		return zileCODisponibile;
	}

	public void setZileCODisponibile(Set<ZileCODisponibile> zileCODisponibile) {
		this.zileCODisponibile = zileCODisponibile;
	}
}
