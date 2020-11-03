package net.guides.springboot2.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Deduceri;

@Repository
public interface DeduceriRepository extends JpaRepository<Deduceri, Integer> {
	@Query(value = "select ?2 from deduceri where dela <= ?1 and ?1 <= panala", nativeQuery = true)
	public Deduceri getDeducereBySalariuAndNrPersoane(float salariu, String strNrPersoaneIntretinere);

	@Query(value = "select * from deduceri where dela <= ?1 and ?1 <= panala", nativeQuery = true)
	public Deduceri getDeducereBySalariu(float salariu);
}
