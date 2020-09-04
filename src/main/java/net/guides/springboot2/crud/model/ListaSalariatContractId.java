package net.guides.springboot2.crud.model;

import java.io.Serializable;
import java.util.Date;

public class ListaSalariatContractId implements Serializable {
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
