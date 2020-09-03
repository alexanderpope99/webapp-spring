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
    private AdresaRepository adresaRepository;

    @GetMapping("/adresa")
    public List<Adresa> getAllAdresas() {
        return adresaRepository.findAll();
    }

    @GetMapping("/adresa/{id}")
    public ResponseEntity<Adresa> getAdresaById(@PathVariable(value = "id") Long adresaId)
            throws ResourceNotFoundException {
        Adresa adresa = adresaRepository.findById(adresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Adresa not found for this id :: " + adresaId));
        return ResponseEntity.ok().body(adresa);
    }

    @PostMapping("/adresa")
    public Adresa createAdresa(@RequestBody Adresa adresa) {
        return adresaRepository.save(adresa);
    }

    @PutMapping("/adresa/{id}")
    public ResponseEntity<Adresa> updateAdresa(@PathVariable(value = "id") Long adresaId,
                                               @RequestBody Adresa adresaDetails) throws ResourceNotFoundException {
        Adresa adresa = adresaRepository.findById(adresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Adresa not found for this id :: " + adresaId));

        adresa.setAdresa(adresaDetails.getAdresa());
        adresa.setJudet(adresaDetails.getJudet());
        adresa.setLocalitate(adresaDetails.getLocalitate());
        adresa.setTara(adresaDetails.getTara());
        final Adresa updatedAdresa = adresaRepository.save(adresa);
        return ResponseEntity.ok(updatedAdresa);
    }

    @DeleteMapping("/adresa/{id}")
    public Map<String, Boolean> deleteAdresa(@PathVariable(value = "id") Long adresaId)
            throws ResourceNotFoundException {
        Adresa adresa = adresaRepository.findById(adresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Adresa not found for this id :: " + adresaId));

        adresaRepository.delete(adresa);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
