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
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "luna_inchisa", uniqueConstraints = {@UniqueConstraint(columnNames = {"luna", "an", "idsocietate"})})
public class LunaInchisa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "luna", nullable = false)
	private int luna;

	@Column(name = "an", nullable = false)
	private int an;

	@JsonBackReference(value = "lunainchisa-societate")
	@JoinColumn(name = "idsocietate", referencedColumnName = "id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Societate societate;

	public LunaInchisa() {
	}

	public LunaInchisa(int luna, int an, Societate societate) {
		this.luna = luna;
		this.an = an;
		this.societate = societate;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getLuna() {
		return this.luna;
	}

	public void setLuna(int luna) {
		this.luna = luna;
	}

	public int getAn() {
		return this.an;
	}

	public void setAn(int an) {
		this.an = an;
	}

	public Societate getSocietate() {
		return societate;
	}

	public void setSocietate(Societate societate) {
		this.societate = societate;
	}

	// ! OTHER

	public LunaInchisa update(LunaInchisa newItem) {
		this.luna = newItem.luna;
		this.an = newItem.an;
		return this;
	}

	public boolean is(int luna, int an, int idsocietate) {
		return this.luna == luna && this.an == an && this.societate.getId() == idsocietate; 
	}
}
