package net.guides.springboot2.crud.model;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "platicont")
public class PlatiCont implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idcontbancar", referencedColumnName = "id")
	private ContBancar contbancar;

	@Column(name = "pentru", nullable = false)
	private String pentru;

	PlatiCont() {
	}

	PlatiCont(ContBancar contbancar, String pentru) {
		this.contbancar = contbancar;
		this.pentru = pentru;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ContBancar getContBancar() {
		return contbancar;
	}

	public void setContbancar(ContBancar contbancar) {
		this.contbancar = contbancar;
	}

	public String getPentru() {
		return pentru;
	}

	public void setPentru(String pentru) {
		this.pentru = pentru;
	}

}
