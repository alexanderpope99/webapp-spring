package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.CO;

@Repository
public interface CORepository extends JpaRepository<CO, Long>{
  List<CO> findByIdcontract(Long idcontract);

	List<CO> findByIdcontractAndTip(Long idcontract, String tip);
}