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
import net.guides.springboot2.crud.model.Permission;
import net.guides.springboot2.crud.repository.PermissionRepository;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionRepository permissionRepository;

    @GetMapping
    public List<Permission> getAllPersoane() {
        return permissionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @GetMapping("{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found for this id :: " + id));

        return ResponseEntity.ok().body(permission);
    }

    @PostMapping
    public Permission createPermission(@RequestBody Permission permission) {
        return permissionRepository.save(permission);
    }

    @PutMapping("{id}")
    public ResponseEntity<Permission> updatePermission(@PathVariable(value = "id") Long id,
            @RequestBody Permission newPermission) throws ResourceNotFoundException {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found for this id :: " + id));

        newPermission.setId(permission.getId());
        final Permission updatedPermission = permissionRepository.save(newPermission);
        return ResponseEntity.ok(updatedPermission);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deletePermission(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found for this id :: " + id));

        permissionRepository.delete(permission);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
