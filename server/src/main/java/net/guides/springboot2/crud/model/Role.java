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
    @Column(name = "descriere")
    private String descriere;

    public Role() {
    }

    public Role(String name, String descriere) {
        this.name = name;
        this.descriere = descriere;
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
    public String getDescriere() {
        return descriere;
    }

    public String getName() {
        return name;
    }

    // SETTERS
    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public void setName(String name) {
        this.name = name;
    }
}
