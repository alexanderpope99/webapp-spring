package net.guides.springboot2.crud.model;

import java.io.Serializable;

public class UserToRoleId implements Serializable {
    protected static final long serialVersionUID = 1L;
    protected long userid;
    protected long roleid;

    public UserToRoleId() {
    }

    public UserToRoleId(long userid, long roleid) {
        this.userid = userid;
        this.roleid = roleid;
    }
}