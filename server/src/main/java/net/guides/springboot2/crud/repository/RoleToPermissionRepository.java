package net.guides.springboot2.crud.repository;

// import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.RoleToPermission;

@Repository
public interface RoleToPermissionRepository extends JpaRepository<RoleToPermission, Long>{ 
		// @Query(value = "select * from roletopermission where roleid = ?1 and permissionid = ?2", nativeQuery = true)
    Optional<RoleToPermission> findByRoleidAndPermissionid(Long roleid, Long permissionid);
}
