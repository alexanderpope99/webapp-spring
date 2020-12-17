package net.guides.springboot2.crud.dto;

import java.time.LocalDateTime;

import net.guides.springboot2.crud.model.User;

public class NotificareDTO {
	private int id;

	private String mesaj;

	private String titlu;

	private String hyperlink;

	private LocalDateTime timp;

	private boolean citit;

	private Integer iduser;

	public Integer getIduser() {
		return iduser;
	}

	public LocalDateTime getTimp() {
		return timp;
	}

	public void setTimp(LocalDateTime timp) {
		this.timp = timp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public boolean isCitit() {
		return citit;
	}

	public void setCitit(boolean citit) {
		this.citit = citit;
	}

	public void setIduser(int iduser) {
		this.iduser = iduser;
	}

	public void setIduserObj(User user) {
		this.iduser = user.getId();
	}
}
