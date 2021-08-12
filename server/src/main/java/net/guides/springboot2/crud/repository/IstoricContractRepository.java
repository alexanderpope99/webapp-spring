package net.guides.springboot2.crud.repository;

import net.guides.springboot2.crud.model.IstoricContract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IstoricContractRepository extends JpaRepository<IstoricContract, Integer> {
    List<IstoricContract> findByAngajat_Persoana_IdOrderByDataModificariiDesc(int id);

}
