package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.FacturaOld;

@Repository
public interface FacturaOldRepository extends JpaRepository<FacturaOld, Integer> {
	List<FacturaOld> findBySocietate_Id(int idsocietate);

	List<FacturaOld> findBySocietate_IdAndStatus(int idsocietate, String status);

	List<FacturaOld> findBySocietate_IdAndAprobator_User_Id(int idsocietate, int userID);
}
