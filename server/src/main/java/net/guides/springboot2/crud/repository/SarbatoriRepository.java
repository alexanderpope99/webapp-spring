package net.guides.springboot2.crud.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Sarbatori;

@Repository
public interface SarbatoriRepository extends JpaRepository<Sarbatori, Long> {

	public List<Sarbatori> findByDelaBetween(LocalDate dela, LocalDate panala);

	public List<Sarbatori> findAllByOrderByDelaDesc();
	public List<Sarbatori> findAllByOrderByDelaAsc();
}
