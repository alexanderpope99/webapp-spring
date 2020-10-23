package net.guides.springboot2.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.repository.RealizariRetineriRepository;
import net.guides.springboot2.crud.services.RealizariRetineriService;


@RestController
@RequestMapping("/realizariretineri")
public class RealizariRetineriController {
    @Autowired
	private RealizariRetineriService realizariRetineriService;
    @Autowired
    private RealizariRetineriRepository realizariRetineriRepository;


    @GetMapping("idc={id}&mo={luna}&y={an}")
    public RealizariRetineri getRealizariRetineriByIdcontract(@PathVariable(value = "id") Long idcontract, @PathVariable(value="luna") Integer luna, @PathVariable(value="an") Integer an) throws ResourceNotFoundException {
        return realizariRetineriService.getRealizariRetineri(luna, an, idcontract);
    }

    @GetMapping("idp={id}&mo={luna}&y={an}")
    public RealizariRetineri getRealizariRetineriByIdpersoana(@PathVariable(value="id") Long idpersoana, @PathVariable(value="luna") Integer luna, @PathVariable(value="an") Integer an) throws ResourceNotFoundException {
        // get contract of persoana
        long idcontract = realizariRetineriService.getIdContractByIdPersoana(idpersoana);
        return realizariRetineriService.getRealizariRetineri(luna, an, idcontract);
    }

    @GetMapping("calc/idc={id}&mo={luna}&y={an}&pb={pb}&nrt={nrt}&tos={tos}")
    public RealizariRetineri calcRealizariRetineri (
        @PathVariable(value="id") Long idcontract, 
        @PathVariable(value="luna") Integer luna, 
        @PathVariable(value="an") Integer an,
        @PathVariable(value="pb") Integer primabruta,
        @PathVariable(value="nrt") Integer nrTichete,
        @PathVariable(value="tos") Integer totalOreSuplimentare
        ) throws ResourceNotFoundException {
            return realizariRetineriService.calcRealizariRetineri(idcontract, luna, an, primabruta, nrTichete, totalOreSuplimentare);
	}
	
	@PostMapping("save/idc={id}&mo={luna}&y={an}")
	public RealizariRetineri saveRealizariRetineri(
		@PathVariable(value="id") Long idcontract,
		@PathVariable(value="luna") Integer luna,
		@PathVariable(value="an") Integer an
	) throws ResourceNotFoundException {	
		return realizariRetineriService.saveRealizariRetineri(luna, an, idcontract);
	}

    @PutMapping("update/idc={idc}&mo={luna}&y={an}")
    public RealizariRetineri updateRealizariRetineri(
		@PathVariable(name="luna") int luna,
        @PathVariable(name="an") int an,
        @PathVariable(name="idc") long idcontract,
		RealizariRetineri newRealizariRetineri) throws ResourceNotFoundException {

        RealizariRetineri oldRealizariRetineri = realizariRetineriRepository.findByLunaAndAnAndIdcontract(luna, an, idcontract);

        newRealizariRetineri.setId(oldRealizariRetineri.getId());

        final RealizariRetineri updatedRR = realizariRetineriRepository.save(newRealizariRetineri);

        return updatedRR;
	}
	
	@PutMapping("update/calc/idc={id}&mo={luna}&y={an}&pb={pb}&nrt={nrt}&tos={tos}")
    public RealizariRetineri calcThenUpdateRealizariRetineri(
		@PathVariable(value="id") Long idcontract, 
        @PathVariable(value="luna") Integer luna, 
        @PathVariable(value="an") Integer an,
        @PathVariable(value="pb") Integer primaBruta,
        @PathVariable(value="nrt") Integer nrTichete,
        @PathVariable(value="tos") Integer totalOreSuplimentare
		) throws ResourceNotFoundException {

		RealizariRetineri oldRealizariRetineri = getRealizariRetineriByIdcontract(idcontract, luna, an);
		long idstat = oldRealizariRetineri.getId();

		RealizariRetineri newRealizariRetineri = realizariRetineriService.calcRealizariRetineri(idcontract, luna, an, primaBruta, nrTichete, totalOreSuplimentare);
        newRealizariRetineri.setId(idstat);

        final RealizariRetineri updatedRR = realizariRetineriRepository.save(newRealizariRetineri);
        return updatedRR;
    }
}
    
