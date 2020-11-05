package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.PersoanaIntretinere;

@Repository
public interface PersoanaIntretinereRepository extends JpaRepository<PersoanaIntretinere, Integer> {
	@Query(value = "select count(id) from persoanaintretinere where idangajat = ?1", nativeQuery = true)
	public int getNrPersoaneIntretinereByIdangajat(int idangajat);

	@Query(value = "select count(*) from persoanaintretinere where idangajat = (select idpersoana from angajat where idcontract = ?1)", nativeQuery = true)
	public int getNrPersoaneIntretinereByIdcontract(int idcontract);

	@Query(value = "select * from persoanaintretinere where idangajat = ?1", nativeQuery = true)
	public List<PersoanaIntretinere> findByIdangajat(int idangajat);

	@Query(value = "select * from persoanaintretinere where idangajat = ?1 order by nume asc, prenume asc", nativeQuery = true)
	public List<PersoanaIntretinere> findByIdangajatOrderByNumeAscPrenumeAsc(int idangajat);
}
