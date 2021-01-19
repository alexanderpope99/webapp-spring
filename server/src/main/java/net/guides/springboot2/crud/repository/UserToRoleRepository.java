package net.guides.springboot2.crud.repository;

// import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.UserToRole;

@Repository
public interface UserToRoleRepository extends JpaRepository<UserToRole, Long>{ 
		@Query(value = "select * from usertorole where userid = ?1 and roleid = ?2", nativeQuery = true)
    Optional<UserToRole> findByUseridAndRoleid(Long userid, Long roleid);
}
