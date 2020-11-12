package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Oresuplimentare;

@Repository
public interface OresuplimentareRepository extends JpaRepository<Oresuplimentare, Integer> {
	List<Oresuplimentare> findByStatsalariat_Id(int idstat);

	@Query(value = "SELECT r.oresuplimentare FROM RealizariRetineri r WHERE r.luna = ?1 AND r.an = ?2 AND r.contract.id = ?3")
	List<Oresuplimentare> findByLunaAndAnAndIdcontract(int luna, int an, int idcontract);

	@Query(value = "SELECT COALESCE( (select sum(nr) from oresuplimentare where idstatsalariat = ?1), 0)", nativeQuery = true)
	Integer getNrOreSuplimentareByIdstat(int idstat);

	@Query(value = "SELECT COALESCE( (select sum(nr) from oresuplimentare where idstatsalariat = ?1 and procent = ?2), 0)", nativeQuery = true)
	Integer getNrByIdstatsalariatAndProcent(int idstat, double procent);
}
