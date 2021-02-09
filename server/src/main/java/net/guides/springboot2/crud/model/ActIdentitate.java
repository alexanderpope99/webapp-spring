package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "actidentitate")
public class ActIdentitate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "cnp")
	private String cnp;
	@Column(name = "tip")
	private String tip;
	@Column(name = "serie")
	private String serie;
	@Column(name = "numar")
	private String numar;
	@Column(name = "datanasterii")
	private Date datanasterii;
	@Column(name = "eliberatde")
	private String eliberatde;
	@Column(name = "dataeliberarii")
	private String dataeliberarii;
	@Column(name = "loculnasterii")
	private String loculnasterii;

	@OneToOne(mappedBy = "actidentitate", fetch = FetchType.LAZY)
	private Persoana persoana;

	public ActIdentitate() {

	}

	public ActIdentitate(String cnp, String tip, String serie, String numar, Date datanasterii, String eliberatde,
			String dataeliberarii, String loculnasterii) {
		this.cnp = cnp;
		this.tip = tip;
		this.serie = serie;
		this.numar = numar;
		this.datanasterii = datanasterii;
		this.eliberatde = eliberatde;
		this.dataeliberarii = dataeliberarii;
		this.loculnasterii = loculnasterii;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCnp() {
		return cnp == null ? "" : cnp;
	}

	public void setCnp(String cnp) {
		this.cnp = cnp;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getSerie() {
		return serie == null ? "" : serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getNumar() {
		return numar == null ? "" : numar;
	}

	public void setNumar(String numar) {
		this.numar = numar;
	}

	public Date getDatanasterii() {
		return datanasterii;
	}

	public void setDatanasterii(Date datanasterii) {
		this.datanasterii = datanasterii;
	}

	public String getEliberatde() {
		return eliberatde;
	}

	public void setEliberatde(String eliberatde) {
		this.eliberatde = eliberatde;
	}

	public String getDataeliberarii() {
		return dataeliberarii;
	}

	public void setDataeliberarii(String dataeliberarii) {
		this.dataeliberarii = dataeliberarii;
	}

	public String getLoculnasterii() {
		return loculnasterii;
	}

	public void setLoculnasterii(String loculnasterii) {
		this.loculnasterii = loculnasterii;
	}

}
