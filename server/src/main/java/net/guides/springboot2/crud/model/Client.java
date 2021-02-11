package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import net.guides.springboot2.crud.model.types.Moneda;

@Entity
@Table(name = "client")
public class Client implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "numecomplet")
  private String numecomplet;

  @Column(name = "nume")
  private String nume;

  @Column(name = "statut")
  private String statut;
  
  @Column(name = "nrregcom")
  private String nrregcom;

  @Column(name = "codfiscal")
  private String codfiscal;

  @Column(name = "cotatva")
  private String cotatva;

  @Column(name = "client")
  private Boolean client = false;

  @Column(name = "furnizor")
  private Boolean furnizor = false;

  @Column(name = "extern")
  private Boolean extern = false;

  @Column(name = "banca")
  private String banca;

  @Column(name = "sucursala")
  private String sucursala;

  @Column(name = "cont")
  private String cont;

  @Column(name = "moneda")
  @Enumerated(EnumType.STRING)
  private Moneda moneda = Moneda.RON;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "idadresa", referencedColumnName = "id")
  private Adresa adresa;

  @JsonBackReference(value = "client-societate")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idsocietate", referencedColumnName = "id")
  private Societate societate;

  @JsonBackReference(value = "client-facturi")
  @OneToMany(mappedBy = "client", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
  private List<Factura> facturi;

  public Client() {
  }

  public Client(String numecomplet, String nume, String statut, String nrregcom, String codfiscal, String cotatva, Adresa adresa, Boolean client, Boolean furnizor, Boolean extern, String banca, String sucursala, String cont, Moneda moneda) {
    this.numecomplet = numecomplet;
    this.nume = nume;
    this.statut = statut;
    this.nrregcom = nrregcom;
    this.codfiscal = codfiscal;
    this.cotatva = cotatva;
    this.adresa = adresa;
    this.client = client;
    this.furnizor = furnizor;
    this.extern = extern;
    this.banca = banca;
    this.sucursala = sucursala;
    this.cont = cont;
    this.moneda = moneda;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNumecomplet() {
    return this.numecomplet;
  }

  public void setNumecomplet(String numecomplet) {
    this.numecomplet = numecomplet;
  }

  public String getNume() {
    return this.nume;
  }

  public void setNume(String nume) {
    this.nume = nume;
  }

  public String getStatut() {
    return this.statut;
  }

  public void setStatut(String statut) {
    this.statut = statut;
  }

  public String getNrregcom() {
    return this.nrregcom;
  }

  public void setNrregcom(String nrregcom) {
    this.nrregcom = nrregcom;
  }

  public String getCodfiscal() {
    return this.codfiscal;
  }

  public void setCodfiscal(String codfiscal) {
    this.codfiscal = codfiscal;
  }

  public String getCotatva() {
    return this.cotatva;
  }

  public void setCotatva(String cotatva) {
    this.cotatva = cotatva;
  }

  public Adresa getAdresa() {
    return this.adresa;
  }

  public void setAdresa(Adresa adresa) {
    this.adresa = adresa;
  }

  public Boolean isClient() {
    return this.client;
  }

  public Boolean getClient() {
    return this.client;
  }

  public void setClient(Boolean client) {
    this.client = client;
  }

  public Boolean isFurnizor() {
    return this.furnizor;
  }

  public Boolean getFurnizor() {
    return this.furnizor;
  }

  public void setFurnizor(Boolean furnizor) {
    this.furnizor = furnizor;
  }

  public Boolean isExtern() {
    return this.extern;
  }

  public Boolean getExtern() {
    return this.extern;
  }

  public void setExtern(Boolean extern) {
    this.extern = extern;
  }

  public String getBanca() {
    return this.banca;
  }

  public void setBanca(String banca) {
    this.banca = banca;
  }

  public String getSucursala() {
    return this.sucursala;
  }

  public void setSucursala(String sucursala) {
    this.sucursala = sucursala;
  }

  public String getCont() {
    return this.cont;
  }

  public void setCont(String cont) {
    this.cont = cont;
  }

  public Moneda getMoneda() {
    return this.moneda;
  }

  public void setMoneda(Moneda moneda) {
    this.moneda = moneda;
  }

  public Societate getSocietate() {
    return societate;
  }

  public void setSocietate(Societate societate) {
    this.societate = societate;
  }

}
