package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
// import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@IdClass(RoleToPermissionId.class)
@Table(name = "roletopermission")
public class RoleToPermission {

    @Id
    @Column(name = "roleid", nullable = false)
    private long roleid;
    @Id
    @Column(name = "permissionid", nullable = false)
    private long permissionid;

    public RoleToPermission() { }

    public RoleToPermission( Long roleid, Long permissionid ) {
        this.roleid = roleid;
        this.permissionid = permissionid;
    }

    // GETTERS
    public Long getPermissionid() {
        return permissionid;
    }
    public Long getRoleid() {
        return roleid;
    }

    // SETTERS
    public void setPermissionid(Long permissionid) {
        this.permissionid = permissionid;
    }
    public void setRoleid(Long roleid) {
        this.roleid = roleid;
    }
}

