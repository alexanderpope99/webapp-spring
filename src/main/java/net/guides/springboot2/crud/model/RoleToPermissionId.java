package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class RoleToPermissionId implements Serializable {
    protected long roleid;
    protected long permissionid;

    public RoleToPermissionId() {
    }

    public RoleToPermissionId(long roleid, long permissionid) {
        this.roleid = roleid;
        this.permissionid = permissionid;
    }
}
