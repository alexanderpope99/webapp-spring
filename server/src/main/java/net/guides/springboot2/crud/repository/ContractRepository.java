package net.guides.springboot2.crud.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long>{
  @Query(
    value = "select * from contract where id = (select idcontract from angajat where idpersoana = ?1)",
    nativeQuery = true)
  Optional<Contract> findByIdPersoana(long idpersoana);
}