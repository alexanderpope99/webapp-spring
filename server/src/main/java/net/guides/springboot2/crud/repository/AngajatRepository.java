package net.guides.springboot2.crud.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Angajat;

@Repository
public interface AngajatRepository extends JpaRepository<Angajat, Integer> {
	List<Angajat> findByContract_IdNotNull();

	@Query(value = "SELECT angajat.* FROM angajat WHERE iduser IS NOT NULL AND iduser IN (SELECT user_id FROM user_societati WHERE societate_id=?1)", nativeQuery = true)
	List<Angajat> findBySocietate_IdAndContract_IdNotNullWithUserAndAccess(int idsocietate);

	List<Angajat> findBySocietate_IdAndContract_IdNotNull(int idsocietate);

	List<Angajat> findBySocietate_IdAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(int idsocietate);

	List<Angajat> findBySocietate_IdAndUser_IdAndContractNotNull(int idsocietate, int iduser);

	List<Angajat> findBySocietate_IdAndUserIsNull(int idsocietate);

	List<Angajat> findBySocietate_IdAndUserIsNullOrderByPersoana_NumeAsc(int idsocietate);

	List<Angajat> findBySocietate_Id(int idsocietate);

	List<Angajat> findBySocietate_IdOrderByPersoana_NumeAsc(int idsocietate);

	List<Angajat> findBySocietate_IdAndIdpersoanaNot(int idsocietate, int idangajat);

	Angajat findBySocietate_IdAndUser_Id(int idsocietate, int iduser);

	List<Angajat> findBySocietate_IdAndIdpersoanaNotAndIdpersoanaNotIn(int idsocietate, int idangajat,
			List<Integer> subalterni);

	List<Angajat> findBySocietate_IdAndContractNotNullAndContract_UltimaZiLucruBeforeOrderByPersoana_NumePersoana_Prenume(int idsocietate, LocalDate primaZiUrmatoareaLuna);

	int countBySocietate_Id(int idsocietate);

	Angajat findByContract_Id(int idcontract);

	@Query(value = "SELECT a.contract.id FROM Angajat a WHERE a.idpersoana = ?1")
	int findIdcontractByIdpersoana(int idangajat);

	@Query(value = "SELECT a.idpersoana FROM Angajat a WHERE a.contract.id = ?1")
	int findIdpersoanaByIdcontract(int idcontract);

	@Query(value = "SELECT id_angajat FROM users WHERE users.id = ?1", nativeQuery = true)
	int findPersoanaIdByUserId(long userid);
}