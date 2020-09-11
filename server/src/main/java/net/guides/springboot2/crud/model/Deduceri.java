package net.guides.springboot2.crud.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "deduceri")
public class Deduceri {

    private long id;
    @Column(name = "dela")
    private Date dela;
    @Column(name = "panala")
    private Date panala;
    @Column(name = "una")
    private Integer una;
    @Column(name = "doua")
    private Integer doua;
    @Column(name = "trei")
    private Integer trei;
    @Column(name = "patru")
    private Integer patru;

    public Deduceri() { }

    public Deduceri( Date dela, Date panala, Integer una, Integer doua, Integer trei, Integer patru) {
        this.dela = dela;
        this.panala = panala;
        this.una = una;
        this.doua = doua;
        this.trei = trei;
        this.patru = patru;
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
    public Date getDela() {
      return dela;
    }
    public Integer getDoua() {
      return doua;
    }
    public Date getPanala() {
      return panala;
    }
    public Integer getPatru() {
      return patru;
    }
    public Integer getTrei() {
      return trei;
    }
    public Integer getUna() {
      return una;
    }

    // SETTERS
    public void setDela(Date dela) {
      this.dela = dela;
    }
    public void setDoua(Integer doua) {
      this.doua = doua;
    }
    public void setPanala(Date panala) {
      this.panala = panala;
    }
    public void setPatru(Integer patru) {
      this.patru = patru;
    }
    public void setTrei(Integer trei) {
      this.trei = trei;
    }
    public void setUna(Integer una) {
      this.una = una;
    }
} 

