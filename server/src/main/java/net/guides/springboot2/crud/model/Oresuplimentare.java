package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oresuplimentare")
public class Oresuplimentare {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "nr")
	private Long nr;

	@Column(name = "procent")
	private Double procent;

	@Column(name = "includenormale")
	private Boolean includenormale;

	@Column(name = "total")
	private Double total;

	@Column(name = "idstatsalariat")
	private Long idstatsalariat;

	public Oresuplimentare() {
	}

	public Oresuplimentare(Long nr, Double procent, Boolean includenormale, Double total, Long idstatsalariat) {
		this.nr = nr;
		this.procent = procent;
		this.includenormale = includenormale;
		this.total = total;
		this.idstatsalariat = idstatsalariat;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIdstatsalariat(Long idstatsalariat) {
		this.idstatsalariat = idstatsalariat;
	}

	public Long getIdstatsalariat() {
		return idstatsalariat;
	}

	public void setProcent(Double procent) {
		this.procent = procent;
	}

	public Double getProcent() {
		return procent;
	}

	public void setNr(Long nr) {
		this.nr = nr;
	}

	public Long getNr() {
		return nr;
	}

	public Boolean getIncludenormale() {
		return includenormale;
	}

	public void setIncludenormale(Boolean includenormale) {
		this.includenormale = includenormale;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
}
