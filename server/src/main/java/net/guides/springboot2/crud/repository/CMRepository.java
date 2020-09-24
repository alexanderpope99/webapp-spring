package net.guides.springboot2.crud.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.CM;

@Repository
public interface CMRepository extends JpaRepository<CM, Long>{
  List<CM> findByIdcontract(Long idcontract);

  @Query(value = "select * from cm where ?1 <= dela and ?2 >= panala and icontract = ?3", 
  		 nativeQuery = true)
  List<CM> findInLunaAnulByIdcontract(LocalDate inceputLuna, LocalDate sfarsitLuna, long idcontract);
}