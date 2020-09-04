package net.guides.springboot2.crud.repository;

// import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.UserToRole;

@Repository
public interface UserToRoleRepository extends JpaRepository<UserToRole, Long>{ 
    Optional<UserToRole> findByUseridAndRoleid(Long userid, Long roleid);
}
