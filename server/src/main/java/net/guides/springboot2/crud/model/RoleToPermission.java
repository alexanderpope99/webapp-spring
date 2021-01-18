package net.guides.springboot2.crud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(RoleToPermissionId.class)
@Table(name = "roletopermission")
public class RoleToPermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "roleid", nullable = false)
	private int roleid;
	@Id
	@Column(name = "permissionid", nullable = false)
	private int permissionid;

	public RoleToPermission() {
	}

	public RoleToPermission(int roleid, int permissionid) {
		this.roleid = roleid;
		this.permissionid = permissionid;
	}

	// GETTERS
	public int getPermissionid() {
		return permissionid;
	}

	public int getRoleid() {
		return roleid;
	}

	// SETTERS
	public void setPermissionid(int permissionid) {
		this.permissionid = permissionid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
}

class RoleToPermissionId implements Serializable {
	private static final long serialVersionUID = 1L;

	protected int roleid;
	protected int permissionid;

	public RoleToPermissionId() {
	}

	public RoleToPermissionId(int roleid, int permissionid) {
		this.roleid = roleid;
		this.permissionid = permissionid;
	}
}