package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.time.LocalDateTime;

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

	@Column(name = "titlu")
	private String titlu;

	@Column(name = "mesaj")
	private String mesaj;

	@Column(name = "timp")
	private LocalDateTime timp;

	@Column(name = "citit")
	private boolean citit;

	@Column(name = "hyperlink")
	private String hyperlink;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iduser")
	private User user;

	public Notificare() {
	}

	public Notificare(String mesaj, LocalDateTime timp, boolean citit, User user) {
		this.mesaj = mesaj;
		this.timp = timp;
		this.citit = citit;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getTimp() {
		return timp;
	}

	public void setTimp(LocalDateTime timp) {
		this.timp = timp;
	}

	public String getTitlu() {
		return titlu;
	}

	public void setTitlu(String titlu) {
		this.titlu = titlu;
	}

	public String getMesaj() {
		return mesaj;
	}

	public void setMesaj(String mesaj) {
		this.mesaj = mesaj;
	}

	public String getHyperlink() {
		return hyperlink;
	}

	public void setHyperlink(String hyperlink) {
		this.hyperlink = hyperlink;
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
