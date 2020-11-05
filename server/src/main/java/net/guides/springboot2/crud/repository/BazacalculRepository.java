package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Bazacalcul;

@Repository
public interface BazacalculRepository extends JpaRepository<Bazacalcul, Integer> {

	@Query(value = "select * from bazacalcul where idangajat = ?1 order by an desc, luna desc", nativeQuery = true)
	public List<Bazacalcul> findByIdangajatOrderByAnDescLunaDesc(int idangajat);

	@Query(value = "select * from bazacalcul where luna = ?1 and an = ?2 and idangajat = ?3", nativeQuery = true)
	public Bazacalcul findByLunaAndAnAndIdangajat(int luna, int an, int idangajat);

	@Query(value = "select exists(select * from bazacalcul where luna = ?1 and an = ?2 and idangajat = ?3)", nativeQuery = true)
	public boolean existsByLunaAndAnAndIdangajat(int luna, int an, int idangajat);
}