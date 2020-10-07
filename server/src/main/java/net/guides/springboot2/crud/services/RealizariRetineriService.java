package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.ParametriiSalariu;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Retineri;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.BazacalculRepository;
import net.guides.springboot2.crud.repository.OresuplimentareRepository;
import net.guides.springboot2.crud.repository.RealizariRetineriRepository;

@Service
public class RealizariRetineriService {
	RealizariRetineriService() {
	}

	// SERVICES
	@Autowired
	private ZileService zileService;
	@Autowired
	private COService coService;
	@Autowired
	private CMService cmService;
	@Autowired
	private PersoaneIntretinereService persoaneIntretinereService;
	@Autowired
	private DeduceriService deduceriService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private ParametriiSalariuService parametriiSalariuService;
	@Autowired
	private TicheteService ticheteService;
	@Autowired

	private RetineriService retineriService;
	@Autowired
	private BazacalculService bazacalculService;

	// REPOSITORIES
	@Autowired
	private RealizariRetineriRepository realizariRetineriRepository;
	@Autowired
	private OresuplimentareRepository oresuplimentareRepository;
	@Autowired
	private AngajatRepository angajatRepository;
	@Autowired
	private BazacalculRepository bazacalculRepository;

	private float impozitSalariu = 0;
	private float deducere = 0;
	private float venitNet = 0;
	private float salariuRealizat = 0;
	private float bazaImpozit = 0;
	private float impozitScutit = 0;

	// gets RealizariRetineri + daca nu are BazaCalcul => creeaza una noua, cu
	// datele existente
	public RealizariRetineri getRealizariRetineri(int luna, int an, long idcontract) throws ResourceNotFoundException {
		RealizariRetineri rv = realizariRetineriRepository.findByLunaAndAnAndIdcontract(luna, an, idcontract);

		long idangajat = angajatRepository.findIdpersoanaByIdcontract(idcontract);
		boolean areBC = bazacalculRepository.existsByLunaAndAnAndIdangajat(luna, an, idangajat);
		if (!areBC) {
			bazacalculService.saveBazacalcul(rv);
		}

		return rv;
	} // getRealizariRetineri

	public int calcRestplata(long idcontract, int luna, int an, float totalDrepturi, float nrTichete,
			int nrPersoaneIntretinere) throws ResourceNotFoundException {

		Contract contract = contractService.getContractById(idcontract);
		ParametriiSalariu parametriiSalariu = parametriiSalariuService.getParametriiSalariu();

		this.impozitScutit = 0;

		float casSalariu = Math.round(totalDrepturi * parametriiSalariu.getCas() / 100);
		float cassSalariu = Math.round(salariuRealizat * parametriiSalariu.getCass() / 100);
		int platesteImpozit = contract.isCalculdeduceri() ? 1 : 0;

		int areFunctieDebaza = contract.isFunctiedebaza() ? 1 : 0;
		float impozit = parametriiSalariu.getImpozit() / 100;

		this.deducere = 0;
		if (totalDrepturi < 3600)
			this.deducere = deduceriService.getDeducereBySalariu(totalDrepturi, nrPersoaneIntretinere)
					* areFunctieDebaza;

		float restPlata = totalDrepturi - casSalariu - cassSalariu;
		this.venitNet = restPlata;

		float valoareTichete = nrTichete * parametriiSalariu.getValtichet();

		this.bazaImpozit = (restPlata + valoareTichete - deducere);

		this.impozitSalariu = bazaImpozit * impozit;

		if (platesteImpozit == 0)
			this.impozitScutit += impozitSalariu;

		this.impozitSalariu *= platesteImpozit;

		restPlata -= impozitSalariu * platesteImpozit;

		return Math.round(restPlata);
	} // calcRestPlata

	// just calc, doesnt affect DB
	public RealizariRetineri calcRealizariRetineri(Contract idcontract, int luna, int an, int primaBruta, int nrTichete,
			float totalOreSuplimentare) throws ResourceNotFoundException {
		Contract contract = contractService.getContractById(idcontract.getId());
		int platesteImpozit = contract.isCalculdeduceri() ? 1 : 0;

		ParametriiSalariu parametriiSalariu = parametriiSalariuService.getParametriiSalariu();

		int zileCO = coService.getZileCOTotal(luna, an, idcontract.getId());
		int zileCOLucratoare = coService.getZileCOLucratoare(luna, an, idcontract.getId());
		int zileCONeplatit = coService.getZileCFP(luna, an, idcontract.getId());
		int zileCONeplatitLucratoare = coService.getZileCFPLucratoare(luna, an, idcontract.getId());
		int zileCM = cmService.getZileCM(luna, an, idcontract.getId());
		int valCM = cmService.getValCM(luna, an, idcontract.getId());
		int zileCMLucratoare = cmService.getZileCMLucratoare(luna, an, idcontract.getId());

		int norma = zileService.getZileLucratoareInLunaAnul(luna, an);
		int duratazilucru = contract.getNormalucru();

		int zileLucrate = norma - zileCOLucratoare - zileCMLucratoare;
		int oreLucrate = zileLucrate * duratazilucru;

		float totalDrepturi = contract.getSalariutarifar() + primaBruta + totalOreSuplimentare;

		float salariuPeZi = totalDrepturi / norma;
		float salariuPeOra = totalDrepturi / norma / duratazilucru;

		this.salariuRealizat = Math.round(salariuPeZi * zileLucrate + primaBruta + totalOreSuplimentare);
		float valCO = (zileCOLucratoare - zileCONeplatitLucratoare) * salariuPeZi;
		totalDrepturi = Math.round(salariuRealizat + valCM + valCO);

		float cas = Math.round(totalDrepturi * parametriiSalariu.getCas() / 100);
		float cass = Math.round(salariuRealizat * parametriiSalariu.getCass() / 100);
		float cam = Math.round(totalDrepturi * parametriiSalariu.getCam() / 100);

		float valoareTichete = parametriiSalariu.getValtichet() * nrTichete;

		int nrPersoaneIntretinere = persoaneIntretinereService.getNrPerosaneIntretinere(contract.getId());
		int restPlata = calcRestplata(idcontract.getId(), luna, an, totalDrepturi, nrTichete, nrPersoaneIntretinere); // rv
																														// is
		// rounded
		float impozit = Math.round(this.impozitSalariu);

		RealizariRetineri rr = new RealizariRetineri(idcontract, luna, an, nrTichete, zileCO, zileCOLucratoare, zileCM,
				zileCMLucratoare, zileCONeplatit, zileCONeplatit, duratazilucru, norma, zileLucrate, oreLucrate,
				(int) totalDrepturi, salariuPeZi, salariuPeOra, cas, cass, cam, impozit, valoareTichete, restPlata,
				nrPersoaneIntretinere, (int) this.deducere, primaBruta, totalOreSuplimentare);

		int nrOreSuplimentare = oresuplimentareRepository.countNrOreSuplimentareByIdstat(rr.getId());
		int zilePlatite = norma - zileCONeplatitLucratoare;

		rr.setValcm(valCM);
		rr.setZileplatite(zilePlatite);
		rr.setNroresuplimentare(nrOreSuplimentare);
		rr.setSalariurealizat((int) salariuRealizat);
		rr.setVenitnet(Math.round(venitNet));
		rr.setBazaimpozit(Math.round(bazaImpozit * platesteImpozit));
		rr.setImpozitscutit(Math.round(this.impozitScutit));

		rr.setValcm(valCM);
		rr.setZileplatite(zilePlatite);
		rr.setNroresuplimentare(nrOreSuplimentare);
		rr.setSalariurealizat((int) salariuRealizat);

		return rr;
	} // calcRealizariRetineri

	public RealizariRetineri saveRealizariRetineri(int luna, int an, Contract idcontract)
			throws ResourceNotFoundException {
		if (realizariRetineriRepository.existsByLunaAndAnAndIdcontract(luna, an, idcontract.getId()))
			return getRealizariRetineri(luna, an, idcontract.getId());

		int nrTichete = ticheteService.getNrTichete(luna, an, idcontract.getId());

		RealizariRetineri realizariRetineri = calcRealizariRetineri(idcontract, luna, an, 0, nrTichete, 0);

		// create and save baza calcul
		bazacalculService.saveBazacalcul(realizariRetineri);

		// create empty retinere
		realizariRetineri = realizariRetineriRepository.save(realizariRetineri);
		retineriService.saveRetinere(realizariRetineri);

		return realizariRetineri;
	} // saveRealizariRetineri

	public RealizariRetineri resetRealizariRetineri(int luna, int an, Contract idcontract)
			throws ResourceNotFoundException {

		RealizariRetineri oldRealizariRetineri = realizariRetineriRepository.findByLunaAndAnAndIdcontract(luna, an,
				idcontract.getId());

		int nrTichete = ticheteService.getNrTichete(luna, an, idcontract.getId());

		RealizariRetineri realizariRetineri = calcRealizariRetineri(idcontract, luna, an, 0, nrTichete, 0);

		realizariRetineri.setId(oldRealizariRetineri.getId());
		realizariRetineri = realizariRetineriRepository.save(realizariRetineri);

		// update baza calcul
		bazacalculService.updateBazacalcul(realizariRetineri);

		// empty retinere
		Retineri oldRetinere = retineriService.getRetinereByIdstat(realizariRetineri.getId());
		retineriService.updateRetinere(oldRetinere, realizariRetineri);

		return realizariRetineri;
	} // resetRealizariRetineri
}
