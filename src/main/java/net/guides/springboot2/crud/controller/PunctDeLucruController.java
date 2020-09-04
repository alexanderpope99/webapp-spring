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
import net.guides.springboot2.crud.model.PunctDeLucru;
import net.guides.springboot2.crud.repository.PunctDeLucruRepository;


@RestController
@RequestMapping("/punctdelucru")
public class PunctDeLucruController {
    @Autowired
    private PunctDeLucruRepository punctDeLucruRepository;

    @GetMapping
    public List<PunctDeLucru> getAllPersoane() {
        return punctDeLucruRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<PunctDeLucru> getPunctDeLucruById(@PathVariable(value="id") Long id) throws ResourceNotFoundException
    {
        PunctDeLucru punctDeLucru = punctDeLucruRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("PunctDeLucru not found for this id :: " + id));

        return ResponseEntity.ok().body(punctDeLucru);
    }

    @PostMapping
        public PunctDeLucru createPunctDeLucru(@RequestBody PunctDeLucru punctDeLucru) {
        return punctDeLucruRepository.save(punctDeLucru);
    }

    @PutMapping("{id}")
    public ResponseEntity<PunctDeLucru> updatePunctDeLucru(@PathVariable(value = "id") Long id,  @RequestBody PunctDeLucru newPunctDeLucru) throws ResourceNotFoundException {
        PunctDeLucru punctDeLucru = punctDeLucruRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("PunctDeLucru not found for this id :: " + id));

        newPunctDeLucru.setId(punctDeLucru.getId());
        final PunctDeLucru updatedPunctDeLucru = punctDeLucruRepository.save(newPunctDeLucru);
        return ResponseEntity.ok(updatedPunctDeLucru);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deletePunctDeLucru(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        PunctDeLucru punctDeLucru = punctDeLucruRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PunctDeLucru not found for this id :: " + id));

        punctDeLucruRepository.delete(punctDeLucru);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
