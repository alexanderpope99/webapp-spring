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
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.SocietateRepository;


@RestController
@RequestMapping("/societate")
public class SocietateController {
    @Autowired
    private SocietateRepository societateRepository;

    @GetMapping
    public List<Societate> getAllPersoane() {
        return societateRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Societate> getSocietateById(@PathVariable(value="id") Long id) throws ResourceNotFoundException
    {
        Societate societate = societateRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + id));

        return ResponseEntity.ok().body(societate);
    }

    @PostMapping
        public Societate createSocietate(@RequestBody Societate societate) {
        return societateRepository.save(societate);
    }

    @PutMapping("{id}")
    public ResponseEntity<Societate> updateSocietate(@PathVariable(value = "id") Long id,  @RequestBody Societate newSocietate) throws ResourceNotFoundException {
        Societate societate = societateRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + id));

        newSocietate.setId(societate.getId());
        final Societate updatedSocietate = societateRepository.save(newSocietate);
        return ResponseEntity.ok(updatedSocietate);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteSocietate(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Societate societate = societateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + id));

        societateRepository.delete(societate);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
