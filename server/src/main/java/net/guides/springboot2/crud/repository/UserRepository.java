package net.guides.springboot2.crud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.dto.RoleDTO;
import net.guides.springboot2.crud.dto.SocietateDTO;
import net.guides.springboot2.crud.dto.PersoanaDTO;
import net.guides.springboot2.crud.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "select roles.id as \"id\",roles.name as \"name\" from users LEFT JOIN user_roles on user_roles.user_id=users.id LEFT JOIN roles on user_roles.role_id=roles.id where users.id=?1", nativeQuery = true)
	List<RoleDTO> getRolesByUserId(Long id);

	@Query(value = "select societate.id as \"id\",societate.nume as \"nume\" from users LEFT JOIN user_societati on user_societati.user_id=users.id LEFT JOIN societate on user_societati.societate_id=societate.id where users.id=?1", nativeQuery = true)
	List<SocietateDTO> getSocietiesByUserId(Long id);

	@Query(value = "SELECT persoana.id as \"id\",persoana.nume as \"nume\",persoana.prenume as \"prenume\"  FROM users LEFT JOIN persoana on users.id_angajat=persoana.id where users.id=?1", nativeQuery = true)
	List<PersoanaDTO> getPersoanaByUserId(Long id);

	@Query(value = "SELECT societate.id as \"id\",societate.nume as \"nume\" FROM users LEFT JOIN angajat ON angajat.idpersoana=users.id_angajat LEFT JOIN societate on angajat.idsocietate=societate.id where users.id=?1", nativeQuery = true)
	List<SocietateDTO> getSocietateByUserId(Long id);

	@Query(value = "SELECT persoana.id as \"id\",persoana.nume as \"nume\",persoana.prenume as \"prenume\" FROM users LEFT JOIN angajat ON angajat.idpersoana=users.id_angajat LEFT JOIN persoana on angajat.idsuperior=persoana.id where users.id=?1", nativeQuery = true)
	List<PersoanaDTO> getSuperiorByUserId(Long id);

	@Query(value = "select * from users where username = ?1", nativeQuery = true)
	Optional<User> findByUsername(String username);

	@Query(value = "select exists(select * from users where username = ?1)", nativeQuery = true)
	Boolean existsByUsername(String username);

	@Query(value = "select exists(select * from users where email = ?1)", nativeQuery = true)
	Boolean existsByEmail(String email);
}
