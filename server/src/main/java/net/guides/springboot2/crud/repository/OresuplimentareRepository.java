package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Oresuplimentare;

@Repository
public interface OresuplimentareRepository extends JpaRepository<Oresuplimentare, Long>{
    List<Oresuplimentare> findByIdstatsalariat(long idstat);

    @Query(value = "select * from oresuplimentare where idstatsalariat = (select id from realizariretineri where luna = ?1 and an = ?2 and idcontract = ?3)", 
	nativeQuery = true)
	List<Oresuplimentare> findByLunaAndAnAndIdcontract(int luna, int an, long idcontract);
	
	@Query(value = "SELECT COALESCE( (select sum(nr) from oresuplimentare where idstatsalariat = ?1), 0)", nativeQuery = true)
	Integer countNrOreSuplimentareByIdstat(long idstat);
}
