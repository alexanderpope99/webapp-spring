package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
	public List<Factura> findByClient_Societate_IdOrderByDataexpedieriiDescNumarDesc(int idsocietate);
	
	@Query(value = "SELECT COALESCE(MAX(f.numar), 1) FROM Factura f") 
	public int findNumarFactura();
}