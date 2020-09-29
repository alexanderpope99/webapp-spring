package net.guides.springboot2.crud.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.ParametriiSalariu;

@Repository
public interface ParametriiSalariuRepository extends JpaRepository<ParametriiSalariu, Long> {
	@Query(value = "select * from parametriisalariu where parametriisalariu.date<=?1 ORDER BY date desc LIMIT 1", nativeQuery = true)
	ParametriiSalariu findByDate(LocalDate localDate);
}
