package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.ContBancar;

@Repository
public interface ContBancarRepository extends JpaRepository<ContBancar, Integer>{
	public List<ContBancar> findBySocietate_Id(int ids);
}