package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "desc")
    private String desc;

    public Role() { }

    public Role( String name, String desc ) {
        this.name = name;
        this.desc = desc;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    // GETTERS
    public String getDesc() {
        return desc;
    }
    public String getName() {
        return name;
    }

    // SETTERS
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setName(String name) {
        this.name = name;
    }
}

