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
@Table(name = "zilecodisponibile")
public class ZileCODisponibile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "nr")
	private int nr;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract")
	private Contract contract;

	public ZileCODisponibile() {
	}

	public ZileCODisponibile(int nr, Contract contract) {
		this.nr = nr;
		this.contract = contract;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// GETTERS
	public Contract getContract() {
		return contract;
	}

	public int getNr() {
		return nr;
	}

	// SETTERS
	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public void setNr(int nr) {
		this.nr = nr;
	}
}
