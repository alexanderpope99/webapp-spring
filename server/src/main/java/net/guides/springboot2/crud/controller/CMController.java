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
import net.guides.springboot2.crud.model.CM;
import net.guides.springboot2.crud.repository.CMRepository;

@RestController
@RequestMapping("/cm")
public class CMController {
    @Autowired
    private CMRepository cmRepository;

    @GetMapping
    public List<CM> getAllCMs() {
        return cmRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<CM> getCMById(@PathVariable(value = "id") Long cmId)
            throws ResourceNotFoundException {
        CM cm = cmRepository.findById(cmId)
                .orElseThrow(() -> new ResourceNotFoundException("CM not found for this id :: " + cmId));
        return ResponseEntity.ok().body(cm);
    }

    @PostMapping
    public CM createCM(@RequestBody CM cm) {
        return cmRepository.save(cm);
    }

    @PutMapping("{id}")
    public ResponseEntity<CM> updateCM(@PathVariable(value = "id") Long cmId,
                                       @RequestBody CM cmDetails) throws ResourceNotFoundException {
        CM cm = cmRepository.findById(cmId)
                .orElseThrow(() -> new ResourceNotFoundException("CM not found for this id :: " + cmId));

        cmDetails.setId(cm.getId());
        final CM updatedCM = cmRepository.save(cm);
        return ResponseEntity.ok(updatedCM);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteCM(@PathVariable(value = "id") Long cmId)
            throws ResourceNotFoundException {
        CM cm = cmRepository.findById(cmId)
                .orElseThrow(() -> new ResourceNotFoundException("CM not found for this id :: " + cmId));

        cmRepository.delete(cm);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
