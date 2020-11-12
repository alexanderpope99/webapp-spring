package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Bazacalcul;

@Repository
public interface BazacalculRepository extends JpaRepository<Bazacalcul, Integer> {

	public List<Bazacalcul> findByAngajat_IdpersoanaOrderByAnDescLunaDesc(int idangajat);

	public Bazacalcul findByLunaAndAnAndAngajat_Idpersoana(int luna, int an, int idangajat);

	public boolean existsByLunaAndAnAndAngajat_Idpersoana(int luna, int an, int idangajat);
}