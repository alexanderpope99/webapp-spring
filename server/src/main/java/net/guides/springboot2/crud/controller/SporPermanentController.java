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
import net.guides.springboot2.crud.model.SporPermanent;
import net.guides.springboot2.crud.repository.SporPermanentRepository;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/sporpermanent")
public class SporPermanentController {
    @Autowired
    private SporPermanentRepository sporPermanentRepository;

    @GetMapping
    public List<SporPermanent> getAllPersoane() {
        return sporPermanentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @GetMapping("{id}")
    public ResponseEntity<SporPermanent> getSporPermanentById(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        SporPermanent sporPermanent = sporPermanentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SporPermanent not found for this id :: " + id));

        return ResponseEntity.ok().body(sporPermanent);
    }

    @PostMapping
    public SporPermanent createSporPermanent(@RequestBody SporPermanent sporPermanent) {
        return sporPermanentRepository.save(sporPermanent);
    }

    @PutMapping("{id}")
    public ResponseEntity<SporPermanent> updateSporPermanent(@PathVariable(value = "id") Long id,
            @RequestBody SporPermanent newSporPermanent) throws ResourceNotFoundException {
        SporPermanent sporPermanent = sporPermanentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SporPermanent not found for this id :: " + id));

        newSporPermanent.setId(sporPermanent.getId());
        final SporPermanent updatedSporPermanent = sporPermanentRepository.save(newSporPermanent);
        return ResponseEntity.ok(updatedSporPermanent);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteSporPermanent(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        SporPermanent sporPermanent = sporPermanentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SporPermanent not found for this id :: " + id));

        sporPermanentRepository.delete(sporPermanent);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
