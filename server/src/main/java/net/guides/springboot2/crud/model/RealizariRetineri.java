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
    private int luna;
    @Column(name = "an")
    private int an;
    @Column(name = "nrtichete")
    private int nrtichete = 0;
    @Column(name = "zileco")
    private int zileco = 0;
    @Column(name = "zileconeplatit")
    private int zileconeplatit = 0;
    @Column(name = "zilecm")
    private int zilecm = 0;
    @Column(name = "zilec")
    private int zilec  = 0;

    @Column(name = "norma")
    private int norma = 0; // nr zile lucratoare in luna
    @Column(name = "duratazilucru")
    private int duratazilucru = 0; // contract.normalucru
    @Column(name = "zilelucrate")
    private int zilelucrate = 0;
    @Column(name = "orelucrate")
    private int orelucrate = 0;

    @Column(name = "totaldrepturi")
    private float totaldrepturi = 0;

    @Column(name = "salariupezi")
    private float salariupezi = 0;
    @Column(name = "salariupeora")
    private float salariupeora = 0;

    @Column(name = "cas")
    private float cas = 0;
    @Column(name = "cass")
    private float cass = 0;
    @Column(name = "cam")
    private float cam = 0;
    @Column(name = "impozit")
    private float impozit = 0;
    @Column(name = "valoaretichete")
    private float valoaretichete = 0;

    @Column(name = "restplata")
    private int restplata = 0;

    @Column(name = "nrpersoaneintretinere")
    private int nrpersoaneintretinere = 0;
    @Column(name = "deducere")
    private int deducere = 0;
    
    public RealizariRetineri() { }

    public RealizariRetineri( long idcontract, int luna, int an, int nrtichete, int zileco, int zilecm, int zileconeplatit, int duratazilucru, int norma, int zilelucrate, int orelucrate, float totaldrepturi, float salariupezi, float salariupeora, float cas, float cass, float cam, float impozit, float valoareTichete, int restplata, int nrpersoaneintretinere, int deducere )
    {
		this.idcontract = idcontract;
		this.luna = luna;
		this.an = an;

        this.nrtichete = nrtichete;
        this.zileco = zileco;
        this.zileconeplatit = zileconeplatit;
        this.zilecm = zilecm;
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
    public int getNrtichete() {
      return nrtichete;
    }
    public int getZilecm() {
      return zilecm;
    }
    public int getZileco() {
      return zileco;
    }
    public int getZilec() {
      return zilec;
    }
    public int getZileconeplatit() {
      return zileconeplatit;
    }
    public int getDuratazilucru() {
      return duratazilucru;
    }
    public int getNorma() {
      return norma;
    }
    public int getOrelucrate() {
      return orelucrate;
    }
    public float getCam() {
      return cam;
    }
    public float getCas() {
      return cas;
    }
    public float getCass() {
      return cass;
    }
    public float getImpozit() {
      return impozit;
    }
    public float getValoaretichete() {
      return valoaretichete;
    }
    public int getTotaldrepturi() {
      return Math.round(totaldrepturi);
    }
    public int getNrpersoaneintretinere() {
      return nrpersoaneintretinere;
    }
    public int getRestplata() {
      return restplata;
    }
    public float getSalariupeora() {
      return salariupeora;
    }
    public float getSalariupezi() {
      return salariupezi;
    }
    public int getZilelucrate() {
      return zilelucrate;
    }
    public int getDeducere() {
      return deducere;
    }
    public int getAn() {
      return an;
    }
    public Long getIdcontract() {
      return idcontract;
    }
    public int getLuna() {
      return luna;
    }

    //! SETTERS
    public void setNrtichete(int nrtichete) {
      this.nrtichete = nrtichete;
    }
    public void setZilecm(int zilecm) {
      this.zilecm = zilecm;
    }
    public void setZileco(int zileco) {
      this.zileco = zileco;
    }
    public void setZilec(int zilec) {
      this.zilec = zilec;
    }
    public void setZileconeplatit(int zileconeplatit) {
      this.zileconeplatit = zileconeplatit;
    }
    public void setDuratazilucru(int duratazilucru) {
      this.duratazilucru = duratazilucru;
    }
    public void setNorma(int norma) {
      this.norma = norma;
    }
    public void setOrelucrate(int orelucrate) {
      this.orelucrate = orelucrate;
    }
    public void setCam(float cam) {
      this.cam = cam;
    }
    public void setCas(float cas) {
      this.cas = cas;
    }
    public void setCass(float cass) {
      this.cass = cass;
    }
    public void setImpozit(float impozit) {
      this.impozit = impozit;
    }
    public void setValoaretichete(float valoaretichete) {
      this.valoaretichete = valoaretichete;
    }
    public void setTotaldrepturi(int totaldrepturi) {
      this.totaldrepturi = totaldrepturi;
    }
    public void setNrpersoaneintretinere(int nrpersoaneintretinere) {
      this.nrpersoaneintretinere = nrpersoaneintretinere;
    }
    public void setRestplata(int restplata) {
      this.restplata = restplata;
    }
    public void setSalariupeora(int salariupeora) {
      this.salariupeora = salariupeora;
    }
    public void setSalariupezi(int salariupezi) {
      this.salariupezi = salariupezi;
    }
    public void setZilelucrate(int zilelucrate) {
      this.zilelucrate = zilelucrate;
    }
    public void setDeducere(int deducere) {
      this.deducere = deducere;
    }
    public void setAn(int an) {
      this.an = an;
    }
    public void setIdcontract(Long idcontract) {
      this.idcontract = idcontract;
    }
    public void setLuna(int luna) {
      this.luna = luna;
    }
}

