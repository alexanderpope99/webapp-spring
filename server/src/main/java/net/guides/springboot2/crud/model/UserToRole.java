package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
// import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@IdClass(UserToRoleId.class)
@Table(name = "usertorole")
public class UserToRole {

    @Id
    @Column(name = "userid", nullable = false)
    private long userid;
    @Id
    @Column(name = "roleid", nullable = false)
    private long roleid;

    public UserToRole() { }

    public UserToRole( Long roleid, Long userid ) {
        this.roleid = roleid;
        this.userid = userid;
    }

    // GETTERS
    public long getUserid() {
        return userid;
    }
    public Long getRoleid() {
        return roleid;
    }

    // SETTERS
    public void setUserid(long userid) {
        this.userid = userid;
    }
    public void setRoleid(Long roleid) {
        this.roleid = roleid;
    }
}

class UserToRoleId implements Serializable {
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

