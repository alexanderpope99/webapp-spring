package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

public class ParametriiSalariuDTO {
	private int id;

	private Float salariumin;

	private Float salariuminstudiivechime;

	private Float salariumediubrut;

	private Float impozit;

	private Float cas;

	private Float cass;

	private Float cam;

	private Float valtichet;

	private LocalDate date;

	public Float getCam() {
		return cam;
	}

	public Float getCas() {
		return cas;
	}

	public Float getCass() {
		return cass;
	}

	public LocalDate getDate() {
		return date;
	}

	public int getId() {
		return id;
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

	public void setCam(Float cam) {
		this.cam = cam;
	}

	public void setCas(Float cas) {
		this.cas = cas;
	}

	public void setCass(Float cass) {
		this.cass = cass;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setId(int id) {
		this.id = id;
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
