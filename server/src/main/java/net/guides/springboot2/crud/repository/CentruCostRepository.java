package net.guides.springboot2.crud.repository;

import net.guides.springboot2.crud.model.CentruCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CentruCostRepository extends JpaRepository<CentruCost, Long>{

}