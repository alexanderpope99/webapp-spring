package net.guides.springboot2.crud.payload.request;

import net.guides.springboot2.crud.model.Contract;

import java.time.LocalDate;

public class NewContractRequest {
    private Contract contract;
    private int idangajat;
    private LocalDate dataModificarii;

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public int getIdangajat() {
        return idangajat;
    }

    public void setIdangajat(int idangajat) {
        this.idangajat = idangajat;
    }

    public LocalDate getDataModificarii() {
        return dataModificarii;
    }

    public void setDataModificarii(LocalDate dataModificarii) {
        this.dataModificarii = dataModificarii;
    }
}
