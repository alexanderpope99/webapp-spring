package net.guides.springboot2.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Echipa;

@Repository
public interface EchipaRepository extends JpaRepository<Echipa, Integer> {

}