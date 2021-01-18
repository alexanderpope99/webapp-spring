package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "persoanaintretinere")
public class PersoanaIntretinere implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "nume", nullable = false)
	private String nume;

	@Column(name = "prenume", nullable = false)
	private String prenume;

	@Column(name = "cnp")
	private String cnp;

	@Column(name = "datanasterii")
	private LocalDate datanasterii;

	@Column(name = "grad")
	private String grad;

	@Column(name = "gradinvaliditate")
	private String gradinvaliditate;

	@Column(name = "intretinut", nullable = false)
	private Boolean intretinut;

	@Column(name = "coasigurat", nullable = false)
	private Boolean coasigurat;

	@JsonBackReference("persoanaintretinere-angajat")
	@ManyToOne
	@JoinColumn(name = "idangajat")
	private Angajat angajat;

	public PersoanaIntretinere() {
	}

	public PersoanaIntretinere(String nume, String prenume, String cnp, LocalDate datanasterii, String grad,
			String gradinvaliditate, Boolean intretinut, Boolean coasigurat, Angajat angajat) {
		this.nume = nume;
		this.prenume = prenume;
		this.cnp = cnp;
		this.datanasterii = datanasterii;
		this.grad = grad;
		this.gradinvaliditate = gradinvaliditate;
		this.intretinut = intretinut;
		this.coasigurat = coasigurat;
		this.angajat = angajat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCnp() {
		return cnp;
	}

	public Boolean getCoasigurat() {
		return coasigurat;
	}

	public LocalDate getDatanasterii() {
		return datanasterii;
	}

	public String getGrad() {
		return grad;
	}

	public String getGradinvaliditate() {
		return gradinvaliditate;
	}

	//public Angajat getAngajat() {
	//	return angajat;
	//}

	public int getIdangajat() {
		if(angajat == null)
			return 0;
			
		return angajat.getPersoana().getId();
	}

	public Boolean getIntretinut() {
		return intretinut;
	}

	public String getNume() {
		return nume;
	}

	public String getPrenume() {
		return prenume;
	}

	public void setCnp(String cnp) {
		this.cnp = cnp;
	}

	public void setCoasigurat(Boolean coasigurat) {
		this.coasigurat = coasigurat;
	}

	public void setDatanasterii(LocalDate datanasterii) {
		this.datanasterii = datanasterii;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public void setGradinvaliditate(String gradinvaliditate) {
		this.gradinvaliditate = gradinvaliditate;
	}

	public void setAngajat(Angajat angajat) {
		this.angajat = angajat;
	}

	public void setIntretinut(Boolean intretinut) {
		this.intretinut = intretinut;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}
}
