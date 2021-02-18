package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Caiet;

@Repository
public interface CaietRepository extends JpaRepository<Caiet, Integer> {
	public List<Caiet> findBySocietate_Id(int idsocietate);
	public Caiet findBySerie(String serie);
}
