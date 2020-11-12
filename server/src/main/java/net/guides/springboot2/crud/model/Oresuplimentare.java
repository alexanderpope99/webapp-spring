package net.guides.springboot2.crud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "oresuplimentare")
public class Oresuplimentare implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "nr")
	private Long nr;

	@Column(name = "procent")
	private Double procent;

	@Column(name = "includenormale")
	private Boolean includenormale;

	@Column(name = "total")
	private Double total;

	// @JsonManagedReference(value = "oresuplimentare-realizariretineri")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idstatsalariat")
	private RealizariRetineri statsalariat;

	public Oresuplimentare() {
	}

	public Oresuplimentare(Long nr, Double procent, Boolean includenormale, Double total,
			RealizariRetineri statsalariat) {
		this.nr = nr;
		this.procent = procent;
		this.includenormale = includenormale;
		this.total = total;
		this.statsalariat = statsalariat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setStatsalariat(RealizariRetineri statsalariat) {
		this.statsalariat = statsalariat;
	}

	public RealizariRetineri getStatsalariat() {
		return statsalariat;
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
