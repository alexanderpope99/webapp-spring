package net.guides.springboot2.crud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "zilecodisponibile")
public class ZileCODisponibile {

    private long id;
    @Column(name = "nr")
    private Long nr;
    @Column(name = "idcontract")
    private Long idcontract;

    public ZileCODisponibile() { }

    public ZileCODisponibile( Long nr, Long idcontract ) {
        this.nr = nr;
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
    public Long getIdcontract() {
        return idcontract;
    }
    public Long getNr() {
        return nr;
    }

    // SETTERS
    public void setIdcontract(Long idcontract) {
        this.idcontract = idcontract;
    }
    public void setNr(Long nr) {
        this.nr = nr;
    }
} 

