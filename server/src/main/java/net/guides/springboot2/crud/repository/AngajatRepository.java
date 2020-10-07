package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Contract;

@Repository
public interface AngajatRepository extends JpaRepository<Angajat, Long> {
	List<Angajat> findByIdcontractNotNull();

	List<Angajat> findByIdsocietate(int idsocietate);

	List<Angajat> findByIdsocietateAndIdcontractNotNull(int idsocietate);

	int countByIdsocietate(int idsocietate);

	Angajat findByIdcontract(long idcontract);

	@Query(value = "select idcontract from angajat where idpersoana = ?1", nativeQuery = true)
	long findIdcontractByIdpersoana(long idangajat);

	@Query(value = "select * from angajat where idpersoana = ?1", nativeQuery = true)
	Contract findContractByIdpersoana(long idangajat);

	@Query(value = "select idpersoana from angajat where idcontract = ?1", nativeQuery = true)
	long findIdpersoanaByIdcontract(long idcontract);

	@Query(value = "select * from angajat where idcontract = ?1", nativeQuery = true)
	Angajat findPersoanaByIdcontract(long idcontract);

	@Query(value = "SELECT id_angajat from users where users.id=?1", nativeQuery = true)
	int findPersoanaIdByUserId(long userid);

	@Query(value = "SELECT persoana.nume || ' ' ||persoana.prenume from persoana inner join angajat on angajat.idpersoana=persoana.id inner join societate on angajat.idsocietate=societate.id where societate.nume=?1", nativeQuery = true)
	List<String> findPersoaneBySocietyName(String nume);
}