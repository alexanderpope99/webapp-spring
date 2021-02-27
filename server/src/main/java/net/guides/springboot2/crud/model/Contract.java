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

import net.guides.springboot2.crud.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Table(name = "contract")
public class Contract implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "tip", nullable = false)
	private String tip;
	@Column(name = "nr", nullable = false)
	private String nr;
	@Column(name = "marca")
	private String marca;
	@Column(name = "data", nullable = false)
	private LocalDate data;
	@Column(name = "dataincepere", nullable = false)
	private LocalDate dataincepere;
	@Column(name = "functiedebaza", nullable = false)
	private Boolean functiedebaza;
	@Column(name = "calculdeduceri", nullable = false)
	private Boolean calculdeduceri;
	@Column(name = "studiisuperioare")
	private Boolean studiisuperioare;
	@Column(name = "normalucru", nullable = false)
	private Integer normalucru;
	@Column(name = "salariutarifar", nullable = false)
	private Integer salariutarifar;
	@Column(name = "monedasalariu")
	private String monedasalariu;
	@Column(name = "conditiimunca")
	private String conditiimunca;
	@Column(name = "pensieprivata")
	private Boolean pensieprivata;
	@Column(name = "cotizatiepensieprivata")
	private Float cotizatiepensieprivata;
	@Column(name = "avans")
	private Float avans;
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
	private Float cotizatiesindicat;
	@Column(name = "spor")
	private String spor;
	@Column(name = "pensionar")
	private Boolean pensionar;

	@ManyToOne
	@JoinColumn(name = "idcentrucost")
	private CentruCost centrucost;

	@ManyToOne
	@JoinColumn(name = "idpunctlucru")
	private PunctDeLucru punctdelucru;

	@ManyToOne
	@JoinColumn(name = "idechipa")
	private Echipa echipa;

	@ManyToOne
	@JoinColumn(name = "iddepartament")
	private Departament departament;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idcontbancar", referencedColumnName = "id")
	private ContBancar contbancar;

	@JsonBackReference(value = "angajat-contract")
	@OneToOne(mappedBy = "contract", fetch = FetchType.LAZY)
	private Angajat angajat;

	@JsonBackReference(value = "retineri-contract")
	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RealizariRetineri> realizariRetineri;

	@JsonBackReference(value = "co-contract")
	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CO> concediiOdihna;

	@JsonBackReference(value = "cm-contract")
	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CM> concediiMedicale;

	@JsonBackReference(value = "suspendare-contract")
	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Suspendare> suspendari;

	public Contract() {
	}

	public Contract(String tip, String nr, String marca, LocalDate data, LocalDate dataincepere, PunctDeLucru punctdelucru, CentruCost centrucost, Echipa echipa, Departament departament, Boolean functiedebaza, Boolean calculdeduceri, Boolean studiisuperioare, Integer normalucru, Integer salariutarifar, String monedasalariu, String conditiimunca, Boolean pensieprivata, Float cotizatiepensieprivata, Float avans, String monedaavans, Integer zilecoan, LocalDate ultimazilucru, String casasanatate, String gradinvaliditate, String functie, String nivelstudii, String cor, Boolean sindicat, Float cotizatiesindicat, String spor, Boolean pensionar) {
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

	public Integer getSalariutarifar() {
		return salariutarifar;
	}

	public void setSalariutarifar(Integer salariutarifar) {
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

	public Float getCotizatiepensieprivata() {
		return cotizatiepensieprivata;
	}

	public void setCotizatiepensieprivata(Float cotizatiepensieprivata) {
		this.cotizatiepensieprivata = cotizatiepensieprivata;
	}

	public Float getAvans() {
		return avans;
	}

	public void setAvans(Float avans) {
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

	public Float getCotizatiesindicat() {
		return cotizatiesindicat;
	}

	public void setCotizatiesindicat(Float cotizatiesindicat) {
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

	public Angajat getAngajat() {
		return angajat;
	}

	public void setAngajat(Angajat angajat) {
		this.angajat = angajat;
	}

	public List<Suspendare> getSuspendari() {
		return suspendari;
	}

	public void setSuspendari(List<Suspendare> suspendari) {
		this.suspendari = suspendari;
	}

	public List<RealizariRetineri> getRealizariRetineri() {
		return realizariRetineri;
	}

	public void setRealizariRetineri(List<RealizariRetineri> realizariRetineri) {
		this.realizariRetineri = realizariRetineri;
	}

	// ! OTHER

	public void checkData() throws ResourceNotFoundException {
		if (angajat == null)
			return;
		String numeAngajat = angajat.getPersoana().getNumeIntreg();
		if (tip == null)
			throw new ResourceNotFoundException("Tipul contractului lui " + numeAngajat + " nu are valoare");
		if (nr == null)
			throw new ResourceNotFoundException("Numarul contractului " + numeAngajat + " nu are valoare");
		if (data == null)
			throw new ResourceNotFoundException("Data contractului " + numeAngajat + " nu are valoare");
		if (dataincepere == null)
			throw new ResourceNotFoundException("Data incepere activitate din contractul lui " + numeAngajat + " nu are valoare");
		if (calculdeduceri == null)
			throw new ResourceNotFoundException("Calcul deduceri pentru " + numeAngajat + " nu are valoare");
		if (normalucru == null)
			throw new ResourceNotFoundException("Norma lucru pentru " + numeAngajat + " nu are valoarea");
		if (salariutarifar == null)
			throw new ResourceNotFoundException("Salariul lui " + numeAngajat + " nu are valoare");
		if (zilecoan == null)
			throw new ResourceNotFoundException("Zilele concediu/an din contractul lui " + numeAngajat + " nu are valoare");
		if (functie == null)
			throw new ResourceNotFoundException("Functia lui " + numeAngajat + " nu are valoare");
	}

	public Contract fixDefaultValuesMissing() throws ResourceNotFoundException {
		if (tip == null)
			this.tip = "Contract de muncă";
		if (calculdeduceri == null)
			this.calculdeduceri = true;
		if (normalucru == null)
			this.normalucru = 8;
		if (zilecoan == null)
			this.zilecoan = 21;
		if (avans == null)
			this.avans = 0f;

		checkData();

		return this;
	}

	public RealizariRetineri getRealizariRetineri(int luna, int an) throws ResourceNotFoundException {
		for (RealizariRetineri rr : realizariRetineri) {
			if (rr.getLuna() == luna && rr.getAn() == an)
				return rr;
		}
		throw new ResourceNotFoundException(angajat.getPersoana().getNumeIntreg() + " nu are salariul calculat in luna " + luna + " " + an);
	}

	public int getZileAngajare(int luna, int an) {
		int daysInMonth = YearMonth.of(an, luna).lengthOfMonth() - getZileSuspendat(luna, an);

		if (luna == data.getMonthValue() && an == data.getYear()) {
			return daysInMonth - data.getDayOfMonth() + 1;
		} else if (ultimazilucru != null && luna == ultimazilucru.getMonthValue() && an == ultimazilucru.getYear()) {
			return daysInMonth - ultimazilucru.getDayOfMonth() + 1;
		} else
			return daysInMonth;
	}

	public int getZileSuspendat(int luna, int an) {

		if(suspendari.isEmpty()) return 0;

		int zileSuspendat = 0;
		
		int daysInMonth = YearMonth.of(an, luna).lengthOfMonth();

		LocalDate monthStart = LocalDate.of(an, luna, 1);
		LocalDate monthEnd = LocalDate.of(an, luna, daysInMonth);

		for (Suspendare suspendare : suspendari) {
			LocalDate suspendareStart = suspendare.getDela();
			LocalDate suspendareEnd = suspendare.getPanala();

			if (suspendareEnd != null) {
				if(monthStart.compareTo(suspendareStart) <= 0)
					return (int) ChronoUnit.DAYS.between(suspendareStart, monthEnd);
				else return 0;
			} else {
				// suspendarea s-a terminat deja
				if (monthStart.compareTo((suspendareEnd)) > 0)
					zileSuspendat += 0;

				// suspendarea cuprinde luna
				if (monthStart.compareTo(suspendareStart) >= 0 && monthEnd.compareTo(suspendareEnd) <= 0)
					zileSuspendat += daysInMonth;

				// suspendarea se termina dar nu incepe in luna
				else if (monthStart.compareTo(suspendareStart) >= 0 && monthEnd.compareTo(suspendareEnd) >= 0)
					zileSuspendat += (int) ChronoUnit.DAYS.between(monthStart, suspendareEnd);

				// suspendarea incepe dar nu se termina in luna
				else if (monthStart.compareTo(suspendareStart) <= 0 && monthEnd.compareTo(suspendareEnd) <= 0)
					zileSuspendat += (int) ChronoUnit.DAYS.between(suspendareStart, monthEnd);

				// luna cuprinde suspendarea
				else if (monthStart.compareTo(suspendareStart) <= 0 && monthEnd.compareTo(suspendareEnd) >= 0)
					zileSuspendat += (int) ChronoUnit.DAYS.between(suspendareStart, suspendareEnd);
			}
		}

		return zileSuspendat;
	}

	public LocalDate[] getPerioadaSuspendat(int luna, int an) {
		if(suspendari.isEmpty()) return new LocalDate[0];

		LocalDate[] perioadaSuspendare = new LocalDate[2];
		
		int daysInMonth = YearMonth.of(an, luna).lengthOfMonth();

		LocalDate monthStart = LocalDate.of(an, luna, 1);
		LocalDate monthEnd = LocalDate.of(an, luna, daysInMonth);

		for (Suspendare suspendare : suspendari) {
			LocalDate suspendareStart = suspendare.getDela();
			LocalDate suspendareEnd = suspendare.getPanala();

			if (suspendareEnd != null) {
				if(monthStart.compareTo(suspendareStart) <= 0) {
					perioadaSuspendare[0] = suspendareStart;
					perioadaSuspendare[1] = monthEnd;
					return perioadaSuspendare;
				}
				else return new LocalDate[0];
			} else {
				// suspendarea s-a terminat deja
				if (monthStart.compareTo((suspendareEnd)) > 0)
					return new LocalDate[0];

				// suspendarea cuprinde luna
				if (monthStart.compareTo(suspendareStart) >= 0 && monthEnd.compareTo(suspendareEnd) <= 0) {
					perioadaSuspendare[0] = monthStart;
					perioadaSuspendare[1] = monthEnd;
				}

				// suspendarea se termina dar nu incepe in luna
				else if (monthStart.compareTo(suspendareStart) >= 0 && monthEnd.compareTo(suspendareEnd) >= 0) {
					perioadaSuspendare[0] = monthStart;
					perioadaSuspendare[1] = suspendareEnd;
				}

				// suspendarea incepe dar nu se termina in luna
				else if (monthStart.compareTo(suspendareStart) <= 0 && monthEnd.compareTo(suspendareEnd) <= 0) {
					perioadaSuspendare[0] = suspendareStart;
					perioadaSuspendare[1] = monthEnd;
				}

				// luna cuprinde suspendarea
				else if (monthStart.compareTo(suspendareStart) <= 0 && monthEnd.compareTo(suspendareEnd) >= 0) {
					perioadaSuspendare[0] = suspendareStart;
					perioadaSuspendare[1] = suspendareEnd;
				}
			}
		}

		return perioadaSuspendare;
	}
}
