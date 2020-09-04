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
import net.guides.springboot2.crud.model.AlteBeneficii;
import net.guides.springboot2.crud.repository.AlteBeneficiiRepository;

@RestController
@RequestMapping("/altebeneficii")
public class AlteBeneficiiController {
    @Autowired
    private AlteBeneficiiRepository alteBeneficiiRepository;

    @GetMapping
    public List<AlteBeneficii> getAllAlteBeneficiis() {
        return alteBeneficiiRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<AlteBeneficii> getAlteBeneficiiById(@PathVariable(value = "id") Long alteBeneficiiId)
            throws ResourceNotFoundException {
        AlteBeneficii alteBeneficii = alteBeneficiiRepository.findById(alteBeneficiiId)
                .orElseThrow(() -> new ResourceNotFoundException("AlteBeneficii not found for this id :: " + alteBeneficiiId));
        return ResponseEntity.ok().body(alteBeneficii);
    }

    @PostMapping
    public AlteBeneficii createAlteBeneficii(@RequestBody AlteBeneficii alteBeneficii) {
        return alteBeneficiiRepository.save(alteBeneficii);
    }

    @PutMapping("{id}")
    public ResponseEntity<AlteBeneficii> updateAlteBeneficii(@PathVariable(value = "id") Long alteBeneficiiId,
                                                             @RequestBody AlteBeneficii alteBeneficiiDetails) throws ResourceNotFoundException {
        AlteBeneficii alteBeneficii = alteBeneficiiRepository.findById(alteBeneficiiId)
                .orElseThrow(() -> new ResourceNotFoundException("AlteBeneficii not found for this id :: " + alteBeneficiiId));

        alteBeneficiiDetails.setId((alteBeneficii.getId()));
        final AlteBeneficii updatedAlteBeneficii = alteBeneficiiRepository.save(alteBeneficii);
        return ResponseEntity.ok(updatedAlteBeneficii);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteAlteBeneficii(@PathVariable(value = "id") Long alteBeneficiiId)
            throws ResourceNotFoundException {
        AlteBeneficii alteBeneficii = alteBeneficiiRepository.findById(alteBeneficiiId)
                .orElseThrow(() -> new ResourceNotFoundException("AlteBeneficii not found for this id :: " + alteBeneficiiId));

        alteBeneficiiRepository.delete(alteBeneficii);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
