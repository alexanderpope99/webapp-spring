package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

import net.guides.springboot2.crud.model.Contract;

public class CMDTO {
	private int id;

	private LocalDate dela;

	private LocalDate panala;

	private Boolean continuare;

	private LocalDate datainceput;

	private String serie;

	private String nr;

	private String cnpcopil;

	private LocalDate dataeliberare;

	private String codurgenta;

	private Float procent;

	private String codboalainfcont;

	private Float bazacalcul;

	private Float bazacalculplafonata;

	private Integer zilebazacalcul;

	private Float mediezilnica;

	private Integer zilefirma;

	private Float indemnizatiefirma;

	private Integer zilefnuass;

	private Float indemnizatiefnuass;

	private Integer zilefaambp;

	private Float indemnizatiefaambp;

	private String locprescriere;

	private String nravizmedic;

	private String codboala;

	private Boolean urgenta;

	private String conditii;

	private int idcontract;

	private Contract contract;

	private String codindemnizatie;

	public Float getBazacalcul() {
		return bazacalcul;
	}

	public Float getBazacalculplafonata() {
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

	public LocalDate getDataeliberare() {
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

	public Integer getIdcontract() {
		if (contract == null)
			return idcontract;
		return contract.getId();
	}

	public Float getIndemnizatiefirma() {
		return indemnizatiefirma;
	}

	public Float getIndemnizatiefnuass() {
		return indemnizatiefnuass;
	}

	public String getLocprescriere() {
		return locprescriere;
	}

	public Float getMediezilnica() {
		return mediezilnica;
	}

	public String getNravizmedic() {
		return nravizmedic;
	}

	public LocalDate getPanala() {
		return panala;
	}

	public Float getProcent() {
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

	public String getCodindemnizatie() {
		return codindemnizatie;
	}

	public void setBazacalcul(Float bazacalcul) {
		this.bazacalcul = bazacalcul;
	}

	public void setBazacalculplafonata(Float bazacalculplafonata) {
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

	public void setDataeliberare(LocalDate dataeliberare) {
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
		this.contract = contract;
	}

	public void setIdcontract(int idcontract) {
		this.idcontract = idcontract;
	}
	
	public void setIdcontract(Contract contract) {
		this.idcontract = contract.getId();
	}

	public void setIndemnizatiefirma(Float indemnizatiefirma) {
		this.indemnizatiefirma = indemnizatiefirma;
	}

	public void setIndemnizatiefnuass(Float indemnizatiefnuass) {
		this.indemnizatiefnuass = indemnizatiefnuass;
	}

	public void setLocprescriere(String locprescriere) {
		this.locprescriere = locprescriere;
	}

	public void setMediezilnica(Float mediezilnica) {
		this.mediezilnica = mediezilnica;
	}

	public void setNravizmedic(String nravizmedic) {
		this.nravizmedic = nravizmedic;
	}

	public void setPanala(LocalDate panala) {
		this.panala = panala;
	}

	public void setProcent(Float procent) {
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

	public void setCodindemnizatie(String codindemnizatie) {
		this.codindemnizatie = codindemnizatie;
	}

	public Integer getZilefaambp() {
		return zilefaambp;
	}

	public void setZilefaambp(Integer zilefaambp) {
		this.zilefaambp = zilefaambp;
	}

	public Float getIndemnizatiefaambp() {
		return indemnizatiefaambp;
	}

	public void setIndemnizatiefaambp(Float indemnizatiefaambp) {
		this.indemnizatiefaambp = indemnizatiefaambp;
	}
}
