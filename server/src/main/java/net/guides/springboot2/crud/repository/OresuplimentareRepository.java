package net.guides.springboot2.crud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Oresuplimentare;

@Repository
public interface OresuplimentareRepository extends JpaRepository<Oresuplimentare, Long>{
    Optional<List<Oresuplimentare>> findByIdstatsalariat(long idstat);
}
