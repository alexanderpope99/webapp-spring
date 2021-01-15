package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;

@Entity
@Table(name = "realizariretineri", uniqueConstraints = {@UniqueConstraint(columnNames = {"luna", "an", "idcontract"})})
public class RealizariRetineri implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "luna")
	private Integer luna;
	@Column(name = "an")
	private Integer an;
	@Column(name = "nrtichete")
	private Integer nrtichete = 0;
	@Column(name = "zileco")
	private Integer zileco = 0;
	@Column(name = "zilecolucratoare")
	private Integer zilecolucratoare = 0;
	@Column(name = "zilecfp")
	private Integer zilecfp = 0;
	@Column(name = "zilecfplucratoare")
	private Integer zilecfplucratoare = 0;
	@Column(name = "zilecm")
	private Integer zilecm = 0;
	@Column(name = "zilecmlucratoare")
	private Integer zilecmlucratoare = 0;
	@Column(name = "zilec")
	private Integer zilec = 0;
	@Column(name = "zileplatite")
	private Integer zileplatite = 0;

	@Column(name = "impozitscutit")
	private Integer impozitscutit = 0;

	@Column(name = "valcm")
	private Integer valcm = 0;
	@Column(name = "valco")
	private Integer valco = 0;

	@Column(name = "norma")
	private Integer norma = 0; // nr zile lucratoare in luna
	@Column(name = "duratazilucru")
	private Integer duratazilucru = 0; // contract.normalucru
	@Column(name = "zilecontract")
	private Integer zilecontract = 0;
	@Column(name = "zilelucrate")
	private Integer zilelucrate = 0;
	@Column(name = "orelucrate")
	private Integer orelucrate = 0;

	@Column(name = "totaldrepturi")
	private Integer totaldrepturi = 0;

	@Column(name = "salariurealizat")
	private Integer salariurealizat = 0;

	@Column(name = "venitnet")
	private Integer venitnet = 0;

	@Column(name = "bazaimpozit")
	private Integer bazaimpozit = 0;

	@Column(name = "salariupezi")
	private Float salariupezi = 0f;
	@Column(name = "salariupeora")
	private Float salariupeora = 0f;

	@Column(name = "cas")
	private Float cas = 0f;
	@Column(name = "cass")
	private Float cass = 0f;
	@Column(name = "cam")
	private Float cam = 0f;
	@Column(name = "impozit")
	private Float impozit = 0f;
	@Column(name = "valoaretichete")
	private Float valoaretichete = 0f;

	@Column(name = "restplata")
	private Integer restplata = 0;

	@Column(name = "nrpersoaneintretinere")
	private Integer nrpersoaneintretinere = 0;
	@Column(name = "deducere")
	private Integer deducere = 0;

	@Column(name = "primabruta")
	private Integer primabruta = 0;

	@Column(name = "totaloresuplimentare")
	private Integer totaloresuplimentare = 0;

	@Column(name = "nroresuplimentare")
	private Integer nroresuplimentare = 0;

	@JsonBackReference(value = "stat-contract")
	@ManyToOne
	@JoinColumn(name = "idcontract", referencedColumnName = "id")
	private Contract contract;

	@JsonBackReference(value = "oresuplimentare-realizariretineri")
	@OneToMany(mappedBy = "statsalariat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Oresuplimentare> oresuplimentare;

	@OneToOne(mappedBy = "stat", fetch = FetchType.LAZY)
	private Retineri retineri;

	public RealizariRetineri() {
	}

	public RealizariRetineri(int luna, int an, Contract contract) {
		this.luna = luna;
		this.an = an;
		this.contract = contract;
	}

	public RealizariRetineri(Contract contract, Integer luna, Integer an, Integer nrtichete, Integer zileco,
			Integer zilecolucratoare, Integer zilecm, Integer zilecmlucratoare, Integer zilecfp, Integer zilecfplucratoare,
			Integer duratazilucru, Integer norma, Integer zilelucrate, Integer orelucrate, Integer totaldrepturi,
			Float salariupezi, Float salariupeora, Float cas, Float cass, Float cam, Float impozit, Float valoareTichete,
			Integer restplata, Integer nrpersoaneintretinere, Integer deducere, Integer primabruta,
			Integer totaloresuplimentare) {
		this.contract = contract;
		this.luna = luna;
		this.an = an;

		this.nrtichete = nrtichete;
		this.zileco = zileco;
		this.zilecolucratoare = zilecolucratoare;
		this.zilecfp = zilecfp;
		this.zilecfplucratoare = zilecfplucratoare;
		this.zilecm = zilecm;
		this.zilecmlucratoare = zilecmlucratoare;
		this.zilec = zileco + zilecm;

		this.norma = norma;
		this.duratazilucru = duratazilucru;
		this.zilelucrate = zilelucrate;
		this.orelucrate = orelucrate;

		this.salariupezi = salariupezi;
		this.salariupeora = salariupeora;

		this.totaldrepturi = totaldrepturi;

		this.cas = cas;
		this.cass = cass;
		this.cam = cam;
		this.impozit = impozit;
		this.valoaretichete = valoareTichete;

		this.restplata = restplata;

		this.nrpersoaneintretinere = nrpersoaneintretinere;
		this.deducere = deducere;
		this.primabruta = primabruta;

		this.totaloresuplimentare = totaloresuplimentare;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// ! GETTERS
	public Integer getNrtichete() {
		return nrtichete;
	}

	public Integer getImpozitscutit() {
		return impozitscutit;
	}

	public Integer getZilecm() {
		return zilecm;
	}

	public Integer getZileco() {
		return zileco;
	}

	public Integer getZilec() {
		return zilec;
	}

	public Integer getZilecfp() {
		return zilecfp;
	}

	public Integer getZileplatite() {
		return zileplatite;
	}

	public Integer getDuratazilucru() {
		return duratazilucru;
	}

	public Integer getNorma() {
		return norma;
	}

	public Integer getOrelucrate() {
		return orelucrate;
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

	public Float getImpozit() {
		return impozit;
	}

	public Integer getBazaimpozit() {
		return bazaimpozit == null ? 0 : bazaimpozit;
	}

	public Float getValoaretichete() {
		return valoaretichete;
	}

	public Integer getTotaldrepturi() {
		return Math.round(totaldrepturi);
	}

	public Integer getSalariurealizat() {
		return salariurealizat;
	}

	public Integer getNrpersoaneintretinere() {
		return nrpersoaneintretinere;
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

	public Integer getZilelucrate() {
		return zilelucrate;
	}

	public Integer getDeducere() {
		return deducere;
	}

	public Integer getAn() {
		return an;
	}

	public Contract getContract() {
		return contract;
	}

	public Integer getLuna() {
		return luna;
	}

	public Integer getPrimabruta() {
		return primabruta;
	}

	public Integer getTotaloresuplimentare() {
		return totaloresuplimentare == null ? 0 : totaloresuplimentare;
	}

	public Integer getZilecmlucratoare() {
		return zilecmlucratoare;
	}

	public Integer getZilecolucratoare() {
		return zilecolucratoare;
	}

	public Integer getZilecfplucratoare() {
		return zilecfplucratoare;
	}

	public Integer getNroresuplimentare() {
		return nroresuplimentare;
	}

	public Integer getValcm() {
		return valcm;
	}

	public Integer getVenitnet() {
		return venitnet;
	}

	public Integer getValco() {
		return valco;
	}

	public Integer getZilecontract() {
		return zilecontract;
	}

	public List<Oresuplimentare> getOresuplimentare() {
		return oresuplimentare;
	}
	public Retineri getRetineri() {
		return retineri;
	}

	// ! SETTERS
	public void setNrtichete(Integer nrtichete) {
		this.nrtichete = nrtichete;
	}

	public void setImpozitscutit(Integer impozitscutit) {
		this.impozitscutit = impozitscutit;
	}

	public void setZilecm(Integer zilecm) {
		this.zilecm = zilecm;
	}

	public void setZileco(Integer zileco) {
		this.zileco = zileco;
	}

	public void setZilec(Integer zilec) {
		this.zilec = zilec;
	}

	public void setZileplatite(Integer zileplatite) {
		this.zileplatite = zileplatite;
	}

	public void setZilecfp(Integer zilecfp) {
		this.zilecfp = zilecfp;
	}

	public void setDuratazilucru(Integer duratazilucru) {
		this.duratazilucru = duratazilucru;
	}

	public void setNorma(Integer norma) {
		this.norma = norma;
	}

	public void setOrelucrate(Integer orelucrate) {
		this.orelucrate = orelucrate;
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

	public void setImpozit(Float impozit) {
		this.impozit = impozit;
	}

	public void setBazaimpozit(Integer bazaimpozit) {
		this.bazaimpozit = bazaimpozit;
	}

	public void setValoaretichete(Float valoaretichete) {
		this.valoaretichete = valoaretichete;
	}

	public void setTotaldrepturi(Integer totaldrepturi) {
		this.totaldrepturi = totaldrepturi;
	}

	public void setSalariurealizat(Integer salariurealizat) {
		this.salariurealizat = salariurealizat;
	}

	public void setNrpersoaneintretinere(Integer nrpersoaneintretinere) {
		this.nrpersoaneintretinere = nrpersoaneintretinere;
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

	public void setZilelucrate(Integer zilelucrate) {
		this.zilelucrate = zilelucrate;
	}

	public void setDeducere(Integer deducere) {
		this.deducere = deducere;
	}

	public void setAn(Integer an) {
		this.an = an;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public void setLuna(Integer luna) {
		this.luna = luna;
	}

	public void setPrimabruta(Integer primabruta) {
		this.primabruta = primabruta;
	}

	public void setTotaloresuplimentare(Integer totaloresuplimentare) {
		this.totaloresuplimentare = totaloresuplimentare;
	}

	public void setZilecmlucratoare(Integer zilecmlucratoare) {
		this.zilecmlucratoare = zilecmlucratoare;
	}

	public void setZilecolucratoare(Integer zilecolucratoare) {
		this.zilecolucratoare = zilecolucratoare;
	}

	public void setZilecfplucratoare(Integer zilecfplucratoare) {
		this.zilecfplucratoare = zilecfplucratoare;
	}

	public void setNroresuplimentare(Integer nroresuplimentare) {
		this.nroresuplimentare = nroresuplimentare;
	}

	public void setValcm(Integer valcm) {
		this.valcm = valcm;
	}

	public void setVenitnet(Integer venitnet) {
		this.venitnet = venitnet;
	}

	public void setValco(Integer valco) {
		this.valco = valco;
	}

	public void setZilecontract(Integer zilecontract) {
		this.zilecontract = zilecontract;
	}
	public void setOresuplimentare(List<Oresuplimentare> oresuplimentare) {
		this.oresuplimentare = oresuplimentare;
	}
	public void setRetineri(Retineri retineri) {
		this.retineri = retineri;
	}

	public void checkData() throws ResourceNotFoundException {
		if(primabruta == null)
			throw new ResourceNotFoundException("Prima bruta nu are valoare");
		if(totaloresuplimentare == null)
			throw new ResourceNotFoundException("Total ore suplimentare nu are valoare");
		if(nroresuplimentare == null)
			throw new ResourceNotFoundException("Numarul de ore suplimentare nu are valoare");
	}
}
