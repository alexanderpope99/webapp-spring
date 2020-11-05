package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.CO;

@Repository
public interface CORepository extends JpaRepository<CO, Integer>{

	@Query(value = "select * from co where idcontract = ?1", nativeQuery = true)
  List<CO> findByIdcontract(int idcontract);
	@Query(value = "select * from co where idcontract = ?1 order by dela desc", nativeQuery = true)
  List<CO> findByIdcontractOrderByDelaDesc(int idcontract);

	@Query(value = "select * from co where idcontract = ?1 and tip = ?2", nativeQuery = true)
	List<CO> findByIdcontractAndTip(int idcontract, String tip);
	@Query(value = "select * from co where idcontract = ?1 and tip = ?2 order by dela desc", nativeQuery = true)
	List<CO> findByIdcontractAndTipOrderByDelaDesc(int idcontract, String tip);

	@Query(value = "select * from co order by dela desc", nativeQuery = true)
	List<CO> findAllByOrderByDelaAsc();
}