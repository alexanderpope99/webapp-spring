package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.CO;

@Repository
public interface CORepository extends JpaRepository<CO, Integer>{

	List<CO> findByContract_Id(int idcontract);
	
  List<CO> findByContract_IdOrderByDelaDesc(int idcontract);

	List<CO> findByContract_IdAndTip(int idcontract, String tip);

	@Query(value = "SELECT c FROM CO c WHERE c.contract.id = ?1 AND c.tip = ?2 AND YEAR(c.dela) = ?3")
	List<CO> findByContract_IdAndTipAndYear(int idcontract, String tip, int an);

	List<CO> findByContract_IdAndTipOrderByDelaDesc(int idcontract, String tip);

	List<CO> findAllByOrderByDelaAsc();
}