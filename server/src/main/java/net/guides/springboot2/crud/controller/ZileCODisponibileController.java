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
import net.guides.springboot2.crud.model.ZileCODisponibile;
import net.guides.springboot2.crud.repository.ZileCODisponibileRepository;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/zilecodisponibile")
public class ZileCODisponibileController {
    @Autowired
    private ZileCODisponibileRepository zileCODisponibileRepository;

    @GetMapping
    public List<ZileCODisponibile> getAllPersoane() {
        return zileCODisponibileRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @GetMapping("{id}")
    public ResponseEntity<ZileCODisponibile> getZileCODisponibileById(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        ZileCODisponibile zileCODisponibile = zileCODisponibileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ZileCODisponibile not found for this id :: " + id));

        return ResponseEntity.ok().body(zileCODisponibile);
    }

    @PostMapping
    public ZileCODisponibile createZileCODisponibile(@RequestBody ZileCODisponibile zileCODisponibile) {
        return zileCODisponibileRepository.save(zileCODisponibile);
    }

    @PutMapping("{id}")
    public ResponseEntity<ZileCODisponibile> updateZileCODisponibile(@PathVariable(value = "id") Long id,
            @RequestBody ZileCODisponibile newZileCODisponibile) throws ResourceNotFoundException {
        ZileCODisponibile zileCODisponibile = zileCODisponibileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ZileCODisponibile not found for this id :: " + id));

        newZileCODisponibile.setId(zileCODisponibile.getId());
        final ZileCODisponibile updatedZileCODisponibile = zileCODisponibileRepository.save(newZileCODisponibile);
        return ResponseEntity.ok(updatedZileCODisponibile);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteZileCODisponibile(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        ZileCODisponibile zileCODisponibile = zileCODisponibileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ZileCODisponibile not found for this id :: " + id));

        zileCODisponibileRepository.delete(zileCODisponibile);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
