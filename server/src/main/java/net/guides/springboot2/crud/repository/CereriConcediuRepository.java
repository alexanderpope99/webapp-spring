package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.CereriConcediu;

@Repository
public interface CereriConcediuRepository extends JpaRepository<CereriConcediu, Long> {
	@Query(value = "SELECT * from cerericoncediu INNER JOIN users on users.id=?1 where users.id_angajat=cerericoncediu.pentru and cerericoncediu.societate=?2 ORDER by dela asc", nativeQuery = true)
	List<CereriConcediu> findCerereConcediuByUserIdAndSocietyId(long usrId, long socId);

	@Query(value = "SELECT * from cerericoncediu INNER JOIN angajat on angajat.idpersoana=cerericoncediu.pentru where idsuperior=(select id_angajat from users where users.id=?1) and idsocietate=?2 ORDER by dela asc", nativeQuery = true)
	List<CereriConcediu> findCerereConcediuBySuperiorIdAndSocietyId(long supId, long socId);

	@Query(value = "UPDATE cerericoncediu SET status='Aprobat' WHERE id=?1 RETURNING *", nativeQuery = true)
	CereriConcediu approveStatus(long cereriConcediuId);

	@Query(value = "UPDATE cerericoncediu SET status='Respins' WHERE id=?1 RETURNING *", nativeQuery = true)
	CereriConcediu rejectStatus(long cereriConcediuId);
}
