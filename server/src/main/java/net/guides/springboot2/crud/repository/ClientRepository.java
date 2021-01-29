package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
  public List<Client> findBySocietate_Id(int idsocietate);
}