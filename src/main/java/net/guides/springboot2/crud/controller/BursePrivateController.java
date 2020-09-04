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
import net.guides.springboot2.crud.model.BursePrivate;
import net.guides.springboot2.crud.repository.BursePrivateRepository;

@RestController
@RequestMapping("/api/v1")
public class BursePrivateController {
    @Autowired
    private BursePrivateRepository bursePrivateRepository;

    @GetMapping("/burseprivate")
    public List<BursePrivate> getAllBursePrivates() {
        return bursePrivateRepository.findAll();
    }

    @GetMapping("/burseprivate/{id}")
    public ResponseEntity<BursePrivate> getBursePrivateById(@PathVariable(value = "id") Long bursePrivateId)
            throws ResourceNotFoundException {
        BursePrivate bursePrivate = bursePrivateRepository.findById(bursePrivateId)
                .orElseThrow(() -> new ResourceNotFoundException("BursePrivate not found for this id :: " + bursePrivateId));
        return ResponseEntity.ok().body(bursePrivate);
    }

    @PostMapping("/burseprivate")
    public BursePrivate createBursePrivate(@RequestBody BursePrivate bursePrivate) {
        return bursePrivateRepository.save(bursePrivate);
    }

    @PutMapping("/burseprivate/{id}")
    public ResponseEntity<BursePrivate> updateBursePrivate(@PathVariable(value = "id") Long bursePrivateId,
                                                           @RequestBody BursePrivate bursePrivateDetails) throws ResourceNotFoundException {
        BursePrivate bursePrivate = bursePrivateRepository.findById(bursePrivateId)
                .orElseThrow(() -> new ResourceNotFoundException("BursePrivate not found for this id :: " + bursePrivateId));

        bursePrivateDetails.setId(bursePrivate.getId());
        final BursePrivate updatedBursePrivate = bursePrivateRepository.save(bursePrivate);
        return ResponseEntity.ok(updatedBursePrivate);
    }

    @DeleteMapping("/burseprivate/{id}")
    public Map<String, Boolean> deleteBursePrivate(@PathVariable(value = "id") Long bursePrivateId)
            throws ResourceNotFoundException {
        BursePrivate bursePrivate = bursePrivateRepository.findById(bursePrivateId)
                .orElseThrow(() -> new ResourceNotFoundException("BursePrivate not found for this id :: " + bursePrivateId));

        bursePrivateRepository.delete(bursePrivate);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
