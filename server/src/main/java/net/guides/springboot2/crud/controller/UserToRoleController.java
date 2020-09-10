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
import net.guides.springboot2.crud.model.UserToRole;
import net.guides.springboot2.crud.repository.UserToRoleRepository;

@RestController
@RequestMapping("/usertorole")
public class UserToRoleController {
    @Autowired
    private UserToRoleRepository userToRoleRepository;

    @GetMapping
    public List<UserToRole> getAllPersoane() {
        return userToRoleRepository.findAll();
    }

    @GetMapping("{roleid}+{permissionid}")
    public ResponseEntity<UserToRole> getUserToRoleById(@PathVariable(value = "roleid") Long roleid,
            @PathVariable(value = "userid") Long userid) throws ResourceNotFoundException {
        UserToRole userToRole = userToRoleRepository.findByUseridAndRoleid(userid, roleid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "UserToRole not found for userid :: " + userid + " and roleid :: " + roleid));

        return ResponseEntity.ok().body(userToRole);
    }

    @PostMapping
    public UserToRole createUserToRole(@RequestBody UserToRole userToRole) {
        return userToRoleRepository.save(userToRole);
    }

    @PutMapping("{userid}+{roleid}")
    public ResponseEntity<UserToRole> updateUserToRole(@PathVariable(value = "roleid") Long roleid,
            @PathVariable(value = "userid") Long userid, @RequestBody UserToRole newUserToRole)
            throws ResourceNotFoundException {
        UserToRole userToRole = userToRoleRepository.findByUseridAndRoleid(userid, roleid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "UserToRole not found for userid :: " + userid + " and roleid :: " + roleid));

        newUserToRole.setRoleid(userToRole.getRoleid());
        newUserToRole.setUserid(userToRole.getUserid());

        final UserToRole updatedUserToRole = userToRoleRepository.save(newUserToRole);
        return ResponseEntity.ok(updatedUserToRole);
    }

    @DeleteMapping("{roleid}+{permissionid}")
    public Map<String, Boolean> deleteUserToRole(@PathVariable(value = "roleid") Long roleid,
            @PathVariable(value = "userid") Long userid) throws ResourceNotFoundException {
        UserToRole userToRole = userToRoleRepository.findByUseridAndRoleid(userid, roleid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "UserToRole not found for userid :: " + userid + " and roleid :: " + roleid));

        userToRoleRepository.delete(userToRole);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
