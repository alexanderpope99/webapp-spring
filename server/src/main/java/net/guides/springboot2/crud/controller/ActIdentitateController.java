package net.guides.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import net.guides.springboot2.crud.model.ActIdentitate;
import net.guides.springboot2.crud.repository.ActIdentitateRepository;

@RestController
@RequestMapping("/actidentitate")
public class ActIdentitateController {
    @Autowired
    private ActIdentitateRepository actIdentitateRepository;

    @GetMapping
    public List<ActIdentitate> getAllActIdentitates() {
        return actIdentitateRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @GetMapping("{id}")
    public ResponseEntity<ActIdentitate> getActIdentitateById(@PathVariable(value = "id") Long actIdentitateId)
            throws ResourceNotFoundException {
        ActIdentitate actIdentitate = actIdentitateRepository.findById(actIdentitateId).orElseThrow(
                () -> new ResourceNotFoundException("ActIdentitate not found for this id :: " + actIdentitateId));
        return ResponseEntity.ok().body(actIdentitate);
    }

    @PostMapping
    public ActIdentitate createActIdentitate(@RequestBody ActIdentitate actIdentitate) {
        return actIdentitateRepository.save(actIdentitate);
    }

    @PutMapping("{id}")
    public ResponseEntity<ActIdentitate> updateActIdentitate(@PathVariable(value = "id") Long actIdentitateId,
            @RequestBody ActIdentitate actIdentitateDetails) throws ResourceNotFoundException {
        ActIdentitate actIdentitate = actIdentitateRepository.findById(actIdentitateId).orElseThrow(
                () -> new ResourceNotFoundException("ActIdentitate not found for this id :: " + actIdentitateId));

        actIdentitateDetails.setId(actIdentitate.getId());
        final ActIdentitate updatedActIdentitate = actIdentitateRepository.save(actIdentitate);
        return ResponseEntity.ok(updatedActIdentitate);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteActIdentitate(@PathVariable(value = "id") Long actIdentitateId)
            throws ResourceNotFoundException {
        ActIdentitate actIdentitate = actIdentitateRepository.findById(actIdentitateId).orElseThrow(
                () -> new ResourceNotFoundException("ActIdentitate not found for this id :: " + actIdentitateId));

        actIdentitateRepository.delete(actIdentitate);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
