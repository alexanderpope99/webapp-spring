package net.guides.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Spor;
import net.guides.springboot2.crud.repository.SporRepository;


@RestController
@RequestMapping("/spor")
public class SporController {
    @Autowired
    private SporRepository sporRepository;

    @GetMapping
    public List<Spor> getAllPersoane() {
        return sporRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Spor> getSporById(@PathVariable(value="id") Long id) throws ResourceNotFoundException
    {
        Spor spor = sporRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Spor not found for this id :: " + id));

        return ResponseEntity.ok().body(spor);
    }

    @PostMapping
        public Spor createSpor(@RequestBody Spor spor) {
        return sporRepository.save(spor);
    }

    @PutMapping("{id}")
    public ResponseEntity<Spor> updateSpor(@PathVariable(value = "id") Long id,  @RequestBody Spor newSpor) throws ResourceNotFoundException {
        Spor spor = sporRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Spor not found for this id :: " + id));

        newSpor.setId(spor.getId());
        final Spor updatedSpor = sporRepository.save(newSpor);
        return ResponseEntity.ok(updatedSpor);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteSpor(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Spor spor = sporRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spor not found for this id :: " + id));

        sporRepository.delete(spor);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
