package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "notificare")
public class Notificare implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "mesaj")
	private String mesaj;

	@Column(name = "data")
	private LocalDate data;

	@Column(name = "citit")
	private boolean citit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iduser")
	private User user;

	public Notificare() {
	}

	public Notificare(String mesaj, LocalDate data, boolean citit, User user) {
		this.mesaj = mesaj;
		this.data = data;
		this.citit = citit;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getMesaj() {
		return mesaj;
	}

	public void setMesaj(String mesaj) {
		this.mesaj = mesaj;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isCitit() {
		return citit;
	}

	public void setCitit(boolean citit) {
		this.citit = citit;
	}
}
