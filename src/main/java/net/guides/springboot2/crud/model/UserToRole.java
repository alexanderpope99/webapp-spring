package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
// import javax.persistence.Id;
import javax.persistence.Table;

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

