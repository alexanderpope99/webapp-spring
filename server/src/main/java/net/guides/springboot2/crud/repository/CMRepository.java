package net.guides.springboot2.crud.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.CM;

@Repository
public interface CMRepository extends JpaRepository<CM, Long>{
  List<CM> findByIdcontract(Long idcontract);

  List<CM> findByIdcontractAndDelaBetween(long idcontract, LocalDate inceputLuna, LocalDate sfarsitLuna);
}