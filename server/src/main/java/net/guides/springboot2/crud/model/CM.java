package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "cm")
public class CM {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "dela")
	private LocalDate dela;

	@Column(name = "panala")
	private LocalDate panala;

	@Column(name = "continuare")
	private Boolean continuare;

	@Column(name = "datainceput")
	private LocalDate datainceput;

	@Column(name = "serienrcertificat")
	private String serienrcertificat;

	@Column(name = "dataeliberare")
	private String dataeliberare;

	@Column(name = "codurgenta")
	private String codurgenta;

	@Column(name = "procent")
	private Double procent;

	@Column(name = "codboalainfcont")
	private String codboalainfcont;

	@Column(name = "bazacalcul")
	private Double bazacalcul;

	@Column(name = "bazacalculplafonata")
	private Double bazacalculplafonata;

	@Column(name = "zilebazacalcul")
	private Integer zilebazacalcul;

	@Column(name = "mediezilnica")
	private Double mediezilnica;

	@Column(name = "zilefirma")
	private Integer zilefirma;

	@Column(name = "indemnizatiefirma")
	private Double indemnizatiefirma;

	@Column(name = "zilefnuass")
	private Integer zilefnuass;

	@Column(name = "indemnizatiefnuass")
	private Double indemnizatiefnuass;

	@Column(name = "locprescriere")
	private String locprescriere;

	@Column(name = "nravizmedic")
	private String nravizmedic;

	@Column(name = "codboala")
	private String codboala;

	@Column(name = "urgenta")
	private Boolean urgenta;

	@Column(name = "conditii")
	private String conditii;

	@ManyToOne
	@JoinColumn(name = "idcontract")
	private Contract idcontract;

	public CM() {
	}

	public CM(LocalDate dela, LocalDate panala, Boolean continuare, LocalDate datainceput, String serienrcertificat,
			String dataeliberare, String codurgenta, Double procent, String codboalainfcont, Double bazacalcul,
			Double bazacalculplafonata, Integer zilebazacalcul, Double mediezilnica, Integer zilefirma,
			Double indemnizatiefirma, Integer zilefnuass, Double indemnizatiefnuass, String locprescriere,
			String nravizmedic, String codboala, Boolean urgenta, String conditii, Contract idcontract) {
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getSerienrcertificat() {
		return serienrcertificat;
	}

	public void setSerienrcertificat(String serienrcertificat) {
		this.serienrcertificat = serienrcertificat;
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

	public Double getProcent() {
		return procent;
	}

	public void setProcent(Double procent) {
		this.procent = procent;
	}

	public String getCodboalainfcont() {
		return codboalainfcont;
	}

	public void setCodboalainfcont(String codboalainfcont) {
		this.codboalainfcont = codboalainfcont;
	}

	public Double getBazacalcul() {
		return bazacalcul;
	}

	public void setBazacalcul(Double bazacalcul) {
		this.bazacalcul = bazacalcul;
	}

	public Double getBazacalculplafonata() {
		return bazacalculplafonata;
	}

	public void setBazacalculplafonata(Double bazacalculplafonata) {
		this.bazacalculplafonata = bazacalculplafonata;
	}

	public Integer getZilebazacalcul() {
		return zilebazacalcul;
	}

	public void setZilebazacalcul(Integer zilebazacalcul) {
		this.zilebazacalcul = zilebazacalcul;
	}

	public Double getMediezilnica() {
		return mediezilnica;
	}

	public void setMediezilnica(Double mediezilnica) {
		this.mediezilnica = mediezilnica;
	}

	public Integer getZilefirma() {
		return zilefirma;
	}

	public void setZilefirma(Integer zilefirma) {
		this.zilefirma = zilefirma;
	}

	public Double getIndemnizatiefirma() {
		return indemnizatiefirma;
	}

	public void setIndemnizatiefirma(Double indemnizatiefirma) {
		this.indemnizatiefirma = indemnizatiefirma;
	}

	public Integer getZilefnuass() {
		return zilefnuass;
	}

	public void setZilefnuass(Integer zilefnuass) {
		this.zilefnuass = zilefnuass;
	}

	public Double getIndemnizatiefnuass() {
		return indemnizatiefnuass;
	}

	public void setIndemnizatiefnuass(Double indemnizatiefnuass) {
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

	public Contract getIdcontract() {
		return idcontract;
	}

	public void setIdcontract(Contract idcontract) {
		this.idcontract = idcontract;
	}
}
