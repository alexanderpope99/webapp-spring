package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Bazacalcul;

@Repository
public interface BazacalculRepository extends JpaRepository<Bazacalcul, Integer> {
	public List<Bazacalcul> findByIdangajatOrderByAnDescLunaDesc(int idangajat);

	public Bazacalcul findByLunaAndAnAndIdangajat(int luna, int an, int idangajat);

	public boolean existsByLunaAndAnAndIdangajat(int luna, int an, int idangajat);
}