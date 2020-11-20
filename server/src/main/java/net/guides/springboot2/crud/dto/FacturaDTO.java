package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.CentruCost;
import net.guides.springboot2.crud.model.Societate;

public class FacturaDTO {
	private int id;

	private String denumirefurnizor;

	private String ciffurnizor;

	private String nr;

	private LocalDate data;

	private String moneda;

	private double sumafaratva;

	private LocalDate termenscadenta;

	private String tipachizitie;

	private String descriereactivitati;

	private Angajat aprobator;

	private boolean aprobat;

	private String observatii;

	private CentruCost centrucost;

	private LocalDate dataplatii;

	private double sumaachitata;

	private Societate societate;

	public Integer getIdaprobator() {
		if (aprobator == null)
			return null;
		else
			return aprobator.getPersoana().getId();
	}

	public void setAprobator(Angajat idaprobator) {
		this.aprobator = idaprobator;
	}

	public Integer getIdcentrucost() {
		if (centrucost == null)
			return null;
		else
			return centrucost.getId();
	}

	public void setCentrucost(CentruCost idcentrucost) {
		this.centrucost = idcentrucost;
	}

	public Integer getIdsocietate() {
		if (societate == null)
			return null;
		else
			return societate.getId();
	}

	public void setSocietate(Societate idsocietate) {
		this.societate = idsocietate;
	}

	public String getCiffurnizor() {
		return ciffurnizor;
	}

	public void setCiffurnizor(String ciffurnizor) {
		this.ciffurnizor = ciffurnizor;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalDate getDataplatii() {
		return dataplatii;
	}

	public void setDataplatii(LocalDate dataplatii) {
		this.dataplatii = dataplatii;
	}

	public String getDenumirefurnizor() {
		return denumirefurnizor;
	}

	public void setDenumirefurnizor(String denumirefurnizor) {
		this.denumirefurnizor = denumirefurnizor;
	}

	public String getDescriereactivitati() {
		return descriereactivitati;
	}

	public void setDescriereactivitati(String descriereactivitati) {
		this.descriereactivitati = descriereactivitati;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	public String getObservatii() {
		return observatii;
	}

	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}

	public double getSumaachitata() {
		return sumaachitata;
	}

	public void setSumaachitata(double sumaachitata) {
		this.sumaachitata = sumaachitata;
	}

	public double getSumafaratva() {
		return sumafaratva;
	}

	public void setSumafaratva(double sumafaratva) {
		this.sumafaratva = sumafaratva;
	}

	public boolean isAprobat() {
		return aprobat;
	}

	public void setAprobat(boolean aprobat) {
		this.aprobat = aprobat;
	}

	public LocalDate getTermenscadenta() {
		return termenscadenta;
	}

	public void setTermenscadenta(LocalDate termenscadenta) {
		this.termenscadenta = termenscadenta;
	}

	public String getTipachizitie() {
		return tipachizitie;
	}

	public void setTipachizitie(String tipachizitie) {
		this.tipachizitie = tipachizitie;
	}

}
