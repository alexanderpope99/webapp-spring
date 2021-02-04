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
import net.guides.springboot2.crud.model.RoleToPermission;
import net.guides.springboot2.crud.repository.RoleToPermissionRepository;

@RestController
@RequestMapping("/roletopermission")
public class RoleToPermissionController {
    @Autowired
    private RoleToPermissionRepository roleToPermissionRepository;

    @GetMapping
    public List<RoleToPermission> getAllPersoane() {
        return roleToPermissionRepository.findAll();
    }

    @GetMapping("{roleid}+{permissionid}")
    public ResponseEntity<RoleToPermission> getRoleToPermissionById(@PathVariable("roleid") Long roleid,
            @PathVariable("permissionid") Long permissionid) throws ResourceNotFoundException {
        RoleToPermission roleToPermission = roleToPermissionRepository.findByRoleidAndPermissionid(roleid, permissionid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "RoleToPermission not found for roleid :: " + roleid + " and permissionid :: " + permissionid));

        return ResponseEntity.ok().body(roleToPermission);
    }

    @PostMapping
    public RoleToPermission createRoleToPermission(@RequestBody RoleToPermission roleToPermission) {
        return roleToPermissionRepository.save(roleToPermission);
    }

    @PutMapping("{roleid}+{permissionid}")
    public ResponseEntity<RoleToPermission> updateRoleToPermission(@PathVariable("roleid") Long roleid,
            @PathVariable("permissionid") Long permissionid, @RequestBody RoleToPermission newRoleToPermission)
            throws ResourceNotFoundException {
        RoleToPermission roleToPermission = roleToPermissionRepository.findByRoleidAndPermissionid(roleid, permissionid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "RoleToPermission not found for roleid :: " + roleid + " and permissionid :: " + permissionid));

        newRoleToPermission.setPermissionid(roleToPermission.getPermissionid());
        newRoleToPermission.setRoleid(roleToPermission.getRoleid());

        final RoleToPermission updatedRoleToPermission = roleToPermissionRepository.save(newRoleToPermission);
        return ResponseEntity.ok(updatedRoleToPermission);
    }

    @DeleteMapping("{roleid}+{permissionid}")
    public Map<String, Boolean> deleteRoleToPermission(@PathVariable("roleid") Long roleid,
            @PathVariable("permissionid") Long permissionid) throws ResourceNotFoundException {
        RoleToPermission roleToPermission = roleToPermissionRepository.findByRoleidAndPermissionid(roleid, permissionid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "RoleToPermission not found for roleid :: " + roleid + " and permissionid :: " + permissionid));

        roleToPermissionRepository.delete(roleToPermission);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
