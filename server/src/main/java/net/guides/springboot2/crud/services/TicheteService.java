package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Contract;

// import net.guides.springboot2.crud.repository.TicheteRepository;

@Service
public class TicheteService {
	TicheteService() {
	}

	@Autowired
	private ZileService zileService;
	@Autowired
	private CMService cmService;
	@Autowired
	private COService coService;
	@Autowired
	private ContractService contractService;

	public int getNrTichete(int luna, int an, int idcontract) throws ResourceNotFoundException {
		Contract contract = contractService.findById(idcontract);
		// daca nu are functie de baza, nu are tichete
		if(!contract.isFunctiedebaza()) return 0;

		int zileSarbatori = 0;
		int zileCMLucratoare = cmService.getZileCMLucratoare(luna, an, idcontract);
		int zileCOLucratoare = coService.getZileCOLucratoare(luna, an, idcontract);

		int wd = zileService.getZileLucratoareInLunaAnul(luna, an);
		return wd - zileCMLucratoare - zileCOLucratoare - zileSarbatori;
	}

} // class
