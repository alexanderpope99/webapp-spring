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

	@Id
	@Column(name = "idpersoana")
	private Long idpersoana;

	@Column(name = "idcontract")
	private Long idcontract;

	@Column(name = "idsocietate")
	private Integer idsocietate;

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "idsuperior")
	private Angajat superior;

	@OneToMany(mappedBy = "superior")
	private Set<Angajat> subalterni = new HashSet<Angajat>();

	public Angajat() {

	}

	public Angajat(Long idcontract, Integer idsocietate) {
		this.idcontract = idcontract;
		this.idsocietate = idsocietate;
	}

	public Long getIdpersoana() {
		return idpersoana;
	}

	public void setIdpersoana(Long idpersoana) {
		this.idpersoana = idpersoana;
	}

	public Long getIdcontract() {
		return idcontract;
	} 

	public void setIdcontract(Long idcontract) {
		this.idcontract = idcontract;
	}

	public Integer getIdsocietate() {
		return idsocietate;
	}

	public void setIdsocietate(Integer idsocietate) {
		this.idsocietate = idsocietate;
	}

	public Set<Angajat> getSubalterni() {
		return subalterni;
	}

	public void setSubalterni(Set<Angajat> subalterni) {
		this.subalterni = subalterni;
	}

	public Angajat getSuperior() {
		return superior;
	}

	public void setSuperior(Angajat superior) {
		this.superior = superior;
	}
}
