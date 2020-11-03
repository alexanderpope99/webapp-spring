package net.guides.springboot2.crud.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "echipa")
public class Echipa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iddepartament")
	private Departament iddepartament;

	@Column(name = "nume")
	private String nume;

	@OneToMany(mappedBy = "idechipa")
	private Set<Contract> contracte;

	public Echipa() {

	}

	public Echipa(Departament iddepartament, String nume) {
		this.iddepartament = iddepartament;
		this.nume = nume;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Departament getIddepartament() {
		return iddepartament;
	}

	public void setIddepartament(Departament iddepartament) {
		this.iddepartament = iddepartament;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public Set<Contract> getContracte() {
		return contracte;
	}

	public void setContracte(Set<Contract> contracte) {
		this.contracte = contracte;
	}

}
