package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;

@MappedSuperclass
public class Concediu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "dela", nullable = false)
	private LocalDate dela;

	@Column(name = "panala", nullable = false)
	private LocalDate panala;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract", nullable = false)
	private Contract contract;

	public Concediu() {
	}

	public Concediu(LocalDate dela, LocalDate panala, Contract contract) {
		this.dela = dela;
		this.panala = panala;
		this.contract = contract;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDela() {
		return this.dela;
	}

	public void setDela(LocalDate dela) {
		this.dela = dela;
	}

	public LocalDate getPanala() {
		return this.panala;
	}

	public void setPanala(LocalDate panala) {
		this.panala = panala;
	}

	public Contract getContract() {
		return this.contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	// ! OTHER

	public String getNumeangajat() {
		return this.contract.getAngajat().getPersoana().getNumeIntreg();
	}

	public boolean overlaps() throws ResourceNotFoundException {
		if (contract == null)
			throw new ResourceNotFoundException("Concediul nu are contract");
		List<Concediu> concediiExistente = new ArrayList<>();
		concediiExistente.addAll(contract.getConcediiOdihna());
		concediiExistente.addAll(contract.getConcediiMedicale());

		// remove slef
		concediiExistente.removeIf(c -> this.id == c.getId());

		// scrise separat pentru lizibilitate
		for (Concediu concediu : concediiExistente) {
			// co includes concediu.dela: co.dela < concediu.dela < co.panala
			if (dela.compareTo(concediu.getDela()) >= 0 && panala.compareTo(concediu.getDela()) <= 0)
				return true;

			// co includes concediu.panala: co.dela < concediu.dela < co.panala
			if (dela.compareTo(concediu.getPanala()) >= 0 && panala.compareTo(concediu.getPanala()) <= 0)
				return true;

			// overlaps - co.dela inside concediu: concediu.dela < co.dela < concediu.panala
			if (dela.compareTo(concediu.getDela()) >= 0 && dela.compareTo(concediu.getPanala()) <= 0)
				return true;

			// overlaps co.panala inside concediu: concediu.dela < co.panala < concediu.panla
			if (panala.compareTo(concediu.getDela()) >= 0 && panala.compareTo(concediu.getPanala()) <= 0)
				return true;
		}

		return false;
	}

	public boolean overlaps(List<LunaInchisa> luniInchise) throws ResourceNotFoundException {
		if (!overlaps()) {
			for (LunaInchisa li : luniInchise) {
				if (dela.getYear() == li.getAn() && li.getLuna() == dela.getMonthValue() || panala.getYear() == li.getAn() && li.getLuna() == panala.getMonthValue())
					return true;
			}
		}

		return false;
	}

	public int getNrZile() {
		return (int) ChronoUnit.DAYS.between(dela, panala);
	}
}
