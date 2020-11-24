package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
	@Query(value = "SELECT f FROM Factura f WHERE f.societate.id = ?1")
	List<Factura> findFacturiByIdsocietate(int idsocietate);
}
