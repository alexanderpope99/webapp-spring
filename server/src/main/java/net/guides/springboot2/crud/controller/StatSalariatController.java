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
import net.guides.springboot2.crud.model.StatSalariat;
import net.guides.springboot2.crud.repository.StatSalariatRepository;


@RestController
@RequestMapping("/statsalariat")
public class StatSalariatController {
    @Autowired
    private StatSalariatRepository statSalariatRepository;

    @GetMapping
    public List<StatSalariat> getAllPersoane() {
        return statSalariatRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<StatSalariat> getStatSalariatById(@PathVariable(value="id") Long id) throws ResourceNotFoundException
    {
        StatSalariat statSalariat = statSalariatRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("StatSalariat not found for this id :: " + id));

        return ResponseEntity.ok().body(statSalariat);
    }

    @PostMapping
        public StatSalariat createStatSalariat(@RequestBody StatSalariat statSalariat) {
        return statSalariatRepository.save(statSalariat);
    }

    @PutMapping("{id}")
    public ResponseEntity<StatSalariat> updateStatSalariat(@PathVariable(value = "id") Long id,  @RequestBody StatSalariat newStatSalariat) throws ResourceNotFoundException {
        StatSalariat statSalariat = statSalariatRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("StatSalariat not found for this id :: " + id));

        newStatSalariat.setId(statSalariat.getId());
        final StatSalariat updatedStatSalariat = statSalariatRepository.save(newStatSalariat);
        return ResponseEntity.ok(updatedStatSalariat);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteStatSalariat(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        StatSalariat statSalariat = statSalariatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StatSalariat not found for this id :: " + id));

        statSalariatRepository.delete(statSalariat);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
