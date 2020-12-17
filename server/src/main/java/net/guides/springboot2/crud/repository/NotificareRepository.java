package net.guides.springboot2.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.crud.model.Notificare;

@Repository
public interface NotificareRepository extends JpaRepository<Notificare, Integer> {
	List<Notificare> findByUser_Id(int userId);
}