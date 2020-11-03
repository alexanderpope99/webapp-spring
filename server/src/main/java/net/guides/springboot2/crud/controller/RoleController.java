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
import net.guides.springboot2.crud.model.Role;
import net.guides.springboot2.crud.repository.RoleRepository;

import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public List<Role> getAllPersoane() {
        return roleRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @GetMapping("{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable(value = "id") int id) throws ResourceNotFoundException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found for this id :: " + id));

        return ResponseEntity.ok().body(role);
    }

    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    @PutMapping("{id}")
    public ResponseEntity<Role> updateRole(@PathVariable(value = "id") int id, @RequestBody Role newRole)
            throws ResourceNotFoundException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found for this id :: " + id));

        newRole.setId(role.getId());
        final Role updatedRole = roleRepository.save(newRole);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteRole(@PathVariable(value = "id") int id) throws ResourceNotFoundException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found for this id :: " + id));

        roleRepository.delete(role);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
