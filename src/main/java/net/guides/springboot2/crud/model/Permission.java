package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission {

    private long id;
    @Column(name = "nume")
    private String nume;

    public Permission() { }

    public Permission( String nume ) {
        this.nume = nume;
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
    public String getNume() {
        return nume;
    }

    // SETTERS
    public void setNume(String nume) {
        this.nume = nume;
    }
} 

