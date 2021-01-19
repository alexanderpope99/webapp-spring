package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Persoana;

@Repository
public interface PersoanaRepository extends JpaRepository<Persoana, Integer> {
	@Query(value = "SELECT a.persoana from Angajat a WHERE a.societate.id = ?1 ORDER BY a.persoana.nume ASC, a.persoana.prenume ASC")
	List<Persoana> findByIdsocietate(int idsocietate);

	@Query(value = "SELECT a.persoana from Angajat a WHERE a.contract.id IS NOT NULL AND a.societate.id = ?1 ORDER BY a.persoana.nume, a.persoana.prenume")
	List<Persoana> findByIdsocietateWithContract(int idsocietate);
}
