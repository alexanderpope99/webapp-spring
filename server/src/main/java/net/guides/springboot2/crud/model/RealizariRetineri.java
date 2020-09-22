package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "realizariretineri")
public class RealizariRetineri {

    private long id;
    @Column(name = "idcontract")
    private Long idcontract;
    @Column(name = "luna")
    private Integer luna;
    @Column(name = "an")
    private Integer an;
    @Column(name = "nrtichete")
    private Integer nrtichete = 0;
    @Column(name = "zileco")
    private Integer zileco = 0;
    @Column(name = "zilecolucratoare")
    private Integer zilecolucratoare;
    @Column(name = "zileconeplatit")
    private Integer zileconeplatit = 0;
    @Column(name = "zileconeplatitlucratoare")
    private Integer zileconeplatitlucratoare;
    @Column(name = "zilecm")
    private Integer zilecm = 0;
    @Column(name = "zilecmlucratoare")
    private Integer zilecmlucratoare = 0;
    @Column(name = "zilec")
    private Integer zilec  = 0;

    @Column(name = "norma")
    private Integer norma = 0; // nr zile lucratoare in luna
    @Column(name = "duratazilucru")
    private Integer duratazilucru = 0; // contract.normalucru
    @Column(name = "zilelucrate")
    private Integer zilelucrate = 0;
    @Column(name = "orelucrate")
    private Integer orelucrate = 0;

    @Column(name = "totaldrepturi")
    private Integer totaldrepturi = 0;

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
	private Integer primabruta;
	@Column(name = "totaloresuplimentare")
	private Integer totaloresuplimentare;
    
    public RealizariRetineri() { }

    public RealizariRetineri( long idcontract, Integer luna, Integer an, Integer nrtichete, Integer zileco, Integer zilecolucratoare, Integer zilecm, Integer zilecmlucratoare, Integer zileconeplatit,  Integer zileconeplatitlucratoare, Integer duratazilucru, Integer norma, Integer zilelucrate, Integer orelucrate, Integer totaldrepturi, Float salariupezi, Float salariupeora, Float cas, Float cass, Float cam, Float impozit, Float valoareTichete, Integer restplata, Integer nrpersoaneintretinere, Integer deducere, Integer primabruta, Integer totaloresuplimentare )
    {
        this.idcontract = idcontract;
        this.luna = luna;
        this.an = an;

        this.nrtichete = nrtichete;
        this.zileco = zileco;
        this.zilecolucratoare = zilecolucratoare;
        this.zileconeplatit = zileconeplatit;
        this.zileconeplatitlucratoare = zileconeplatitlucratoare;
        this.zilecm = zilecm;
        this.zilecmlucratoare = zilecmlucratoare;
        this.zilec  = zileco + zilecm;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    //! GETTERS
    public Integer getNrtichete() {
      return nrtichete;
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
    public Integer getZileconeplatit() {
      return zileconeplatit;
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
    public Float getValoaretichete() {
      return valoaretichete;
    }
    public Integer getTotaldrepturi() {
      return Math.round(totaldrepturi);
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
    public Long getIdcontract() {
      return idcontract;
    }
    public Integer getLuna() {
      return luna;
	}
	public Integer getPrimabruta() {
		return primabruta;
	}
	public Integer getTotaloresuplimentare() {
		return totaloresuplimentare;
	}
	public Integer getZilecmlucratoare() {
		return zilecmlucratoare;
	}
	public Integer getZilecolucratoare() {
		return zilecolucratoare;
	}
	public Integer getZileconeplatitlucratoare() {
		return zileconeplatitlucratoare;
	}

	//! SETTERS
	public void setNrtichete(Integer nrtichete) {
		this.nrtichete = nrtichete;
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
	public void setZileconeplatit(Integer zileconeplatit) {
		this.zileconeplatit = zileconeplatit;
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
	public void setValoaretichete(Float valoaretichete) {
		this.valoaretichete = valoaretichete;
	}
	public void setTotaldrepturi(Integer totaldrepturi) {
		this.totaldrepturi = totaldrepturi;
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
	public void setIdcontract(Long idcontract) {
		this.idcontract = idcontract;
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
	public void setZileconeplatitlucratoare(Integer zileconeplatitlucratoare) {
		this.zileconeplatitlucratoare = zileconeplatitlucratoare;
	}
}

