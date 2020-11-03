package net.guides.springboot2.crud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission implements Serializable {
	private static final long serialVersionUID = 1L;

    private long id;
    @Column(name = "name")
    private String name;

    public Permission() { }

    public Permission( String name ) {
        this.name = name;
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
    public String getName() {
        return name;
    }

    // SETTERS
    public void setName(String name) {
        this.name = name;
    }
} 

