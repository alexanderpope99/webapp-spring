package net.guides.springboot2.crud.model;

import javax.persistence.CascadeType;
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

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "contract")
public class Contract implements Serializable {
	private static final long serialVersionUID = 1L;

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
	private LocalDate data;
	@Column(name = "dataincepere")
	private LocalDate dataincepere;
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
	private LocalDate ultimazilucru;
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

	@ManyToOne()
	@JoinColumn(name = "idcentrucost")
	private CentruCost centrucost;

	@ManyToOne()
	@JoinColumn(name = "idpunctlucru")
	private PunctDeLucru punctdelucru;

	@ManyToOne()
	@JoinColumn(name = "idechipa")
	private Echipa echipa;

	@ManyToOne()
	@JoinColumn(name = "iddepartapent")
	private Departament departament;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idcontbancar", referencedColumnName = "iban")
	private ContBancar contbancar;

	@JsonBackReference(value = "angajat-contract")
	@OneToOne(mappedBy = "contract", fetch = FetchType.LAZY)
	private Angajat angajat;

	@JsonBackReference(value = "retineri-contract")
	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY)
	private List<RealizariRetineri> realizariRetineri;

	@JsonBackReference(value = "co-contract")
	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY)
	private List<CO> concediiOdihna;

	@JsonBackReference(value = "cm-contract")
	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY)
	private List<CM> concediiMedicale;

	public Contract() {
	}

	public Contract(String tip, String nr, String marca, LocalDate data, LocalDate dataincepere,
			PunctDeLucru punctdelucru, CentruCost centrucost, Echipa echipa, Departament departament,
			Boolean functiedebaza, Boolean calculdeduceri, Boolean studiisuperioare, Integer normalucru,
			Float salariutarifar, String monedasalariu, String conditiimunca, Boolean pensieprivata,
			Double cotizatiepensieprivata, Double avans, String monedaavans, Integer zilecoan, LocalDate ultimazilucru,
			String casasanatate, String gradinvaliditate, String functie, String nivelstudii, String cor,
			Boolean sindicat, Double cotizatiesindicat, String spor, Boolean pensionar) {
		this.tip = tip;
		this.nr = nr;
		this.marca = marca;
		this.data = data;
		this.dataincepere = dataincepere;
		this.punctdelucru = punctdelucru;
		this.centrucost = centrucost;
		this.echipa = echipa;
		this.departament = departament;
		this.functiedebaza = functiedebaza;
		this.calculdeduceri = calculdeduceri;
		this.studiisuperioare = studiisuperioare;
		this.normalucru = normalucru;
		this.salariutarifar = salariutarifar;
		this.monedasalariu = monedasalariu;
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

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalDate getDataincepere() {
		return dataincepere;
	}

	public void setDataincepere(LocalDate dataincepere) {
		this.dataincepere = dataincepere;
	}

	public CentruCost getCentrucost() {
		return centrucost;
	}

	public Departament getDepartament() {
		return departament;
	}

	public Echipa getEchipa() {
		return echipa;
	}

	public PunctDeLucru getPunctdelucru() {
		return punctdelucru;
	}

	public void setCentrucost(CentruCost centrucost) {
		this.centrucost = centrucost;
	}

	public void setDepartament(Departament departament) {
		this.departament = departament;
	}

	public void setEchipa(Echipa echipa) {
		this.echipa = echipa;
	}

	public void setPunctdelucru(PunctDeLucru punctdelucru) {
		this.punctdelucru = punctdelucru;
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

	public LocalDate getUltimazilucru() {
		return ultimazilucru;
	}

	public void setUltimazilucru(LocalDate ultimazilucru) {
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

	public ContBancar getContbancar() {
		return contbancar;
	}

	public void setContbancar(ContBancar contbancar) {
		this.contbancar = contbancar;
	}

	public List<CO> getConcediiOdihna() {
		return concediiOdihna;
	}
	public void setConcediiOdihna(List<CO> concediiOdihna) {
		this.concediiOdihna = concediiOdihna;
	}

	public List<CM> getConcediiMedicale() {
		return concediiMedicale;
	}

	public void setConcediiMedicale(List<CM> concediiMedicale) {
		this.concediiMedicale = concediiMedicale;
	}
}
