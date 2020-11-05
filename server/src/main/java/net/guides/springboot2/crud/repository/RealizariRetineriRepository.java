package net.guides.springboot2.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.dto.NotaContabilaDTO;
import net.guides.springboot2.crud.model.RealizariRetineri;

@Repository
public interface RealizariRetineriRepository extends JpaRepository<RealizariRetineri, Integer> {
	@Query(value = "select * from realizariretineri where luna = ?1 and an = ?2 and idcontract = ?3", nativeQuery = true)
	public RealizariRetineri findByLunaAndAnAndIdcontract(int luna, int an, int idcontract);

	@Query(value = "select sum(salariurealizat) from realizariretineri where luna = ?1 and an = ?2 and idcontract in (select id from contract where id in (select idcontract from angajat where idsocietate = ?3))", nativeQuery = true)
	public long getSalDatoratByLunaAndAn(int luna, int an, int idsocietate);

	@Query(value = "select sum(cas) from realizariretineri where luna = ?1 and an = ?2 and idcontract in (select id from contract where id in (select idcontract from angajat where idsocietate = ?3))", nativeQuery = true)
	public long getCAS25ByLunaAndAn(int luna, int an, int idsocietate);

	@Query(value = "select sum(cass) from realizariretineri where luna = ?1 and an = ?2 and idcontract in (select id from contract where id in (select idcontract from angajat where idsocietate = ?3))", nativeQuery = true)
	public long getCASS10ByLunaAndAn(int luna, int an, int idsocietate);

	@Query(value = "select sum(impozit) from realizariretineri where luna = ?1 and an = ?2 and idcontract in (select id from contract where id in (select idcontract from angajat where idsocietate = ?3))", nativeQuery = true)
	public long getImpozitByLunaAndAn(int luna, int an, int idsocietate);

	@Query(value = "select sum(valcm) from realizariretineri where luna = ?1 and an = ?2 and idcontract in (select id from contract where id in (select idcontract from angajat where idsocietate = ?3))", nativeQuery = true)
	public long getValCMByLunaAndAn(int luna, int an, int idsocietate);

	@Query(value = "select sum(cam) from realizariretineri where luna = ?1 and an = ?2 and idcontract in (select id from contract where id in (select idcontract from angajat where idsocietate = ?3))", nativeQuery = true)
	public long getCAMByLunaAndAn(int luna, int an, int idsocietate);

	@Query(value = "select sum(cas) as cas25, sum(cass) as cass10, sum(impozit) as impozit, sum(salariurealizat) as salariuDatorat, sum(valcm) as valCM, sum(cam) as cam from realizariretineri where luna = ?1 and an = ?2 and idcontract in (select id from contract where id in (select idcontract from angajat where idsocietate = ?3))", nativeQuery = true)
	public NotaContabilaDTO getNotaContabilaByLunaAndAnAndIdsocietate(int luna, int an, int idsocietate);

	@Query(value = "select exists (select id from realizariretineri where luna = ?1 and an = ?2 and idcontract = ?3)", nativeQuery = true)
	public boolean existsByLunaAndAnAndIdcontract(int luna, int an, int idcontract);
}
