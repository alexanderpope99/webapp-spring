package net.guides.springboot2.crud.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Bazacalcul;

@Repository
public interface BazacalculRepository extends JpaRepository<Bazacalcul, Long>{
	public Bazacalcul findByLunaAndAnAndIdangajat(int luna, int an, long idangajat);

	public boolean existsByLunaAndAnAndIdangajat(int luna, int an, long idangajat);
}