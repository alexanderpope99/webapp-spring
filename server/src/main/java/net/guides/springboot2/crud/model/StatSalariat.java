package net.guides.springboot2.crud.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "statsalariat")
public class StatSalariat {

    private long id;
    @Column(name = "data")
    private Date data;
    @Column(name = "idcontract")
    private Long idcontract;
    

    public StatSalariat() { }

    public StatSalariat( Date data, Long idcontract ) {
        this.data = data;
        this.idcontract = idcontract;
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
    public Date getData() {
        return data;
    }
    public Long getIdcontract() {
        return idcontract;
    }

    // SETTERS
    public void setData(Date data) {
        this.data = data;
    }
    public void setIdcontract(Long idcontract) {
        this.idcontract = idcontract;
    }
}

