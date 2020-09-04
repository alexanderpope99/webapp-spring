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
import net.guides.springboot2.crud.model.CentruCost;
import net.guides.springboot2.crud.repository.CentruCostRepository;

@RestController
@RequestMapping("/centrucost")
public class CentruCostController {
    @Autowired
    private CentruCostRepository centruCostRepository;

    @GetMapping
    public List<CentruCost> getAllCentruCosts() {
        return centruCostRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<CentruCost> getCentruCostById(@PathVariable(value = "id") Long centruCostId)
            throws ResourceNotFoundException {
        CentruCost centruCost = centruCostRepository.findById(centruCostId)
                .orElseThrow(() -> new ResourceNotFoundException("CentruCost not found for this id :: " + centruCostId));
        return ResponseEntity.ok().body(centruCost);
    }

    @PostMapping
    public CentruCost createCentruCost(@RequestBody CentruCost centruCost) {
        return centruCostRepository.save(centruCost);
    }

    @PutMapping("{id}")
    public ResponseEntity<CentruCost> updateCentruCost(@PathVariable(value = "id") Long centruCostId,
                                                       @RequestBody CentruCost centruCostDetails) throws ResourceNotFoundException {
        CentruCost centruCost = centruCostRepository.findById(centruCostId)
                .orElseThrow(() -> new ResourceNotFoundException("CentruCost not found for this id :: " + centruCostId));

        centruCostDetails.setId(centruCost.getId());
        final CentruCost updatedCentruCost = centruCostRepository.save(centruCost);
        return ResponseEntity.ok(updatedCentruCost);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteCentruCost(@PathVariable(value = "id") Long centruCostId)
            throws ResourceNotFoundException {
        CentruCost centruCost = centruCostRepository.findById(centruCostId)
                .orElseThrow(() -> new ResourceNotFoundException("CentruCost not found for this id :: " + centruCostId));

        centruCostRepository.delete(centruCost);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
