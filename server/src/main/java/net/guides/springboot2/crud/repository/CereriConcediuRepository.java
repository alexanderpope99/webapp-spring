package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.CereriConcediu;

@Repository
public interface CereriConcediuRepository extends JpaRepository<CereriConcediu, Integer> {
	List<CereriConcediu> findByUser_IdAndSocietate_Id(int usrId, int socId);

	List<CereriConcediu> findBySocietate_Id(int socId);

}
