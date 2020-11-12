package net.guides.springboot2.crud.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
	@Query(value = "SELECT c FROM Contract c WHERE c.id = (SELECT a.contract.id FROM Angajat a WHERE a.idpersoana = ?1)")
	Optional<Contract> findByIdPersoana(int idpersoana);
}