package net.guides.springboot2.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.SporPermanent;

@Repository
public interface SporPermanentRepository extends JpaRepository<SporPermanent, Integer> {
}
