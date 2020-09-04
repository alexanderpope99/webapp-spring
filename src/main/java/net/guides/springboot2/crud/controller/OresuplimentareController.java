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
import net.guides.springboot2.crud.model.Oresuplimentare;
import net.guides.springboot2.crud.repository.OresuplimentareRepository;


@RestController
@RequestMapping("/oresuplimentare")
public class OresuplimentareController {
    @Autowired
    private OresuplimentareRepository oresuplimentareRepository;

    @GetMapping
    public List<Oresuplimentare> getAllPersoane() {
        return oresuplimentareRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Oresuplimentare> getOresuplimentareById(@PathVariable(value="id") Long id) throws ResourceNotFoundException
    {
        Oresuplimentare oresuplimentare = oresuplimentareRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Oresuplimentare not found for this id :: " + id));

        return ResponseEntity.ok().body(oresuplimentare);
    }

    @PostMapping
        public Oresuplimentare createOresuplimentare(@RequestBody Oresuplimentare oresuplimentare) {
        return oresuplimentareRepository.save(oresuplimentare);
    }

    @PutMapping("{id}")
    public ResponseEntity<Oresuplimentare> updateOresuplimentare(@PathVariable(value = "id") Long id,  @RequestBody Oresuplimentare newOresuplimentare) throws ResourceNotFoundException {
        Oresuplimentare oresuplimentare = oresuplimentareRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Oresuplimentare not found for this id :: " + id));

        newOresuplimentare.setId(oresuplimentare.getId());
        final Oresuplimentare updatedOresuplimentare = oresuplimentareRepository.save(newOresuplimentare);
        return ResponseEntity.ok(updatedOresuplimentare);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteOresuplimentare(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Oresuplimentare oresuplimentare = oresuplimentareRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oresuplimentare not found for this id :: " + id));

        oresuplimentareRepository.delete(oresuplimentare);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
