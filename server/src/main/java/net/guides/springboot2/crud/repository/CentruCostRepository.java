package net.guides.springboot2.crud.repository;

import net.guides.springboot2.crud.model.CentruCost;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CentruCostRepository extends JpaRepository<CentruCost, Integer> {
	@Query(value = "SELECT c FROM CentruCost c WHERE c.societate.id = ?1")
	List<CentruCost> findCentreCostByIdsocietate(int idsocietate);
}