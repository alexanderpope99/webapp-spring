package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.RealizariRetineri;

public class OresuplimentareDTO {
	private int id;

	private Long nr;

	private Double procent;

	private Boolean includenormale;

	private Double total;

	private Integer idstatsalariat;
	private RealizariRetineri statsalariat;

	public int getId() {
		return id;
	}

	public Boolean getIncludenormale() {
		return includenormale;
	}

	public Long getNr() {
		return nr;
	}

	public Double getProcent() {
		return procent;
	}

	public Double getTotal() {
		return total;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIncludenormale(Boolean includenormale) {
		this.includenormale = includenormale;
	}

	public void setNr(Long nr) {
		this.nr = nr;
	}

	public void setProcent(Double procent) {
		this.procent = procent;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Integer getIdstatsalariat() {
		if (statsalariat == null)
			return idstatsalariat;
		else
			return statsalariat.getId();
	}

	public void setStatsalariat(RealizariRetineri statsalariat) {
		this.statsalariat = statsalariat;
	}
	public void setIdstatsalariat(RealizariRetineri statsalariat) {
		this.idstatsalariat = statsalariat.getId();
	}
	public void setIdstatsalariat(int idstatsalariat) {
		this.idstatsalariat = idstatsalariat;
	}
}
