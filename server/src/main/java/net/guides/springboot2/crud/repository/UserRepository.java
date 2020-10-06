package net.guides.springboot2.crud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "select roles.name from users LEFT JOIN user_roles on user_roles.user_id=users.id LEFT JOIN roles on user_roles.role_id=roles.id where users.id=?1", nativeQuery = true)
	List<String> getRolesByUserId(long id);

	@Query(value = "select societate.nume from users LEFT JOIN user_societati on user_societati.user_id=users.id LEFT JOIN societate on user_societati.societate_id=societate.id where users.id=?1", nativeQuery = true)
	List<String> getSocietiesByUserId(long id);

	@Query(value = "SELECT persoana.nume || ' ' || persoana.prenume as \"numeprenume\" FROM users LEFT JOIN persoana on users.id_angajat=persoana.id where users.id=?1", nativeQuery = true)
	List<String> getNumePrenumeByUserId(long id);

	@Query(value = "SELECT societate.nume FROM users LEFT JOIN angajat ON angajat.idpersoana=users.id_angajat LEFT JOIN societate on angajat.idsocietate=societate.id where users.id=?1", nativeQuery = true)
	List<String> getSocietateByUserId(long id);

	@Query(value = "SELECT persoana.nume || ' ' || persoana.prenume as \"superior\" FROM users LEFT JOIN angajat ON angajat.idpersoana=users.id_angajat LEFT JOIN persoana on angajat.idsuperior=persoana.id where users.id=?1", nativeQuery = true)
	List<String> getSuperiorByUserId(long id);

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
