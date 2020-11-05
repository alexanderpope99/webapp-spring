package net.guides.springboot2.crud.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.ERole;
import net.guides.springboot2.crud.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	@Query(value = "select * from role where name = ?1", nativeQuery = true)
	Optional<Role> findByName(ERole name);
}
