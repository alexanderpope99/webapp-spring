package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Angajat;

@Repository
public interface AngajatRepository extends JpaRepository<Angajat, Long>{
  List<Angajat> findByIdcontractNotNull();
}