package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "public.user")
public class User {

    private long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "nume")
    private String nume;
    @Column(name = "prenume")
    private String prenume;
    @Column(name = "societateselectata")
    private Long societateselectata;
    
    public User() { }

    public User( String username, String password, String nume, String prenume, Long societateselecteta ) {
        this.username = username;
        this.password = password;
        this.nume = nume;
        this.prenume = prenume;
        this.societateselectata = societateselecteta;
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
    public String getPassword() {
        return password;
    }
    public String getPrenume() {
        return prenume;
    }
    public Long getSocietateselectata() {
        return societateselectata;
    }
    public String getUsername() {
        return username;
    }

    // SETTERS
    public void setNume(String nume) {
        this.nume = nume;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }
    public void setSocietateselectata(Long societateselectata) {
        this.societateselectata = societateselectata;
    }
    public void setUsername(String username) {
        this.username = username;
    }
} 

