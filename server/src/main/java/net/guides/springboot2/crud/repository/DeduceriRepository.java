package net.guides.springboot2.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Deduceri;

@Repository
public interface DeduceriRepository extends JpaRepository<Deduceri, Integer> {
	@Query(value = "SELECT ?2 FROM deduceri WHERE dela <= ?1 AND ?1 <= panala", nativeQuery = true)
	public Deduceri getDeducereBySalariuAndNrPersoane(float salariu, String strNrPersoaneIntretinere);

	@Query(value = "SELECT d FROM Deduceri d WHERE d.dela <= ?1 AND ?1 <= d.panala")
	public Deduceri getDeducereBySalariu(float salariu);
}
