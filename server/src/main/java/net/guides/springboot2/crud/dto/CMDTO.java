package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

import net.guides.springboot2.crud.model.Contract;

public class CMDTO {
	private int id;

	private int idcontract;

	private LocalDate dela;

	private LocalDate panala;

	private Boolean continuare;

	private LocalDate datainceput;

	private String serie;

	private String nr;

	private String cnpcopil;

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

	private Contract contract;

	public Double getBazacalcul() {
		return bazacalcul;
	}

	public Double getBazacalculplafonata() {
		return bazacalculplafonata;
	}

	public String getCodboala() {
		return codboala;
	}

	public String getCodboalainfcont() {
		return codboalainfcont;
	}

	public String getCodurgenta() {
		return codurgenta;
	}

	public String getConditii() {
		return conditii;
	}

	public Boolean getContinuare() {
		return continuare;
	}

	public String getDataeliberare() {
		return dataeliberare;
	}

	public LocalDate getDatainceput() {
		return datainceput;
	}

	public LocalDate getDela() {
		return dela;
	}

	public int getId() {
		return id;
	}

	public Integer getIdontract() {
		if (contract == null)
			return idcontract;
		return contract.getId();
	}

	public Double getIndemnizatiefirma() {
		return indemnizatiefirma;
	}

	public Double getIndemnizatiefnuass() {
		return indemnizatiefnuass;
	}

	public String getLocprescriere() {
		return locprescriere;
	}

	public Double getMediezilnica() {
		return mediezilnica;
	}

	public String getNravizmedic() {
		return nravizmedic;
	}

	public LocalDate getPanala() {
		return panala;
	}

	public Double getProcent() {
		return procent;
	}

	public Boolean getUrgenta() {
		return urgenta;
	}

	public Integer getZilebazacalcul() {
		return zilebazacalcul;
	}

	public Integer getZilefirma() {
		return zilefirma;
	}

	public Integer getZilefnuass() {
		return zilefnuass;
	}

	public void setBazacalcul(Double bazacalcul) {
		this.bazacalcul = bazacalcul;
	}

	public void setBazacalculplafonata(Double bazacalculplafonata) {
		this.bazacalculplafonata = bazacalculplafonata;
	}

	public void setCodboala(String codboala) {
		this.codboala = codboala;
	}

	public void setCodboalainfcont(String codboalainfcont) {
		this.codboalainfcont = codboalainfcont;
	}

	public void setCodurgenta(String codurgenta) {
		this.codurgenta = codurgenta;
	}

	public void setConditii(String conditii) {
		this.conditii = conditii;
	}

	public void setContinuare(Boolean continuare) {
		this.continuare = continuare;
	}

	public void setDataeliberare(String dataeliberare) {
		this.dataeliberare = dataeliberare;
	}

	public void setDatainceput(LocalDate datainceput) {
		this.datainceput = datainceput;
	}

	public void setDela(LocalDate dela) {
		this.dela = dela;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setContract(Contract contract) {
		this.idcontract = contract.getId();
	}

	public void setIdcontract(Contract contract) {
		this.idcontract = contract.getId();
	}

	public void setIdcontract(int idcontract) {
		this.idcontract = idcontract;
	}

	public void setIndemnizatiefirma(Double indemnizatiefirma) {
		this.indemnizatiefirma = indemnizatiefirma;
	}

	public void setIndemnizatiefnuass(Double indemnizatiefnuass) {
		this.indemnizatiefnuass = indemnizatiefnuass;
	}

	public void setLocprescriere(String locprescriere) {
		this.locprescriere = locprescriere;
	}

	public void setMediezilnica(Double mediezilnica) {
		this.mediezilnica = mediezilnica;
	}

	public void setNravizmedic(String nravizmedic) {
		this.nravizmedic = nravizmedic;
	}

	public void setPanala(LocalDate panala) {
		this.panala = panala;
	}

	public void setProcent(Double procent) {
		this.procent = procent;
	}

	public void setUrgenta(Boolean urgenta) {
		this.urgenta = urgenta;
	}

	public void setZilebazacalcul(Integer zilebazacalcul) {
		this.zilebazacalcul = zilebazacalcul;
	}

	public void setZilefirma(Integer zilefirma) {
		this.zilefirma = zilefirma;
	}

	public void setZilefnuass(Integer zilefnuass) {
		this.zilefnuass = zilefnuass;
	}

	public String getCnpcopil() {
		return cnpcopil;
	}

	public void setCnpcopil(String cnpcopil) {
		this.cnpcopil = cnpcopil;
	}

	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}
}
