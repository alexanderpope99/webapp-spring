package net.guides.springboot2.crud.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Client;
import net.guides.springboot2.crud.services.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {
  @Autowired
  private ClientService clientService;

  @GetMapping
  public List<Client> findAll() {
    return clientService.findAll();
  }

  @GetMapping("{id}")
  public Client findById(@PathVariable("id") int id) throws ResourceNotFoundException {
    return clientService.findById(id);
  }

  @GetMapping("ids={ids}")
  public List<Client> findBySocietate_Id(@PathVariable("ids") int id) throws ResourceNotFoundException {
    return clientService.findBySocietate_Id(id);
  }

  @PostMapping
  public Client save(@RequestBody Client client) {
    return clientService.save(client);
  }

  @PostMapping("ids={ids}")
  public Client save(@RequestBody Client client, @PathVariable("ids") int idsocietate) throws ResourceNotFoundException {
    return clientService.saveBySocietate_Id(client, idsocietate);
  }

  @PutMapping("{id}")
  public Client update(@PathVariable("id") int id, @RequestBody Client newClient) throws ResourceNotFoundException {
    return clientService.update(id, newClient);
  }

  @PutMapping("{id}/ids={ids}")
  public Client save(@PathVariable("id") int id, @RequestBody Client client, @PathVariable("ids") int idsocietate) throws ResourceNotFoundException {
    return clientService.updateBySocietate_Id(id, client, idsocietate);
  }


  @DeleteMapping("{id}")
  public Map<String, Boolean> delete(@PathVariable("id") int id) throws ResourceNotFoundException {
    return clientService.delete(id);
  }
}
