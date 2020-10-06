package net.guides.springboot2.crud.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "angajat")
public class Angajat {

	private Long idpersoana;
	private Long idcontract;
	private Integer idsocietate;

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "idsuperior")
	private Angajat superior;

	@OneToMany(mappedBy = "superior")
	private Set<Angajat> subordinates = new HashSet<Angajat>();

	public Angajat() {

	}

	public Angajat(Long idcontract, Integer idsocietate) {
		this.idcontract = idcontract;
		this.idsocietate = idsocietate;
	}

	@Id
	@Column(name = "idpersoana")
	public Long getIdpersoana() {
		return idpersoana;
	}

	public void setIdpersoana(Long idpersoana) {
		this.idpersoana = idpersoana;
	}

	@Column(name = "idcontract")
	public Long getIdcontract() {
		return idcontract;
	}

	public void setIdcontract(Long idcontract) {
		this.idcontract = idcontract;
	}

	@Column(name = "idsocietate")
	public Integer getIdsocietate() {
		return idsocietate;
	}

	public void setIdsocietate(Integer idsocietate) {
		this.idsocietate = idsocietate;
	}
}
