package net.guides.springboot2.crud.dto;

import net.guides.springboot2.crud.model.ERole;

public class RoleDTO {
	private int id;

	private ERole name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name.name();
	}

	public void setName(ERole name) {
		this.name = name;
	}
}
