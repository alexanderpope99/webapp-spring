package net.guides.springboot2.crud.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(ListaSalariatContractId.class)
@Table(name = "echipa")
public class ListaSalariatContract {
	@Id
	@Column(name = "idsalariat")
	private Long idsalariat;

	@Id
	@Column(name = "idcontract")
	private Long idcontract;

	@Column(name = "dataschimbare")
	private Date dataschimbare;

	public ListaSalariatContract() {

	}

	public ListaSalariatContract(Long idsalariat, Long idcontract, Date dataschimbare) {
		this.idsalariat = idsalariat;
		this.idcontract = idcontract;
		this.dataschimbare = dataschimbare;
	}

	public Long getIdsalariat() {
		return idsalariat;
	}

	public void setIdsalariat(Long idsalariat) {
		this.idsalariat = idsalariat;
	}

	public Long getIdcontract() {
		return idcontract;
	}

	public void setIdcontract(Long idcontract) {
		this.idcontract = idcontract;
	}

	public Date getDataschimbare() {
		return dataschimbare;
	}

	public void setDataschimbare(Date dataschimbare) {
		this.dataschimbare = dataschimbare;
	}

}

class ListaSalariatContractId implements Serializable {
	private static final long serialVersionUID = 1L;
	protected Long idsalariat;
	protected Long idcontract;
	protected Date dataschimbare;

	public ListaSalariatContractId() {
	}

	public ListaSalariatContractId(Long idsalariat, Long idcontract, Date dataschimbare) {
		this.idsalariat = idsalariat;
		this.idcontract = idcontract;
		this.dataschimbare = dataschimbare;
	}

}
