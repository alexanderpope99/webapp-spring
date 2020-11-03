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
import net.guides.springboot2.crud.model.AlteDrepturi;
import net.guides.springboot2.crud.repository.AlteDrepturiRepository;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/altedrepturi")
public class AlteDrepturiController {
    @Autowired
    private AlteDrepturiRepository alteDrepturiRepository;

    @GetMapping
    public List<AlteDrepturi> getAllAlteDrepturis() {
        return alteDrepturiRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @GetMapping("{id}")
    public ResponseEntity<AlteDrepturi> getAlteDrepturiById(@PathVariable(value = "id") int alteDrepturiId)
            throws ResourceNotFoundException {
        AlteDrepturi alteDrepturi = alteDrepturiRepository.findById(alteDrepturiId).orElseThrow(
                () -> new ResourceNotFoundException("AlteDrepturi not found for this id :: " + alteDrepturiId));
        return ResponseEntity.ok().body(alteDrepturi);
    }

    @PostMapping
    public AlteDrepturi createAlteDrepturi(@RequestBody AlteDrepturi alteDrepturi) {
        return alteDrepturiRepository.save(alteDrepturi);
    }

    @PutMapping("{id}")
    public ResponseEntity<AlteDrepturi> updateAlteDrepturi(@PathVariable(value = "id") int alteDrepturiId,
            @RequestBody AlteDrepturi alteDrepturiDetails) throws ResourceNotFoundException {
        AlteDrepturi alteDrepturi = alteDrepturiRepository.findById(alteDrepturiId).orElseThrow(
                () -> new ResourceNotFoundException("AlteDrepturi not found for this id :: " + alteDrepturiId));

        alteDrepturiDetails.setId(alteDrepturi.getId());
        final AlteDrepturi updatedAlteDrepturi = alteDrepturiRepository.save(alteDrepturi);
        return ResponseEntity.ok(updatedAlteDrepturi);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteAlteDrepturi(@PathVariable(value = "id") int alteDrepturiId)
            throws ResourceNotFoundException {
        AlteDrepturi alteDrepturi = alteDrepturiRepository.findById(alteDrepturiId).orElseThrow(
                () -> new ResourceNotFoundException("AlteDrepturi not found for this id :: " + alteDrepturiId));

        alteDrepturiRepository.delete(alteDrepturi);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
