package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Persoana;

@Repository
public interface PersoanaRepository extends JpaRepository<Persoana, Long> {
  @Query(value = "select * from persoana where id in (select idpersoana from angajat where idsocietate = ?1) order by nume, prenume", nativeQuery = true)
  List<Persoana> getPersoanaByIdsocietateNoContract(long idsocietate);

  @Query(value = "select * from persoana where id in (select idpersoana from angajat where idcontract is not null and idsocietate = ?1) order by nume, prenume", nativeQuery = true)
  List<Persoana> getPersoanaByIdsocietateWithContract(long idsocietate);
}
