package net.guides.springboot2.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Adresa;

@Repository
public interface AdresaRepository extends JpaRepository<Adresa, Long>{

	@Query(value = "select * from adresa where id = (select idadresa from persoana where id = ?1)", nativeQuery = true)
	public Adresa findByIdpersoana(long idpersoana);
}
