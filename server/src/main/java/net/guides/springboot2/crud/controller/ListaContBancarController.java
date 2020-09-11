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
import net.guides.springboot2.crud.model.ListaContBancar;
import net.guides.springboot2.crud.repository.ListaContBancarRepository;

@RestController
@RequestMapping("/listacontbancar")
public class ListaContBancarController {
    @Autowired
    private ListaContBancarRepository listaContBancarRepository;

    @GetMapping
    public List<ListaContBancar> getAllListaContBancars() {
        return listaContBancarRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<ListaContBancar> getListaContBancarById(@PathVariable(value = "id") Long listaContBancarId)
            throws ResourceNotFoundException {
        ListaContBancar listaContBancar = listaContBancarRepository.findById(listaContBancarId).orElseThrow(
                () -> new ResourceNotFoundException("ListaContBancar not found for this id :: " + listaContBancarId));
        return ResponseEntity.ok().body(listaContBancar);
    }

    @PostMapping
    public ListaContBancar createListaContBancar(@RequestBody ListaContBancar listaContBancar) {
        return listaContBancarRepository.save(listaContBancar);
    }

    @PutMapping("{id}")
    public ResponseEntity<ListaContBancar> updateListaContBancar(@PathVariable(value = "id") Long listaContBancarId,
            @RequestBody ListaContBancar listaContBancarDetails) throws ResourceNotFoundException {
        ListaContBancar listaContBancar = listaContBancarRepository.findById(listaContBancarId).orElseThrow(
                () -> new ResourceNotFoundException("ListaContBancar not found for this id :: " + listaContBancarId));

        listaContBancarDetails.setIdsocietate(listaContBancar.getIdsocietate());
        listaContBancarDetails.setIban((listaContBancar.getIban()));
        final ListaContBancar updatedListaContBancar = listaContBancarRepository.save(listaContBancar);
        return ResponseEntity.ok(updatedListaContBancar);
    }

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteListaContBancar(@PathVariable(value = "id") Long listaContBancarId)
            throws ResourceNotFoundException {
        ListaContBancar listaContBancar = listaContBancarRepository.findById(listaContBancarId).orElseThrow(
                () -> new ResourceNotFoundException("ListaContBancar not found for this id :: " + listaContBancarId));

        listaContBancarRepository.delete(listaContBancar);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
