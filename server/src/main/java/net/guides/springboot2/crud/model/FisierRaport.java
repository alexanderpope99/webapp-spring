package net.guides.springboot2.crud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "factura")
public class FisierRaport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "numefisier")
	private String numefisier;

	@Column(name = "dimensiunefisier")
	private Long dimensiunefisier;

	@JsonIgnore
	@Lob
	@Column(name = "fisier")
	private byte[] fisier;
	
	@JsonBackReference(value = "fisier-user")
	@ManyToOne
	@Column(name = "iduser")
	private User user;

	public FisierRaport() { }

	public FisierRaport(String numefisier, Long dimensiunefisier, byte[] fisier) {
		this.numefisier = numefisier;
		this.dimensiunefisier = dimensiunefisier;
		this.fisier = fisier;
	}

	public String getNumefisier() {
		return numefisier;
	}
	public Long getDimensiunefisier() {
		return dimensiunefisier;
	}
	public byte[] getFisier() {
		return fisier;
	}

	public void setDimensiunefisier(Long dimensiunefisier) {
		this.dimensiunefisier = dimensiunefisier;
	}
	public void setFisier(byte[] fisier) {
		this.fisier = fisier;
	}
	public void setNumefisier(String numefisier) {
		this.numefisier = numefisier;
	}

}
