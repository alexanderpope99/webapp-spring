package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.Contract;

public class RealizariRetineriDTO {
	private int id;

	private Contract contract;

	private Integer luna;

	private Integer an;

	private Integer nrtichete = 0;

	private Integer zileco = 0;

	private Integer zilecolucratoare;

	private Integer zileconeplatit = 0;

	private Integer zileconeplatitlucratoare;

	private Integer zilecm = 0;

	private Integer zilecmlucratoare = 0;

	private Integer zilec = 0;

	private Integer zileplatite = 0;

	private Integer impozitscutit;

	private Integer valcm;

	private Integer norma = 0; // nr zile lucratoare in luna

	private Integer duratazilucru = 0; // contract.normalucru

	private Integer zilelucrate = 0;

	private Integer orelucrate = 0;

	private Integer totaldrepturi = 0;

	private Integer salariurealizat = 0;

	private Integer venitnet = 0;

	private Integer bazaimpozit;

	private Float salariupezi = 0f;

	private Float salariupeora = 0f;

	private Float cas = 0f;

	private Float cass = 0f;

	private Float cam = 0f;

	private Float impozit = 0f;

	private Float valoaretichete = 0f;

	private Integer restplata = 0;

	private Integer nrpersoaneintretinere = 0;

	private Integer deducere = 0;

	private Integer primabruta;

	private Float totaloresuplimentare;

	private Integer nroresuplimentare;

	public Integer getAn() {
		return an;
	}

	public Integer getBazaimpozit() {
		return bazaimpozit;
	}

	public Float getCam() {
		return cam;
	}

	public Float getCas() {
		return cas;
	}

	public Float getCass() {
		return cass;
	}

	public Integer getDeducere() {
		return deducere;
	}

	public Integer getDuratazilucru() {
		return duratazilucru;
	}

	public int getId() {
		return id;
	}

	public Integer getIdcontract() {
		if (contract == null)
			return null;
		else
			return contract.getId();
	}

	public Float getImpozit() {
		return impozit;
	}

	public Integer getImpozitscutit() {
		return impozitscutit;
	}

	public Integer getLuna() {
		return luna;
	}

	public Integer getNorma() {
		return norma;
	}

	public Integer getNroresuplimentare() {
		return nroresuplimentare;
	}

	public Integer getNrpersoaneintretinere() {
		return nrpersoaneintretinere;
	}

	public Integer getNrtichete() {
		return nrtichete;
	}

	public Integer getOrelucrate() {
		return orelucrate;
	}

	public Integer getPrimabruta() {
		return primabruta;
	}

	public Integer getRestplata() {
		return restplata;
	}

	public Float getSalariupeora() {
		return salariupeora;
	}

	public Float getSalariupezi() {
		return salariupezi;
	}

	public Integer getSalariurealizat() {
		return salariurealizat;
	}

	public Integer getTotaldrepturi() {
		return totaldrepturi;
	}

	public Float getTotaloresuplimentare() {
		return totaloresuplimentare;
	}

	public Integer getValcm() {
		return valcm;
	}

	public Float getValoaretichete() {
		return valoaretichete;
	}

	public Integer getVenitnet() {
		return venitnet;
	}

	public Integer getZilec() {
		return zilec;
	}

	public Integer getZilecm() {
		return zilecm;
	}

	public Integer getZilecmlucratoare() {
		return zilecmlucratoare;
	}

	public Integer getZileco() {
		return zileco;
	}

	public Integer getZilecolucratoare() {
		return zilecolucratoare;
	}

	public Integer getZileconeplatit() {
		return zileconeplatit;
	}

	public Integer getZileconeplatitlucratoare() {
		return zileconeplatitlucratoare;
	}

	public Integer getZilelucrate() {
		return zilelucrate;
	}

	public Integer getZileplatite() {
		return zileplatite;
	}

	public void setAn(Integer an) {
		this.an = an;
	}

	public void setBazaimpozit(Integer bazaimpozit) {
		this.bazaimpozit = bazaimpozit;
	}

	public void setCam(Float cam) {
		this.cam = cam;
	}

	public void setCas(Float cas) {
		this.cas = cas;
	}

	public void setCass(Float cass) {
		this.cass = cass;
	}

	public void setDeducere(Integer deducere) {
		this.deducere = deducere;
	}

	public void setDuratazilucru(Integer duratazilucru) {
		this.duratazilucru = duratazilucru;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setContract(Contract idcontract) {
		this.contract = idcontract;
	}

	public void setImpozit(Float impozit) {
		this.impozit = impozit;
	}

	public void setImpozitscutit(Integer impozitscutit) {
		this.impozitscutit = impozitscutit;
	}

	public void setLuna(Integer luna) {
		this.luna = luna;
	}

	public void setNorma(Integer norma) {
		this.norma = norma;
	}

	public void setNroresuplimentare(Integer nroresuplimentare) {
		this.nroresuplimentare = nroresuplimentare;
	}

	public void setNrpersoaneintretinere(Integer nrpersoaneintretinere) {
		this.nrpersoaneintretinere = nrpersoaneintretinere;
	}

	public void setNrtichete(Integer nrtichete) {
		this.nrtichete = nrtichete;
	}

	public void setOrelucrate(Integer orelucrate) {
		this.orelucrate = orelucrate;
	}

	public void setPrimabruta(Integer primabruta) {
		this.primabruta = primabruta;
	}

	public void setRestplata(Integer restplata) {
		this.restplata = restplata;
	}

	public void setSalariupeora(Float salariupeora) {
		this.salariupeora = salariupeora;
	}

	public void setSalariupezi(Float salariupezi) {
		this.salariupezi = salariupezi;
	}

	public void setSalariurealizat(Integer salariurealizat) {
		this.salariurealizat = salariurealizat;
	}

	public void setTotaldrepturi(Integer totaldrepturi) {
		this.totaldrepturi = totaldrepturi;
	}

	public void setTotaloresuplimentare(Float totaloresuplimentare) {
		this.totaloresuplimentare = totaloresuplimentare;
	}

	public void setValcm(Integer valcm) {
		this.valcm = valcm;
	}

	public void setValoaretichete(Float valoaretichete) {
		this.valoaretichete = valoaretichete;
	}

	public void setVenitnet(Integer venitnet) {
		this.venitnet = venitnet;
	}

	public void setZilec(Integer zilec) {
		this.zilec = zilec;
	}

	public void setZilecm(Integer zilecm) {
		this.zilecm = zilecm;
	}

	public void setZilecmlucratoare(Integer zilecmlucratoare) {
		this.zilecmlucratoare = zilecmlucratoare;
	}

	public void setZileco(Integer zileco) {
		this.zileco = zileco;
	}

	public void setZilecolucratoare(Integer zilecolucratoare) {
		this.zilecolucratoare = zilecolucratoare;
	}

	public void setZileconeplatit(Integer zileconeplatit) {
		this.zileconeplatit = zileconeplatit;
	}

	public void setZileconeplatitlucratoare(Integer zileconeplatitlucratoare) {
		this.zileconeplatitlucratoare = zileconeplatitlucratoare;
	}

	public void setZilelucrate(Integer zilelucrate) {
		this.zilelucrate = zilelucrate;
	}

	public void setZileplatite(Integer zileplatite) {
		this.zileplatite = zileplatite;
	}
}
