package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Societate;

@Repository
public interface SocietateRepository extends JpaRepository<Societate, Integer> {
	@Query(value = "SELECT * FROM societate INNER JOIN user_societati on user_societati.societate_id=societate.id where user_societati.user_id=?1 ORDER BY nume ASC", nativeQuery = true)
	List<Societate> findByUserId(Integer id);

	List<Societate> findAllByOrderByNumeAsc();
}
