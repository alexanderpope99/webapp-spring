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
import net.guides.springboot2.crud.model.Departament;
import net.guides.springboot2.crud.repository.DepartamentRepository;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/departament")
public class DepartamentController {
    @Autowired
    private DepartamentRepository departamentRepository;

    @GetMapping
    public List<Departament> getAllDepartaments() {
        return departamentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @GetMapping("{id}")
    public ResponseEntity<Departament> getDepartamentById(@PathVariable(value = "id") Long departamentId)
            throws ResourceNotFoundException {
        Departament departament = departamentRepository.findById(departamentId).orElseThrow(
                () -> new ResourceNotFoundException("Departament not found for this id :: " + departamentId));
        return ResponseEntity.ok().body(departament);
    }

    @PostMapping
    public Departament createDepartament(@RequestBody Departament departament) {
        return departamentRepository.save(departament);
    }

    @PutMapping("{id}")
    public ResponseEntity<Departament> updateDepartament(@PathVariable(value = "id") Long departamentId,
            @RequestBody Departament departamentDetails) throws ResourceNotFoundException {
        Departament departament = departamentRepository.findById(departamentId).orElseThrow(
                () -> new ResourceNotFoundException("Departament not found for this id :: " + departamentId));

        departamentDetails.setId(departament.getId());
        final Departament updatedDepartament = departamentRepository.save(departament);
        return ResponseEntity.ok(updatedDepartament);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteDepartament(@PathVariable(value = "id") Long departamentId)
            throws ResourceNotFoundException {
        Departament departament = departamentRepository.findById(departamentId).orElseThrow(
                () -> new ResourceNotFoundException("Departament not found for this id :: " + departamentId));

        departamentRepository.delete(departament);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
