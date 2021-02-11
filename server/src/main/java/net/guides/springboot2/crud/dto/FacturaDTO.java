package net.guides.springboot2.crud.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import net.guides.springboot2.crud.model.Client;
import net.guides.springboot2.crud.model.Produs;
import net.guides.springboot2.crud.model.Proiect;

public class FacturaDTO {
	private int id;

	private String serie;
	private String numar;
	private String nravizinsotire;
	private String titlu;
	private LocalDate dataexpedierii;
	private String oraexpedierii;
	private Float totalFaraTva;
	private Float tva;

	private int idclient;

	private Client client;

	private List<Produs> produse;

	private List<ProiectJSON> proiecte;

	public int getId() {
		return id;
	}
	public List<ProiectJSON> getProiecte() {
		return proiecte;
	}

	public void setId(int id) {
		this.id = id;
	}
	public void setProiecte(List<ProiectJSON> proiecte) {
		this.proiecte = proiecte;
	}
	public void setProiecteClass(List<Proiect> proiecte) {
    this.proiecte = new ArrayList<>();
    proiecte.forEach(pr -> this.proiecte.add(new ProiectJSON(pr.getId(), pr.getNume())));
	}

	public LocalDate getDataexpedierii() {
		return dataexpedierii;
	}

	public void setDataexpedierii(LocalDate dataexpedierii) {
		this.dataexpedierii = dataexpedierii;
	}

	public String getNravizinsotire() {
		return nravizinsotire;
	}

	public void setNravizinsotire(String nravizinsotire) {
		this.nravizinsotire = nravizinsotire;
	}

	public String getNumar() {
		return numar;
	}

	public void setNumar(String numar) {
		this.numar = numar;
	}

	public String getOraexpedierii() {
		return oraexpedierii;
	}

	public void setOraexpedierii(String oraexpedierii) {
		this.oraexpedierii = oraexpedierii;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getTitlu() {
		return titlu;
	}

	public void setTitlu(String titlu) {
		this.titlu = titlu;
	}

	public Float getTotalFaraTva() {
		return totalFaraTva;
	}

	public void setTotalFaraTva(Float totalFaraTva) {
		this.totalFaraTva = totalFaraTva;
	}

	public Float getTva() {
		return tva;
	}

	public void setTva(Float tva) {
		this.tva = tva;
	}
	
	public Float getTotal(){
		return totalFaraTva+tva;
	}
	
	public List<Produs> getProduse() {
		return produse;
	}

	public void setProduse(List<Produs> produse) {
		this.produse = produse;
	}

	public Integer getIdclient() {
		if (client == null)
			return idclient;
		else
			return client.getId();
	}

	public void setClient(Client client) {
		this.client = client;
	}
	public void setIdclient(int idclient) {
		this.idclient = idclient;
	}
}


