package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="bazacalcul")
public class Bazacalcul {
	public Bazacalcul() {}

	private long id;
	@Column(name = "luna")
	private int luna;
	@Column(name = "zilelucrate")
	private int zilelucrate;
	@Column(name = "salariurealizat")
	private int salariurealizat;

	public Bazacalcul( int luna, int zilelucrate, int salariurealizat ) {
		this.luna = luna;
		this.zilelucrate = zilelucrate;
		this.salariurealizat = salariurealizat;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
	}
	
	//! GETTERS
	public int getLuna() {
		return luna;
	}
	public int getSalariurealizat() {
		return salariurealizat;
	}
	public int getZilelucrate() {
		return zilelucrate;
	}

	//! SETTERS
	public void setLuna(int luna) {
		this.luna = luna;
	}
	public void setSalariurealizat(int salariurealizat) {
		this.salariurealizat = salariurealizat;
	}
	public void setZilelucrate(int zilelucrate) {
		this.zilelucrate = zilelucrate;
	}
}
