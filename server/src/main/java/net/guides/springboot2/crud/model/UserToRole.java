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
public class UserToRole implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "userid", nullable = false)
    private int userid;
    @Id
    @Column(name = "roleid", nullable = false)
    private int roleid;

    public UserToRole() { }

    public UserToRole( int roleid, int userid ) {
        this.roleid = roleid;
        this.userid = userid;
    }

    // GETTERS
    public int getUserid() {
        return userid;
    }
    public int getRoleid() {
        return roleid;
    }

    // SETTERS
    public void setUserid(int userid) {
        this.userid = userid;
    }
    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }
}

class UserToRoleId implements Serializable {
		protected static final long serialVersionUID = 1L;
		
    protected int userid;
    protected int roleid;

    public UserToRoleId() {
    }

    public UserToRoleId(int userid, int roleid) {
        this.userid = userid;
        this.roleid = roleid;
    }
}

