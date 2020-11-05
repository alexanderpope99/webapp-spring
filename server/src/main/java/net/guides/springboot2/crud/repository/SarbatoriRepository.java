package net.guides.springboot2.crud.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Sarbatori;

@Repository
public interface SarbatoriRepository extends JpaRepository<Sarbatori, Long> {

	@Query(value = "select * from sarbatori where dela between ?1 and ?2", nativeQuery = true)
	public List<Sarbatori> findByDelaBetween(LocalDate dela, LocalDate panala);

	@Query(value = "select * from sarbatori order by dela desc", nativeQuery = true)
	public List<Sarbatori> findAllByOrderByDelaDesc();
	@Query(value = "select * from sarbatori order by dela asc", nativeQuery = true)
	public List<Sarbatori> findAllByOrderByDelaAsc();
}
