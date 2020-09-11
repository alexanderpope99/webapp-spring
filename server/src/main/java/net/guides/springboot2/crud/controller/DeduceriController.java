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
import net.guides.springboot2.crud.model.Deduceri;
import net.guides.springboot2.crud.repository.DeduceriRepository;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/deduceri")
public class DeduceriController {
    @Autowired
    private DeduceriRepository deduceriRepository;

    @GetMapping
    public List<Deduceri> getAllPersoane() {
        return deduceriRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @GetMapping("{id}")
    public ResponseEntity<Deduceri> getDeduceriById(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Deduceri deduceri = deduceriRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deduceri not found for this id :: " + id));

        return ResponseEntity.ok().body(deduceri);
    }

    @PostMapping
    public Deduceri createDeduceri(@RequestBody Deduceri deduceri) {
        return deduceriRepository.save(deduceri);
    }

    @PostMapping("/all")
    public void createAllDeduceri(@RequestBody Deduceri deducere) {
        deduceriRepository.save(deducere);
        int zero = 495, una = 655, doua = 815, trei = 975, patru = 1295;
        for(int i=1951; i <= 3600; i+=50) {
            deducere = new Deduceri(i, i+49, zero, una, doua, trei, patru);
            deduceriRepository.save(deducere);
            zero -= 15;
            una -= 15;
            doua -= 15;
            trei -= 15;
            patru -= 15;
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Deduceri> updateDeduceri(@PathVariable(value = "id") Long id, @RequestBody Deduceri newDeduceri)
            throws ResourceNotFoundException {
        Deduceri deduceri = deduceriRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deduceri not found for this id :: " + id));

        newDeduceri.setId(deduceri.getId());
        final Deduceri updatedDeduceri = deduceriRepository.save(newDeduceri);
        return ResponseEntity.ok(updatedDeduceri);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteDeduceri(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        Deduceri deduceri = deduceriRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deduceri not found for this id :: " + id));

        deduceriRepository.delete(deduceri);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
