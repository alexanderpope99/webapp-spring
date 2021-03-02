package net.guides.springboot2.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Suspendare;

@Repository
public interface SuspendareRepository extends JpaRepository<Suspendare, Integer> {
	List<Suspendare> findByContract_IdOrderByDelaAsc(int id);
}