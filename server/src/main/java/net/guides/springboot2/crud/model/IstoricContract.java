package net.guides.springboot2.crud.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "istoric_contract")
public class IstoricContract implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idangajat", referencedColumnName = "idpersoana")
	private Angajat angajat;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idcontract", referencedColumnName = "id")
	private Contract contract;

	@Column(name = "datamodificarii")
	private LocalDate dataModificarii;

	public IstoricContract() {}

	public IstoricContract(Angajat angajat, Contract contract, LocalDate dataModificarii) {
		this.angajat = angajat;
		this.contract = contract;
		this.dataModificarii = dataModificarii;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public LocalDate getDataModificarii() {
		return dataModificarii;
	}

	public void setDataModificarii(LocalDate dataModificarii) {
		this.dataModificarii = dataModificarii;
	}

	public Angajat getAngajat() {
		return angajat;
	}

	public Contract getContract() {
		return contract;
	}

	public void setAngajat(Angajat angajat) {
		this.angajat = angajat;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}


	public IstoricContract update(IstoricContract newIstoric) {
		dataModificarii = newIstoric.dataModificarii;
		return this;
	}

}