package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.ZileCOAn;

@Repository
public interface ZileCOAnRepository extends JpaRepository<ZileCOAn, Integer> {
	
	List<ZileCOAn> findByAngajat_IdpersoanaOrderByAnDesc(int ida);
}
