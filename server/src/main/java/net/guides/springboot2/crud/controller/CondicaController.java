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
import net.guides.springboot2.crud.model.Condica;
import net.guides.springboot2.crud.repository.CondicaRepository;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/condica")
public class CondicaController {
    @Autowired
    private CondicaRepository condicaRepository;

    @GetMapping
    public List<Condica> getAllCondicas() {
        return condicaRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @GetMapping("{id}")
    public ResponseEntity<Condica> getCondicaById(@PathVariable(value = "id") Long condicaId)
            throws ResourceNotFoundException {
        Condica condica = condicaRepository.findById(condicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Condica not found for this id :: " + condicaId));
        return ResponseEntity.ok().body(condica);
    }

    @PostMapping
    public Condica createCondica(@RequestBody Condica condica) {
        return condicaRepository.save(condica);
    }

    @PutMapping("{id}")
    public ResponseEntity<Condica> updateCondica(@PathVariable(value = "id") Long condicaId,
            @RequestBody Condica condicaDetails) throws ResourceNotFoundException {
        Condica condica = condicaRepository.findById(condicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Condica not found for this id :: " + condicaId));

        condicaDetails.setId(condica.getId());
        final Condica updatedCondica = condicaRepository.save(condica);
        return ResponseEntity.ok(updatedCondica);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteCondica(@PathVariable(value = "id") Long condicaId)
            throws ResourceNotFoundException {
        Condica condica = condicaRepository.findById(condicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Condica not found for this id :: " + condicaId));

        condicaRepository.delete(condica);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
