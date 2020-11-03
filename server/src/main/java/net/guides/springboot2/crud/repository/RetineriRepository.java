package net.guides.springboot2.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Retineri;

@Repository
public interface RetineriRepository extends JpaRepository<Retineri, Integer> {
	public Retineri findByIdstat(int idstat);

	@Query(value = "select sum(avansnet) from retineri where idstat in (select id from realizariretineri where luna = ?1 and an = ?2 and idcontract in (select id from contract where id in (select idcontract from angajat where idsocietate = ?3)))", nativeQuery = true)
	public long getAvansByLunaAndAn(int luna, int an, int idsocietate);
}
