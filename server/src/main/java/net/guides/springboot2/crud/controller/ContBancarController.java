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
import net.guides.springboot2.crud.model.ContBancar;
import net.guides.springboot2.crud.repository.ContBancarRepository;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/contbancar")
public class ContBancarController {
    @Autowired
    private ContBancarRepository contBancarRepository;

    @GetMapping
    public List<ContBancar> getAllContBancars() {
        return contBancarRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @GetMapping("{id}")
    public ResponseEntity<ContBancar> getContBancarById(@PathVariable(value = "id") Long contBancarId)
            throws ResourceNotFoundException {
        ContBancar contBancar = contBancarRepository.findById(contBancarId).orElseThrow(
                () -> new ResourceNotFoundException("ContBancar not found for this id :: " + contBancarId));
        return ResponseEntity.ok().body(contBancar);
    }

    @PostMapping
    public ContBancar createContBancar(@RequestBody ContBancar contBancar) {
        return contBancarRepository.save(contBancar);
    }

    @PutMapping("{id}")
    public ResponseEntity<ContBancar> updateContBancar(@PathVariable(value = "id") Long contBancarId,
            @RequestBody ContBancar contBancarDetails) throws ResourceNotFoundException {
        ContBancar contBancar = contBancarRepository.findById(contBancarId).orElseThrow(
                () -> new ResourceNotFoundException("ContBancar not found for this id :: " + contBancarId));

        contBancarDetails.setIban(contBancar.getIban());
        final ContBancar updatedContBancar = contBancarRepository.save(contBancar);
        return ResponseEntity.ok(updatedContBancar);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteContBancar(@PathVariable(value = "id") Long contBancarId)
            throws ResourceNotFoundException {
        ContBancar contBancar = contBancarRepository.findById(contBancarId).orElseThrow(
                () -> new ResourceNotFoundException("ContBancar not found for this id :: " + contBancarId));

        contBancarRepository.delete(contBancar);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
