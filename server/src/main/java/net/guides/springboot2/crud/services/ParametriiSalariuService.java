package net.guides.springboot2.crud.services;

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
        return parametriiSalariuRepository.findById((long)1)
          .orElseThrow(
            () -> new ResourceNotFoundException("ParametriiSalariu not found for this id :: "));
    }
}
