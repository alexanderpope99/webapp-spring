package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.util.ArrayList;
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
    @Column(name = "luna", nullable = false)
    private Integer luna;
    @Column(name = "an", nullable = false)
    private Integer an;
    @Column(name = "salariudebaza")
    private Integer salariudebaza;
    @Column(name = "functie")
    private String functie;

    @Column(name = "nrtichete", nullable = false)
    private Integer nrtichete = 0;
    @Column(name = "zileco", nullable = false)
    private Integer zileco = 0;
    @Column(name = "zilecolucratoare", nullable = false)
    private Integer zilecolucratoare = 0;
    @Column(name = "zilecfp", nullable = false)
    private Integer zilecfp = 0;
    @Column(name = "zilecfplucratoare", nullable = false)
    private Integer zilecfplucratoare = 0;
    @Column(name = "zilecm", nullable = false)
    private Integer zilecm = 0;
    @Column(name = "zilecmlucratoare", nullable = false)
    private Integer zilecmlucratoare = 0;
    @Column(name = "zilec", nullable = false)
    private Integer zilec = 0;
    @Column(name = "zileplatite", nullable = false)
    private Integer zileplatite = 0;

    @Column(name = "impozitscutit", nullable = false)
    private Integer impozitscutit = 0;

    @Column(name = "valcm", nullable = false)
    private Integer valcm = 0;
    @Column(name = "valcmfnuass")
    private Integer valcmfnuass = 0;
    @Column(name = "valcmfaambp")
    private Integer valcmfaambp = 0;
    @Column(name = "valco", nullable = false)
    private Integer valco = 0;

    @Column(name = "norma", nullable = false)
    private Integer norma = 0; // nr zile lucratoare in luna
    @Column(name = "duratazilucru", nullable = false)
    private Integer duratazilucru = 0; // contract.normalucru
    @Column(name = "zilecontract", nullable = false)
    private Integer zilecontract = 0;
    @Column(name = "zilelucrate", nullable = false)
    private Integer zilelucrate = 0;
    @Column(name = "orelucrate", nullable = false)
    private Integer orelucrate = 0;

    @Column(name = "totaldrepturi", nullable = false)
    private Integer totaldrepturi = 0;

    @Column(name = "salariurealizat", nullable = false)
    private Integer salariurealizat = 0;

    @Column(name = "venitnet", nullable = false)
    private Integer venitnet = 0;

    @Column(name = "bazaimpozit", nullable = false)
    private Integer bazaimpozit = 0;

    @Column(name = "salariupezi", nullable = false)
    private Float salariupezi = 0f;
    @Column(name = "salariupeora", nullable = false)
    private Float salariupeora = 0f;

    @Column(name = "cas", nullable = false)
    private Float cas = 0f;
    @Column(name = "cass", nullable = false)
    private Float cass = 0f;
    @Column(name = "cam", nullable = false)
    private Float cam = 0f;
    @Column(name = "impozit", nullable = false)
    private Float impozit = 0f;
    @Column(name = "valoaretichete", nullable = false)
    private Float valoaretichete = 0f;

    @Column(name = "restplata", nullable = false)
    private Integer restplata = 0;

    @Column(name = "nrpersoaneintretinere", nullable = false)
    private Integer nrpersoaneintretinere = 0;
    @Column(name = "deducere", nullable = false)
    private Integer deducere = 0;

    @Column(name = "primabruta", nullable = false)
    private Integer primabruta = 0;

    @Column(name = "totaloresuplimentare", nullable = false)
    private Integer totaloresuplimentare = 0;

    @Column(name = "nroresuplimentare", nullable = false)
    private Integer nroresuplimentare = 0;

    @Column(name = "coneefectuat")
    private Integer coneefectuat = 0;

    @JsonBackReference(value = "stat-contract")
    @ManyToOne
    @JoinColumn(name = "idcontract", referencedColumnName = "id", nullable = false)
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

    public Integer getConeefectuat() {
        return coneefectuat == null || coneefectuat < 0 ? 0 : coneefectuat;
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

    public int getValcmsocietate() {
        return valcm - getValcmfaambp() - getValcmfnuass();
    }

    public int getValcmfaambp() {
        return valcmfaambp == null ? 0 : valcmfaambp;
    }

    public void setValcmfaambp(Integer valcmfaambp) {
        this.valcmfaambp = valcmfaambp;
    }

    public int getValcmfnuass() {
        return valcmfnuass == null ? 0 : valcmfnuass;
    }

    public void setValcmfnuass(Integer valcmfnuass) {
        this.valcmfnuass = valcmfnuass;
    }

    public void setConeefectuat(Integer coneefectuat) {
        this.coneefectuat = coneefectuat;
    }

    public Integer getSalariudebaza() {
        if (salariudebaza != null)
            return salariudebaza;
        else return contract.getSalariutarifar();
    } // exista pt. a pastra in state salariul in cazul in care se modifica salariul din contract.

    public void setSalariudebaza(Integer salariudebaza) {
        this.salariudebaza = salariudebaza;
    }

    public String getFunctie() {
        return functie;
    }

    public void setFunctie(String functie) {
        this.functie = functie;
    }

    // ! OTHER

    public int getCheltuieliangajator() {
        return Math.round(restplata + cam + valoaretichete);
    }

    public int getVenitbrut() {
        return Math.round(totaldrepturi + valoaretichete);
    }

    public int getRestplatabrut() {
        return Math.round(venitnet - impozit);
    }

    public void addOreSuplimentare(Oresuplimentare oresuplimentare) {
        if (this.oresuplimentare == null)
            this.oresuplimentare = new ArrayList<>();

        this.oresuplimentare.add(oresuplimentare);
    }

    public void checkData() throws ResourceNotFoundException {
        if (contract == null)
            throw new ResourceNotFoundException("Contractul lipseste pt realizariRetineri.id = " + id);
        if (primabruta == null)
            throw new ResourceNotFoundException("Prima bruta nu are valoare pt realizariRetineri.id = " + id);
        if (totaloresuplimentare == null)
            throw new ResourceNotFoundException("Total ore suplimentare nu are valoare pt realizariRetineri.id = " + id);
        if (nroresuplimentare == null)
            throw new ResourceNotFoundException("Numarul de ore suplimentare nu are valoare pt realizariRetineri.id = " + id);
    }

    public RealizariRetineri fixValuesMissing() throws ResourceNotFoundException {
        if (nrtichete == null)
            this.nrtichete = 0;
        if (zileco == null)
            this.zileco = 0;
        if (zilecolucratoare == null)
            this.zilecolucratoare = 0;
        if (zilecfp == null)
            this.zilecfp = 0;
        if (zilecfplucratoare == null)
            this.zilecfplucratoare = 0;
        if (zilecm == null)
            this.zilecm = 0;
        if (zilecmlucratoare == null)
            this.zilecmlucratoare = 0;
        if (zilec == null)
            this.zilec = 0;
        if (zileplatite == null)
            this.zileplatite = 0;

        if (impozitscutit == null)
            this.impozitscutit = 0;

        if (valcm == null)
            this.valcm = 0;
        if (valco == null)
            this.valco = 0;

        if (norma == null)
            this.norma = 0;
        if (duratazilucru == null)
            this.duratazilucru = 0;
        if (zilecontract == null)
            this.zilecontract = 0;
        if (zilelucrate == null)
            this.zilelucrate = 0;
        if (orelucrate == null)
            this.orelucrate = 0;

        if (totaldrepturi == null)
            this.totaldrepturi = 0;

        if (salariurealizat == null)
            this.salariurealizat = 0;

        if (venitnet == null)
            this.venitnet = 0;

        if (bazaimpozit == null)
            this.bazaimpozit = 0;

        if (salariupezi == null)
            this.salariupezi = 0f;
        if (salariupeora == null)
            this.salariupeora = 0f;

        if (cas == null)
            this.cas = 0f;
        if (cass == null)
            this.cass = 0f;
        if (cam == null)
            this.cam = 0f;
        if (impozit == null)
            this.impozit = 0f;
        if (valoaretichete == null)
            this.valoaretichete = 0f;

        if (restplata == null)
            this.restplata = 0;

        if (nrpersoaneintretinere == null)
            this.nrpersoaneintretinere = 0;
        if (deducere == null)
            this.deducere = 0;

        if (primabruta == null)
            this.primabruta = 0;

        if (totaloresuplimentare == null)
            this.totaloresuplimentare = 0;

        if (nroresuplimentare == null)
            this.nroresuplimentare = 0;

        this.checkData();

        return this;
    }
}
