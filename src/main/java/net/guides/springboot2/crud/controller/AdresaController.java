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
import net.guides.springboot2.crud.model.Adresa;
import net.guides.springboot2.crud.repository.AdresaRepository;

@RestController
@RequestMapping("/api/v1")
public class AdresaController {
    @Autowired
    private AdresaRepository departamentRepository;

    @GetMapping("/adresa")
    public List<Adresa> getAllAdresas() {
        return departamentRepository.findAll();
    }

    @GetMapping("/adresa/{id}")
    public ResponseEntity<Adresa> getAdresaById(@PathVariable(value = "id") Long departamentId)
            throws ResourceNotFoundException {
        Adresa departament = departamentRepository.findById(departamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Adresa not found for this id :: " + departamentId));
        return ResponseEntity.ok().body(departament);
    }

    @PostMapping("/adresa")
    public Adresa createAdresa(@RequestBody Adresa departament) {
        return departamentRepository.save(departament);
    }

    @PutMapping("/adresa/{id}")
    public ResponseEntity<Adresa> updateAdresa(@PathVariable(value = "id") Long departamentId,
                                               @RequestBody Adresa departamentDetails) throws ResourceNotFoundException {
        Adresa departament = departamentRepository.findById(departamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Adresa not found for this id :: " + departamentId));

        departamentDetails.setId(departament.getId());
        final Adresa updatedAdresa = departamentRepository.save(departament);
        return ResponseEntity.ok(updatedAdresa);
    }

    @DeleteMapping("/adresa/{id}")
    public Map<String, Boolean> deleteAdresa(@PathVariable(value = "id") Long departamentId)
            throws ResourceNotFoundException {
        Adresa departament = departamentRepository.findById(departamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Adresa not found for this id :: " + departamentId));

        departamentRepository.delete(departament);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
