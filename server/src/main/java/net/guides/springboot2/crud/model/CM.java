package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "cm")
public class CM implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "dela")
	private LocalDate dela;

	@Column(name = "panala")
	private LocalDate panala;

	@Column(name = "continuare")
	private Boolean continuare;

	@Column(name = "datainceput")
	private LocalDate datainceput;

	@Column(name = "serie")
	private String serie;

	@Column(name = "nr")
	private String nr;

	@Column(name = "cnpcopil")
	private String cnpcopil;

	@Column(name = "dataeliberare")
	private String dataeliberare;

	@Column(name = "codurgenta")
	private String codurgenta;

	@Column(name = "procent")
	private Float procent;

	@Column(name = "codboalainfcont")
	private String codboalainfcont;

	@Column(name = "bazacalcul")
	private Float bazacalcul;

	@Column(name = "bazacalculplafonata")
	private Float bazacalculplafonata;

	@Column(name = "zilebazacalcul")
	private Integer zilebazacalcul;

	@Column(name = "mediezilnica")
	private Float mediezilnica;

	@Column(name = "zilefirma")
	private Integer zilefirma;

	@Column(name = "indemnizatiefirma")
	private Float indemnizatiefirma;

	@Column(name = "zilefnuass")
	private Integer zilefnuass;

	@Column(name = "indemnizatiefnuass")
	private Float indemnizatiefnuass;

	@Column(name = "locprescriere")
	private String locprescriere;

	@Column(name = "nravizmedic")
	private String nravizmedic;

	@Column(name = "codindemnizatie")
	private String codindemnizatie;

	@Column(name = "codboala")
	private String codboala;

	@Column(name = "urgenta")
	private Boolean urgenta;

	@Column(name = "conditii")
	private String conditii;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract")
	private Contract contract;

	public CM() {
	}

	public CM(LocalDate dela, LocalDate panala, Boolean continuare, LocalDate datainceput, String serie, String nr,
			String dataeliberare, String codurgenta, Float procent, String codboalainfcont, Float bazacalcul,
			Float bazacalculplafonata, Integer zilebazacalcul, Float mediezilnica, Integer zilefirma,
			Float indemnizatiefirma, Integer zilefnuass, Float indemnizatiefnuass, String locprescriere,
			String nravizmedic, String codboala, Boolean urgenta, String conditii, Contract contract, String cnpcopil,
			String codindemnizatie) {
		this.dela = dela;
		this.panala = panala;
		this.continuare = continuare;
		this.datainceput = datainceput;
		this.serie = serie;
		this.nr = nr;
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
		this.contract = contract;
		this.cnpcopil = cnpcopil;
		this.codindemnizatie = codindemnizatie;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDela() {
		return dela;
	}

	public void setDela(LocalDate dela) {
		this.dela = dela;
	}

	public LocalDate getPanala() {
		return panala;
	}

	public void setPanala(LocalDate panala) {
		this.panala = panala;
	}

	public Boolean isContinuare() {
		return continuare;
	}

	public void setContinuare(Boolean continuare) {
		this.continuare = continuare;
	}

	public LocalDate getDatainceput() {
		return datainceput;
	}

	public void setDatainceput(LocalDate datainceput) {
		this.datainceput = datainceput;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	public String getCnpcopil() {
		return cnpcopil;
	}

	public void setCnpcopil(String cnpcopil) {
		this.cnpcopil = cnpcopil;
	}

	public String getDataeliberare() {
		return dataeliberare;
	}

	public void setDataeliberare(String dataeliberare) {
		this.dataeliberare = dataeliberare;
	}

	public String getCodurgenta() {
		return codurgenta;
	}

	public void setCodurgenta(String codurgenta) {
		this.codurgenta = codurgenta;
	}

	public Float getProcent() {
		return procent;
	}

	public void setProcent(Float procent) {
		this.procent = procent;
	}

	public String getCodboalainfcont() {
		return codboalainfcont;
	}

	public void setCodboalainfcont(String codboalainfcont) {
		this.codboalainfcont = codboalainfcont;
	}

	public Float getBazacalcul() {
		return bazacalcul;
	}

	public void setBazacalcul(Float bazacalcul) {
		this.bazacalcul = bazacalcul;
	}

	public Float getBazacalculplafonata() {
		return bazacalculplafonata;
	}

	public void setBazacalculplafonata(Float bazacalculplafonata) {
		this.bazacalculplafonata = bazacalculplafonata;
	}

	public Integer getZilebazacalcul() {
		return zilebazacalcul;
	}

	public void setZilebazacalcul(Integer zilebazacalcul) {
		this.zilebazacalcul = zilebazacalcul;
	}

	public Float getMediezilnica() {
		return mediezilnica;
	}

	public void setMediezilnica(Float mediezilnica) {
		this.mediezilnica = mediezilnica;
	}

	public Integer getZilefirma() {
		return zilefirma;
	}

	public void setZilefirma(Integer zilefirma) {
		this.zilefirma = zilefirma;
	}

	public Float getIndemnizatiefirma() {
		return indemnizatiefirma;
	}

	public void setIndemnizatiefirma(Float indemnizatiefirma) {
		this.indemnizatiefirma = indemnizatiefirma;
	}

	public Integer getZilefnuass() {
		return zilefnuass;
	}

	public void setZilefnuass(Integer zilefnuass) {
		this.zilefnuass = zilefnuass;
	}

	public Float getIndemnizatiefnuass() {
		return indemnizatiefnuass;
	}

	public void setIndemnizatiefnuass(Float indemnizatiefnuass) {
		this.indemnizatiefnuass = indemnizatiefnuass;
	}

	public String getLocprescriere() {
		return locprescriere;
	}

	public void setLocprescriere(String locprescriere) {
		this.locprescriere = locprescriere;
	}

	public String getNravizmedic() {
		return nravizmedic;
	}

	public void setNravizmedic(String nravizmedic) {
		this.nravizmedic = nravizmedic;
	}

	public String getCodboala() {
		return codboala;
	}

	public void setCodboala(String codboala) {
		this.codboala = codboala;
	}

	public Boolean isUrgenta() {
		return urgenta;
	}

	public void setUrgenta(Boolean urgenta) {
		this.urgenta = urgenta;
	}

	public String getConditii() {
		return conditii;
	}

	public void setConditii(String conditii) {
		this.conditii = conditii;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public String getCodindemnizatie() {
		return codindemnizatie;
	}

	public void setCodindemnizatie(String codindemnizatie) {
		this.codindemnizatie = codindemnizatie;
	}
}
