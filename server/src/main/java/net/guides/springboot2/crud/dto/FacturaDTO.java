package net.guides.springboot2.crud.dto;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.CentruCost;
import net.guides.springboot2.crud.model.Societate;

public class FacturaDTO {
	private int id;

	private String denumirefurnizor;

	private String ciffurnizor;

	private String nr;

	private String moneda;

	private double sumafaratva;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate termenscadenta;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataplatii;

	private String tipachizitie;

	private String descriereactivitati;

	private boolean aprobat;

	private String observatii;

	private double sumaachitata;

	private MultipartFile fisier;

	// * ///////////////////
	private Angajat aprobator;
	private int idaprobator;
	private CentruCost centrucost;
	private int idcentrucost;
	private Societate societate;
	private int idsocietate;

	public int getIdaprobator() {
		if (aprobator == null)
			return idaprobator;
		return aprobator.getPersoana().getId();
	}

	public int getIdcentrucost() {
		if (centrucost == null)
			return idcentrucost;
		return centrucost.getId();
	}

	public int getIdsocietate() {
		if (societate == null)
			return idsocietate;
		return societate.getId();
	}

	public void setAprobator(Angajat aprobator) {
		this.aprobator = aprobator;
	}

	public void setIdaprobator(Angajat aprobator) {
		this.idaprobator = aprobator.getPersoana().getId();
	}

	public void setIdaprobator(int idaprobator) {
		this.idaprobator = idaprobator;
	}

	public void setCentrucost(CentruCost centrucost) {
		this.centrucost = centrucost;
	}

	public void setIdcentrucost(CentruCost centrucost) {
		this.idcentrucost = centrucost.getId();
	}

	public void setIdcentrucost(int idcentrucost) {
		this.idcentrucost = idcentrucost;
	}

	public void setSocietate(Societate societate) {
		this.societate = societate;
	}

	public void setIdsocietate(Societate societate) {
		this.idsocietate = societate.getId();
	}

	public void setIdsocietate(int idsocietate) {
		this.idsocietate = idsocietate;
	}
	// * ///////////////////
	public void setFisier(MultipartFile fisier) {
		this.fisier = fisier;
	}

	public byte[] getFisier() throws IOException {
		if(fisier != null)
			return fisier.getBytes();
		//	returns empty array instead of null
		else return new byte[0];
	}
	
	public String getNumefisier() {
		if(fisier != null)
			return fisier.getOriginalFilename();
		else return null;
	}

	public long getDimensiunefisier() {
		if(fisier != null)
			return fisier.getSize();
		else return 0;
	}
	// * ///////////////////

	public String getCiffurnizor() {
		return ciffurnizor;
	}

	public void setCiffurnizor(String ciffurnizor) {
		this.ciffurnizor = ciffurnizor;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(String data) {
		this.data = LocalDate.parse(data);
	}
	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalDate getDataplatii() {
		return dataplatii;
	}

	public void setDataplatii(String dataplatii) {
		this.dataplatii = LocalDate.parse(dataplatii);
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

	public void setTermenscadenta(String termenscadenta) {
		this.termenscadenta = LocalDate.parse(termenscadenta);
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
