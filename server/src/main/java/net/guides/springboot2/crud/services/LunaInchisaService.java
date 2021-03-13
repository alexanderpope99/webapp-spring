package net.guides.springboot2.crud.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.LunaInchisa;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.LunaInchisaRepository;

@Service
public class LunaInchisaService {
	
	@Autowired
	private LunaInchisaRepository repo;

	@Autowired
	private SocietateService societateService;

	@Autowired
	private RealizariRetineriService rrService;

	@Autowired
	private ContractService contractService;

	public List<LunaInchisa> findAll() {
		// return repo.findAll(Sort.by(Sort.Direction.DESC, "an", "luna"));
		return repo.findAll();
	}

	public LunaInchisa findById(int id) throws ResourceNotFoundException {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Luna Inchisa not found for this id :: " + id));
	}

	public LunaInchisa findByLunaAnSocietate_Id(int luna, int an, int idsocietate) throws ResourceNotFoundException {
		return repo.findByLunaAndAnAndSocietate_Id(luna, an, idsocietate).orElseThrow(() -> new ResourceNotFoundException("Luna not found"));
	}

	public List<LunaInchisa> findBySocietate_Id(int idsocietate) {
		return repo.findBySocietate_Id(idsocietate);
	}

	public LunaInchisa save(LunaInchisa lunaInchisa, int idsocietate) throws ResourceNotFoundException {
		Societate societate = societateService.findById(idsocietate);
		List<RealizariRetineri> salarii = rrService.findByLunaAnSocietate(lunaInchisa.getLuna(), lunaInchisa.getAn(), idsocietate);

		if (contractService.findBySocietate_Id(idsocietate).size() != salarii.size()) 
			throw new ResourceNotFoundException("Toate salariile trebuie calculate inainte de inchiderea lunii.");

		lunaInchisa.setSocietate(societate);
		return repo.save(lunaInchisa);
	}

	public Boolean delete(int luna, int an, int idsocietate) throws ResourceNotFoundException {
		LunaInchisa lunaInchisa = findByLunaAnSocietate_Id(luna, an, idsocietate);
		repo.delete(lunaInchisa);

		return Boolean.TRUE;
	}

	public Boolean delete(int id) throws ResourceNotFoundException {
		LunaInchisa lunaInchisa = findById(id);
		repo.delete(lunaInchisa);
		
		return Boolean.TRUE;
	}

	public Boolean exists(int luna, int an, int idsocietate) {
		return repo.existsByLunaAndAnAndSocietate_Id(luna, an, idsocietate);
	}

	public boolean includes(List<LunaInchisa> luniInchise, int luna, int an, int idsocietate) {
		for(LunaInchisa lunaInchisa : luniInchise) {
			if(lunaInchisa.is(luna, an, idsocietate))
				return true;
		}
		return false;
	}
}
