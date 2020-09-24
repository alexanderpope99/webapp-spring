package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.CM;

@Repository
public interface CMRepository extends JpaRepository<CM, Long>{
  List<CM> findByIdcontract(Long idcontract);

//   @Query(value = "", nativeQuery = true)
//   int getZileFNUASSInLunaAnul(int luna, int an);
}