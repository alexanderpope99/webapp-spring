package net.guides.springboot2.crud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.LunaInchisa;

@Repository
public interface LunaInchisaRepository extends JpaRepository<LunaInchisa, Integer> {
	public List<LunaInchisa> findByAn(int an);	

	public List<LunaInchisa> findBySocietate_Id(int id);

	public Optional<LunaInchisa> findByLunaAndAnAndSocietate_Id(int luna, int an, int idsocietate);
}
