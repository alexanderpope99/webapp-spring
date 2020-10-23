package net.guides.springboot2.crud.repository;

// import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.RoleToPermission;

@Repository
public interface RoleToPermissionRepository extends JpaRepository<RoleToPermission, Long>{ 
    Optional<RoleToPermission> findByRoleidAndPermissionid(Long roleid, Long permissionid);
}
