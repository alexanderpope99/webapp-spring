package net.guides.springboot2.crud.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Persoana;

@Repository
public interface PersoanaRepository extends JpaRepository<Persoana, Integer> {
	@Query(value = "select * from persoana where id in (select idpersoana from angajat where idsocietate = ?1) order by nume, prenume", nativeQuery = true)
	List<Persoana> getPersoanaByIdsocietateNoContract(int idsocietate);

	@Query(value = "select * from persoana where id in (select idpersoana from angajat where idcontract is not null and idsocietate = ?1) order by nume, prenume", nativeQuery = true)
	List<Persoana> getPersoanaByIdsocietateWithContract(int idsocietate);

	@Query(value = "select nume, cnp from persoana where id in (select idpersoana from angajat where idcontract is nou null and id societate = ?1) order by nume", nativeQuery = true)
	Map<String, String> getNumeCnpPersoanaByIdsocietate(int idsocietate);
}
