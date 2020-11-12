package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.CO;

@Repository
public interface CORepository extends JpaRepository<CO, Integer>{

	List<CO> findByContract_Id(int idcontract);
	
  List<CO> findByContract_IdOrderByDelaDesc(int idcontract);

	List<CO> findByContract_IdAndTip(int idcontract, String tip);

	List<CO> findByContract_IdAndTipOrderByDelaDesc(int idcontract, String tip);

	List<CO> findAllByOrderByDelaAsc();
}