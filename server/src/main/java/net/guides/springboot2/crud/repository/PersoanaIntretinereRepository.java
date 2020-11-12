package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.PersoanaIntretinere;

@Repository
public interface PersoanaIntretinereRepository extends JpaRepository<PersoanaIntretinere, Integer> {
	@Query(value = "select count(*) from persoanaintretinere where idangajat = (select idpersoana from angajat where idcontract = ?1)", nativeQuery = true)
	public int getNrPersoaneIntretinereByIdcontract(int idcontract);

	@Query(value = "SELECT a.persoaneintretinere FROM Angajat a WHERE a.contract.id = ?1")
	public List<PersoanaIntretinere> findByIdcontract(int idcontract);

	public List<PersoanaIntretinere> findByAngajat_IdpersoanaOrderByNumeAscPrenumeAsc(int idangajat);
}
