package net.guides.springboot2.crud.model;

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
@Table(name = "zilecodisponibile")
public class ZileCODisponibile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "nr")
	private Long nr;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract")
	private Contract idcontract;

	public ZileCODisponibile() {
	}

	public ZileCODisponibile(Long nr, Contract idcontract) {
		this.nr = nr;
		this.idcontract = idcontract;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// GETTERS
	public Contract getIdcontract() {
		return idcontract;
	}

	public Long getNr() {
		return nr;
	}

	// SETTERS
	public void setIdcontract(Contract idcontract) {
		this.idcontract = idcontract;
	}

	public void setNr(Long nr) {
		this.nr = nr;
	}
}
