package net.guides.springboot2.crud.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Client;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.ClientRepository;

@Service
public class ClientService {
  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private SocietateService societateService;

  public List<Client> findAll() {
    return clientRepository.findAll(Sort.by(Sort.Direction.ASC, "nume"));
  }

  public Client findById(int id) throws ResourceNotFoundException {
    return clientRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Nu exista client cu id :: " + id));
  }

  public List<Client> findBySocietate_Id(int idsocietate) {
    return clientRepository.findBySocietate_Id(idsocietate);
  }

  public Client save(Client client) {
    return clientRepository.save(client);
  }

  public Client saveBySocietate_Id(Client client, int idsocietate) throws ResourceNotFoundException {
    Societate societate = societateService.findById(idsocietate);
    
    client.setSocietate(societate);

    return clientRepository.save(client);
  }

  public Client update(int id, Client newClient) throws ResourceNotFoundException {
    Client client = findById(id);

    return clientRepository.save(client.update(newClient));
  }

  public Map<String, Boolean> delete(int id) throws ResourceNotFoundException {
    Client client = findById(id);

    clientRepository.delete(client);
    
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted " + id, Boolean.TRUE);

    return response;
  }
}
