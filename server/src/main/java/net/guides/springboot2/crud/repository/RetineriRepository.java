package net.guides.springboot2.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Retineri;

@Repository
public interface RetineriRepository extends JpaRepository<Retineri, Integer> {
	public Retineri findByStat_Id(int stat);

	public Retineri findByStat_Contract_IdAndStat_LunaAndStat_An(int idcontract, int luna, int an);

	@Query(value = "select sum(avansnet) from retineri where idstat in (select id from realizariretineri where luna = ?1 and an = ?2 and idcontract in (select id from contract where id in (select idcontract from angajat where idsocietate = ?3)))", nativeQuery = true)
	public long getAvansByLunaAndAnByIdsocietate(int luna, int an, int idsocietate);
}
