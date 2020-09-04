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
import net.guides.springboot2.crud.model.Caen;
import net.guides.springboot2.crud.repository.CaenRepository;

@RestController
@RequestMapping("/caen")
public class CaenController {
    @Autowired
    private CaenRepository caenRepository;

    @GetMapping
    public List<Caen> getAllCaens() {
        return caenRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Caen> getCaenById(@PathVariable(value = "id") Long caenId)
            throws ResourceNotFoundException {
        Caen caen = caenRepository.findById(caenId)
                .orElseThrow(() -> new ResourceNotFoundException("Caen not found for this id :: " + caenId));
        return ResponseEntity.ok().body(caen);
    }

    @PostMapping
    public Caen createCaen(@RequestBody Caen caen) {
        return caenRepository.save(caen);
    }

    @PutMapping("{id}")
    public ResponseEntity<Caen> updateCaen(@PathVariable(value = "id") Long caenId,
                                           @RequestBody Caen caenDetails) throws ResourceNotFoundException {
        Caen caen = caenRepository.findById(caenId)
                .orElseThrow(() -> new ResourceNotFoundException("Caen not found for this id :: " + caenId));

        caenDetails.setId(caen.getId());
        final Caen updatedCaen = caenRepository.save(caen);
        return ResponseEntity.ok(updatedCaen);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteCaen(@PathVariable(value = "id") Long caenId)
            throws ResourceNotFoundException {
        Caen caen = caenRepository.findById(caenId)
                .orElseThrow(() -> new ResourceNotFoundException("Caen not found for this id :: " + caenId));

        caenRepository.delete(caen);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
