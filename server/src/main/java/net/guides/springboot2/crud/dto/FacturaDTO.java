package net.guides.springboot2.crud.dto;

import java.time.LocalDate;
import java.util.List;

import net.guides.springboot2.crud.model.Client;
import net.guides.springboot2.crud.model.Proiect;

public class FacturaDTO {
	private int id;

	private String serie;
	private String numar;
	private String nrAvizInsotire;
	private String titlu;
	private LocalDate dataExpedierii;
	private String oraExpedierii;
	private Float totalFaraTva;
	private Float tva;

	private Client client;
	private Proiect proiect;

	private List<ProdusDTO> produse;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDataexpedierii() {
		return dataExpedierii;
	}

	public void setDataexpedierii(LocalDate dataexpedierii) {
		this.dataExpedierii = dataexpedierii;
	}

	public String getNravizinsotire() {
		return nrAvizInsotire;
	}

	public void setNravizinsotire(String nravizinsotire) {
		this.nrAvizInsotire = nravizinsotire;
	}

	public String getNumar() {
		return numar;
	}

	public void setNumar(String numar) {
		this.numar = numar;
	}

	public String getOraexpedierii() {
		return oraExpedierii;
	}

	public void setOraexpedierii(String oraexpedierii) {
		this.oraExpedierii = oraexpedierii;
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
	
	public List<ProdusDTO> getProduse() {
		return produse;
	}

	public void setProduse(List<ProdusDTO> produse) {
		this.produse = produse;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Proiect getProiect() {
		return proiect;
	}

	public void setProiect(Proiect proiect) {
		this.proiect = proiect;
	}
	
}


