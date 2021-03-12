package net.guides.springboot2.crud.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.CO;

@Repository
public interface CORepository extends JpaRepository<CO, Integer> {

	public List<CO> findByContract_Id(int idcontract);

	public List<CO> findByContract_IdOrderByDelaDesc(int idcontract);

	public List<CO> findByContract_IdAndTip(int idcontract, String tip);

	@Query(value = "SELECT c FROM CO c WHERE c.contract.id = ?1 AND c.tip = ?2 AND YEAR(c.dela) = ?3")
	public List<CO> findByContract_IdAndTipAndYear(int idcontract, String tip, int an);

	public List<CO> findByContract_IdAndTipOrderByDelaDesc(int idcontract, String tip);

	public CO findByTipAndDelaAndPanalaAndContract_Id(String tip, LocalDate dela, LocalDate panala, int idcontract);

	public List<CO> findByContract_Angajat_Societate_Id(int idsocietate);

	public List<CO> findAllByOrderByDelaAsc();

	@Query(value = "SELECT c FROM CO c WHERE YEAR(c.dela) = ?1 AND MONTH(c.dela) = ?2")
	public List<CO> findByAnAndLuna(int an, int luna);

	@Query(value = "SELECT c FROM CO c WHERE YEAR(c.dela) = ?1")
	public List<CO> findByAn(int an);
}