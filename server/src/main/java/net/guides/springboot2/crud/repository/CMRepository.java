package net.guides.springboot2.crud.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.CM;

@Repository
public interface CMRepository extends JpaRepository<CM, Integer> {
	@Query(value = "select * from cm where idcontract = ?1", nativeQuery = true)
	List<CM> findByIdcontract(int idcontract);

	@Query(value = "select * from cm where idcontract = ?1 and dela between ?2 and ?3", nativeQuery = true)
	List<CM> findByIdcontractAndDelaBetween(int idcontract, LocalDate inceputLuna, LocalDate sfarsitLuna);
}