package net.guides.springboot2.crud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
	@Query(value = "SELECT a.contract FROM Angajat a WHERE a.idpersoana = ?1")
	public Optional<Contract> findByIdPersoana(int idpersoana);

	public List<Contract> findByAngajat_Societate_Id(int idsocietate);
	
	public int countByGradinvaliditateAndAngajat_Societate_Id(String gradinvaliditate, int idsocietate);

	@Query(value = "SELECT SUM(c.normalucru) FROM Contract c WHERE c.angajat.societate.id = ?1")
	public long getSumaNormaLucruSocietate(int idsocietate);
}