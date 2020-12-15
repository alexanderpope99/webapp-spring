package net.guides.springboot2.crud.dto;

import java.time.LocalDate;

import net.guides.springboot2.crud.model.User;

public class NotificareDTO {
	private int id;

	private String mesaj;

	private LocalDate data;

	private boolean citit;

	private Integer iduser;

	public Integer getIduser() {
		return iduser;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMesaj() {
		return mesaj;
	}

	public void setMesaj(String mesaj) {
		this.mesaj = mesaj;
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
