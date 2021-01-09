package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.dto.NotaContabilaDTO;
import net.guides.springboot2.crud.model.RealizariRetineri;

@Repository
public interface RealizariRetineriRepository extends JpaRepository<RealizariRetineri, Integer> {
	public RealizariRetineri findByLunaAndAnAndContract_Id(int luna, int an, int idcontract);

	@Query(value = "select sum(cas) as cas25, sum(cass) as cass10, sum(impozit) as impozit, sum(salariurealizat) as salariuDatorat, sum(valcm) as valCM, sum(cam) as cam from realizariretineri where luna = ?1 and an = ?2 and idcontract in (select id from contract where id in (select idcontract from angajat where idsocietate = ?3))", nativeQuery = true)
	public NotaContabilaDTO getNotaContabilaByLunaAndAnAndIdsocietate(int luna, int an, int idsocietate);

	public boolean existsByLunaAndAnAndContract_Id(int luna, int an, int idcontract);

	public List<RealizariRetineri> findByContract_Id(int idcontract);
}
