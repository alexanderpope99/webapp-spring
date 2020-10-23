package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parametriisalariu")
public class ParametriiSalariu {

    private long id;
    @Column(name = "salariumin")
    private Float salariumin;
    @Column(name = "salariuminstudiivechime")
    private Float salariuminstudiivechime;
    @Column(name = "salariumediubrut")
    private Float salariumediubrut;
    @Column(name = "impozit")
    private Float impozit;
    @Column(name = "cas")
    private Float cas;
    @Column(name = "cass")
    private Float cass;
    @Column(name = "cam")
    private Float cam;
    @Column(name = "valtichet")
    private Float valtichet;

    public ParametriiSalariu() { }

    public ParametriiSalariu( Float salariumin, Float salariuminstudiivechime, Float salariumediubrut, Float impozit, Float cas, Float cass, Float cam, Float valtichet) {
        this.salariumin = salariumin;
        this.salariuminstudiivechime = salariuminstudiivechime;
        this.salariumediubrut = salariumediubrut;
        this.impozit = impozit;
        this.cas = cas;
        this.cass = cass;
        this.cam = cam;
        this.valtichet = valtichet;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    // GETTERS
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
    public Float getSalariumediubrut() {
      return salariumediubrut;
    }
    public Float getSalariumin() {
      return salariumin;
    }
    public Float getSalariuminstudiivechime() {
      return salariuminstudiivechime;
    }
    public Float getValtichet() {
      return valtichet;
    }

    // SETTERS
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
    public void setSalariumediubrut(Float salariumediubrut) {
      this.salariumediubrut = salariumediubrut;
    }
    public void setSalariumin(Float salariumin) {
      this.salariumin = salariumin;
    }
    public void setSalariuminstudiivechime(Float salariuminstudiivechime) {
      this.salariuminstudiivechime = salariuminstudiivechime;
    }
    public void setValtichet(Float valtichet) {
      this.valtichet = valtichet;
    }
} 

