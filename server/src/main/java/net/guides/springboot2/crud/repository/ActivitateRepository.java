package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Activitate;

@Repository
public interface ActivitateRepository extends JpaRepository<Activitate, Integer> {
	public List<Activitate> findBySocietate_Id(int id);
}