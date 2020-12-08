package net.guides.springboot2.crud.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.ParametriiSalariu;
import net.guides.springboot2.crud.repository.ParametriiSalariuRepository;

@Service
public class ParametriiSalariuService {

	@Autowired
	private ParametriiSalariuRepository parametriiSalariuRepository;

	public ParametriiSalariu getParametriiSalariu() throws ResourceNotFoundException {
		return parametriiSalariuRepository.findByDate(LocalDate.now());
	}

	public void init() {
		parametriiSalariuRepository.save(
			new ParametriiSalariu(2230, 22350, 5429, (float)10, (float)25, (float)10, (float)2.25, (float)10, LocalDate.parse("1994-01-01"));
		);
	}

}
