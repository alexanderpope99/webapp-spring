package net.guides.springboot2.crud.controller;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.IstoricContract;
import net.guides.springboot2.crud.payload.request.NewContractRequest;
import net.guides.springboot2.crud.services.IstoricContractService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/istoric-contract")
public class IstoricContractController {
    private final IstoricContractService service;

    public IstoricContractController(IstoricContractService service) {
        this.service = service;
    }

    @GetMapping
    public List<IstoricContract> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public IstoricContract findById(@PathVariable("id") int id) throws ResourceNotFoundException {
        return service.findById(id);
    }

    @GetMapping("/ida={ida}")
    public List<IstoricContract> findByAngajat_Id(@PathVariable("ida") int ida) {
        return service.findByAngajat_Id(ida);
    }

    @PostMapping
    public IstoricContract save(@RequestBody IstoricContract istoricContract) throws ResourceNotFoundException {
        return service.save(istoricContract);
    }

    @PostMapping("/add-new-contract")
    public Contract addNewContract(@RequestBody NewContractRequest newContractRequest) throws ResourceNotFoundException {
        return service.addNewContract(newContractRequest);
    }

    @PutMapping
    public IstoricContract update(@RequestBody IstoricContract newIstoric) throws ResourceNotFoundException {
        return service.update(newIstoric);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) throws ResourceNotFoundException {
        service.delete(id);
    }

}
