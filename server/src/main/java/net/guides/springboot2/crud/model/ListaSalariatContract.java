package net.guides.springboot2.crud.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(ListaSalariatContractId.class)
@Table(name = "echipa")
public class ListaSalariatContract implements Serializable {

    @Id
    private int idsalariat;
    @Id
    private int idcontract;
    private Date dataschimbare;


    public ListaSalariatContract() {

    }

    public ListaSalariatContract(int idsalariat, int idcontract, Date dataschimbare) {
        this.idsalariat = idsalariat;
        this.idcontract = idcontract;
        this.dataschimbare = dataschimbare;
    }

    @Column(name="idsalariat")
    public int getIdsalariat() {
        return idsalariat;
    }

    public void setIdsalariat(int idsalariat) {
        this.idsalariat = idsalariat;
    }

    @Column(name="idcontract")
    public int getIdcontract() {
        return idcontract;
    }

    public void setIdcontract(int idcontract) {
        this.idcontract = idcontract;
    }

    @Column(name="dataschimbare")
    public Date getDataschimbare() {
        return dataschimbare;
    }

    public void setDataschimbare(Date dataschimbare) {
        this.dataschimbare = dataschimbare;
    }

}

class ListaSalariatContractId implements Serializable {
    protected int idsalariat;
    protected int idcontract;
    protected Date dataschimbare;

    public ListaSalariatContractId() {
    }

    public ListaSalariatContractId(int idsalariat, int idcontract, Date dataschimbare) {
        this.idsalariat = idsalariat;
        this.idcontract = idcontract;
        this.dataschimbare = dataschimbare;
    }


}

