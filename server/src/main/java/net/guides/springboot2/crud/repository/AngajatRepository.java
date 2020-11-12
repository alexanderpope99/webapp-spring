package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Angajat;

@Repository
public interface AngajatRepository extends JpaRepository<Angajat, Integer> {
	List<Angajat> findByContract_IdNotNull();

	List<Angajat> findBySocietate_IdAndContract_IdNotNull(int idsocietate);

	int countBySocietate_Id(int idsocietate);

	Angajat findByContract_Id(int idcontract);

	@Query(value = "SELECT a.contract.id FROM Angajat a WHERE a.idpersoana = ?1")
	int findIdcontractByIdpersoana(int idangajat);

	@Query(value = "select a.idpersoana from Angajat a where a.contract.id = ?1")
	int findIdpersoanaByIdcontract(int idcontract);

	@Query(value = "SELECT id_angajat from users where users.id = ?1", nativeQuery = true)
	int findPersoanaIdByUserId(long userid);
}