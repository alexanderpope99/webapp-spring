package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.RealizariRetineri;

public class TicheteDTO {
	private int id;

	private String tip;

	private Long nr;

	private Long restituite;

	private Double valoare;

	private Boolean impozabil;

	private RealizariRetineri stat;

	public int getId() {
		return id;
	}

	public Integer getIdstat() {
		if (stat == null)
			return null;
		else
			return stat.getId();

	}

	public Boolean getImpozabil() {
		return impozabil;
	}

	public Long getNr() {
		return nr;
	}

	public Long getRestituite() {
		return restituite;
	}

	public String getTip() {
		return tip;
	}

	public Double getValoare() {
		return valoare;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setStat(RealizariRetineri idstat) {
		this.stat = idstat;
	}

	public void setImpozabil(Boolean impozabil) {
		this.impozabil = impozabil;
	}

	public void setNr(Long nr) {
		this.nr = nr;
	}

	public void setRestituite(Long restituite) {
		this.restituite = restituite;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public void setValoare(Double valoare) {
		this.valoare = valoare;
	}
}
