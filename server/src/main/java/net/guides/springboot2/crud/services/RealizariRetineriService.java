package net.guides.springboot2.crud.services;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.LuniCuSalarii;
import net.guides.springboot2.crud.dto.RRDetails;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.ParametriiSalariu;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Retineri;
import net.guides.springboot2.crud.model.Angajat;
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
	private PersoanaIntretinereService persoaneIntretinereService;
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
	public RealizariRetineri getRealizariRetineri(int luna, int an, int idcontract) throws ResourceNotFoundException {
		RealizariRetineri rv = realizariRetineriRepository.findByLunaAndAnAndContract_Id(luna, an, idcontract);
		if (rv == null)
			throw new ResourceNotFoundException("Salariul nu este calculat în " + luna + "/" + an);

		int idangajat = angajatRepository.findIdpersoanaByIdcontract(idcontract);

		boolean areBC = bazacalculRepository.existsByLunaAndAnAndAngajat_Idpersoana(luna, an, idangajat);
		if (!areBC) {
			bazacalculService.saveBazacalcul(rv);
		}

		return rv;
	} // getRealizariRetineri

	public int calcRestplata(int idcontract, int luna, int an, float totalDrepturi, float nrTichete,
			int nrPersoaneIntretinere) throws ResourceNotFoundException {

		Contract contract = contractService.getContractById(idcontract);
		if (contract == null)
			throw new ResourceNotFoundException("Nu există contract cu id " + idcontract);

		ParametriiSalariu parametriiSalariu = parametriiSalariuService.getParametriiSalariu();
		if (parametriiSalariu == null)
			throw new ResourceNotFoundException("Nu există parametrii salariu");

		this.impozitScutit = 0;

		float casSalariu = Math.round(totalDrepturi * parametriiSalariu.getCas() / 100);
		float cassSalariu = Math.round(salariuRealizat * parametriiSalariu.getCass() / 100);
		int platesteImpozit = contract.isCalculdeduceri() ? 1 : 0;

		int areFunctieDebaza = contract.isFunctiedebaza() ? 1 : 0;
		float impozit = parametriiSalariu.getImpozit() / 100;

		this.deducere = 0;
		if (totalDrepturi < 3600)
			this.deducere = deduceriService.getDeducereBySalariu((int) totalDrepturi, nrPersoaneIntretinere)
					* areFunctieDebaza * platesteImpozit;

		float restPlata = totalDrepturi - casSalariu - cassSalariu;

		float valoareTichete = nrTichete * parametriiSalariu.getValtichet();
		this.venitNet = restPlata + valoareTichete;

		Integer deducerePensieFacultativa = retineriService.calculeazaPensieDeductibila(idcontract, an, luna);
		if (deducerePensieFacultativa == null)
			deducerePensieFacultativa = 0;

		this.bazaImpozit = (restPlata + valoareTichete - deducere - deducerePensieFacultativa);

		this.impozitSalariu = bazaImpozit * impozit;

		if (platesteImpozit == 0)
			this.impozitScutit += impozitSalariu;

		this.impozitSalariu *= platesteImpozit;

		restPlata -= impozitSalariu * platesteImpozit;

		// get Retineri here and subtract it
		Retineri retineri = retineriService.getRetinereByIdcontractAndLunaAndAn(idcontract, luna, an);
		if (retineri != null) {
			restPlata -= (retineri.getAvansnet() + retineri.getTotalPensiiFacultativeRON()
					+ retineri.getPensiealimentara() + retineri.getPopriri() + retineri.getImprumuturi());
		}

		return Math.round(restPlata);
	} // calcRestPlata

	// just calc, doesnt affect DB
	public RealizariRetineri calcRealizariRetineri(int idcontract, int luna, int an, int primaBruta, int nrTichete,
			int totalOreSuplimentare) throws ResourceNotFoundException {
		Contract contract = contractService.getContractById(idcontract);
		
		impozitSalariu = 0;
		deducere = 0;
		venitNet = 0;
		salariuRealizat = 0;
		bazaImpozit = 0;
		impozitScutit = 0;

		int zileContract = zileService.getNrZileLucratoareContract(luna, an, contract);
		if (zileContract == 0) {
			// return retineri wtih 0 values
			return new RealizariRetineri(luna, an, contract);
		}

		int platesteImpozit = contract.isCalculdeduceri() ? 1 : 0;

		ParametriiSalariu parametriiSalariu = parametriiSalariuService.getParametriiSalariu();

		int zileCOTotal = coService.getZileCOTotal(luna, an, idcontract);

		int zileCO = 0, zileCOLucratoare = 0;
		int zileCFP = 0, zileCFPLucratoare = 0;
		if (zileCOTotal != 0) {
			zileCO = coService.getZileCO(luna, an, idcontract);
			zileCOLucratoare = coService.getZileCOLucratoare(luna, an, idcontract);
			zileCFP = coService.getZileCFP(luna, an, idcontract);
			zileCFPLucratoare = coService.getZileCFPLucratoare(luna, an, idcontract);
		}
		int zileCM = cmService.getZileCM(luna, an, idcontract);
		int valCM = 0, zileCMLucratoare = 0;
		if (zileCM != 0) {
			valCM = cmService.getValCM(luna, an, idcontract);
			zileCMLucratoare = cmService.getZileCMLucratoare(luna, an, idcontract);
		}

		int norma = zileService.getZileLucratoareInLunaAnul(luna, an);
		int duratazilucru = contract.getNormalucru();

		int zileLucrate = zileContract - zileCOLucratoare - zileCMLucratoare - zileCFPLucratoare;
		int oreLucrate = zileLucrate * duratazilucru;

		float totalDrepturi = contract.getSalariutarifar() + primaBruta + totalOreSuplimentare;

		float salariuPeZi = totalDrepturi / norma;
		float salariuPeOra = totalDrepturi / norma / duratazilucru;

		this.salariuRealizat = Math.round(salariuPeZi * (zileContract - zileCFPLucratoare - zileCMLucratoare)
				+ primaBruta + totalOreSuplimentare);

		// zileCOLucratoare include zileCFPLucratoare
		float valCO = (zileCOLucratoare - zileCFPLucratoare) * salariuPeZi;
		totalDrepturi = Math.round(salariuRealizat + valCM - primaBruta);

		float cas = Math.round(totalDrepturi * parametriiSalariu.getCas() / 100);
		float cass = Math.round(salariuRealizat * parametriiSalariu.getCass() / 100); // probleme
		float cam = Math.round(totalDrepturi * parametriiSalariu.getCam() / 100);

		float valoareTichete = parametriiSalariu.getValtichet() * nrTichete;

		int nrPersoaneIntretinere = persoaneIntretinereService.getNrPersoaneIntretinere(contract.getId());

		int restPlata = 0;
		int zilePlatite = norma - zileCFPLucratoare;
		if (zilePlatite > 0)
			restPlata = calcRestplata(idcontract, luna, an, totalDrepturi, nrTichete, nrPersoaneIntretinere);

		float impozit = Math.round(this.impozitSalariu);

		RealizariRetineri rr = new RealizariRetineri(contract, luna, an, nrTichete, zileCO, zileCOLucratoare, zileCM,
				zileCMLucratoare, zileCFP, zileCFPLucratoare, duratazilucru, norma, zileLucrate, oreLucrate,
				(int) totalDrepturi, salariuPeZi, salariuPeOra, cas, cass, cam, impozit, valoareTichete, restPlata,
				nrPersoaneIntretinere, (int) this.deducere, primaBruta, totalOreSuplimentare);

		int nrOreSuplimentare = oresuplimentareRepository.getNrOreSuplimentareByIdstat(rr.getId());

		rr.setValcm(valCM);
		rr.setZileplatite(zilePlatite);
		rr.setNroresuplimentare(nrOreSuplimentare);
		rr.setSalariurealizat((int) salariuRealizat);
		rr.setVenitnet(Math.round(venitNet));
		rr.setBazaimpozit(Math.round(bazaImpozit * platesteImpozit));
		rr.setImpozitscutit(Math.round(this.impozitScutit));
		rr.setValco(Math.round(valCO));
		rr.setZilecontract(zileContract);

		return rr;
	} // calcRealizariRetineri

	public List<LuniCuSalarii> getAllRealizariRetineriByIdSocietateAndIdUser(int ids, int idu)
			throws ResourceNotFoundException {
		Angajat ang = angajatRepository.findBySocietate_IdAndUser_Id(ids, idu);
		if (ang == null)
			throw new ResourceNotFoundException("Nu există angajat în societatea cu id " + ids + " și id user " + idu);

		List<LuniCuSalarii> lunaan = new ArrayList<>();

		List<RealizariRetineri> rrl = realizariRetineriRepository
				.findAnByContract_IdOrderByAnAsc(ang.getContract().getId());

		Set<Integer> ani = new HashSet<>();

		for (RealizariRetineri rr : rrl) {
			ani.add(rr.getAn());
		}

		for (int an : ani) {
			LuniCuSalarii luna = new LuniCuSalarii(an);
			List<RealizariRetineri> rrs = realizariRetineriRepository.findLunaByAnAndContract_IdOrderByLunaAsc(an,
					ang.getContract().getId());

			for (RealizariRetineri rr : rrs)
				luna.addLuna(rr.getLuna());

			lunaan.add(luna);
		}

		return lunaan;
	}

	// calculeaza apoi salveaza
	public RealizariRetineri saveRealizariRetineri(int luna, int an, int idcontract) throws ResourceNotFoundException {
		int nrTichete = ticheteService.getNrTichete(luna, an, idcontract);

		RealizariRetineri realizariRetineri = calcRealizariRetineri(idcontract, luna, an, 0, nrTichete, 0);

		// create and save baza calcul only if contract valid in month, year
		bazacalculService.saveBazacalcul(realizariRetineri);

		realizariRetineriRepository.save(realizariRetineri);

		Retineri retineri = retineriService.saveRetinere(realizariRetineri);

		realizariRetineri.setRetineri(retineri);

		realizariRetineri = realizariRetineriRepository.save(realizariRetineri);

		return realizariRetineri;
	} // saveRealizariRetineri

	// daca exista deja calculate, returneaza pe acelea, daca nu, calculeaza
	public RealizariRetineri saveOrGetRealizariRetineri(int luna, int an, int idcontract)
			throws ResourceNotFoundException {
		if (realizariRetineriRepository.existsByLunaAndAnAndContract_Id(luna, an, idcontract))
			return this.getRealizariRetineri(luna, an, idcontract);
		else
			return this.saveRealizariRetineri(luna, an, idcontract);
	} // saveOrGetRealizariRetineri

	public RealizariRetineri recalcRealizariRetineri(RRDetails rrDetails) throws ResourceNotFoundException {
		return recalcRealizariRetineri(rrDetails.getLuna(), rrDetails.getAn(), rrDetails.getIdcontract(), rrDetails.getPrimaBruta(), rrDetails.getNrTichete(), rrDetails.getTotalOreSuplimentare());
	}

	public RealizariRetineri recalcRealizariRetineri(int luna, int an, int idcontract, int primaBruta, int nrTichete,
			int totalOreSuplimentare) throws ResourceNotFoundException {

		// nu e calculat in (luna, an) => calculeaza/creeaza
		if (!realizariRetineriRepository.existsByLunaAndAnAndContract_Id(luna, an, idcontract)) {
			return this.saveRealizariRetineri(luna, an, idcontract);
		}

		// verifica daca trebuie folosite (primaBruta, nrTichete, totalOreSuplimentare)
		// existente
		if (primaBruta == -1 && nrTichete == -1 && totalOreSuplimentare == -1) {
			RealizariRetineri tmpRR = realizariRetineriRepository.findByLunaAndAnAndContract_Id(luna, an, idcontract);
			primaBruta = tmpRR.getPrimabruta() == null ? 0 : tmpRR.getPrimabruta();
			nrTichete = tmpRR.getNrtichete() == null ? 0 : tmpRR.getNrtichete();
			totalOreSuplimentare = tmpRR.getTotaloresuplimentare() == null ? 0 : tmpRR.getTotaloresuplimentare();
		}

		RealizariRetineri oldRealizariRetineri = realizariRetineriRepository.findByLunaAndAnAndContract_Id(luna, an,
				idcontract);
		if (oldRealizariRetineri == null)
			throw new ResourceNotFoundException("Nu are salariul calculat în " + luna + "/" + an);

		RealizariRetineri newRealizariRetineri = this.calcRealizariRetineri(idcontract, luna, an, primaBruta, nrTichete,
				totalOreSuplimentare);
		newRealizariRetineri.setId(oldRealizariRetineri.getId());

		bazacalculService.updateBazacalcul(newRealizariRetineri);

		return realizariRetineriRepository.save(newRealizariRetineri);
	} // recalcRealizariRetineri

	// exclude (luna, an) din argument
	// primaBruta, nrTichete, totalOreSuplimentare raman neschimbate
	public void recalcRealizariRetineriUltimele6Luni(int luna, int an, int idcontract)
			throws ResourceNotFoundException {
		int luna6 = 0, an6 = an;
		if (luna <= 6) {
			luna6 = 12 - (6 - luna);
			an6--;
		} else
			luna6 = luna - 6;

		// ( -1, -1, -1) == foloseste (primaBruta, nrTichete, totalOreSuplimentare)
		// existente
		if (luna6 > luna && an6 < an) {
			for (int i = luna6; i <= 12; ++i) {
				this.recalcRealizariRetineri(i, an6, idcontract, -1, -1, -1);
			}

			for (int i = 1; i <= luna; ++i) {
				this.recalcRealizariRetineri(i, an6, idcontract, -1, -1, -1);
			}
		} else {
			for (int i = luna6; i <= luna; ++i) {
				this.recalcRealizariRetineri(i, an6, idcontract, -1, -1, -1);
			}
		}
	}

	public void recalcSocietate(int luna, int an, int idsocietate) throws ResourceNotFoundException {
		List<Angajat> angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNull(idsocietate);

		for (Angajat angajat : angajati) {
			this.recalcRealizariRetineri(luna, an, angajat.getContract().getId(), -1, -1, -1);
		}
	}

	public void recalcSocietateUltimele6Luni(int luna, int an, int idsocietate) throws ResourceNotFoundException {
		List<Angajat> angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNull(idsocietate);

		for (Angajat angajat : angajati) {
			this.recalcRealizariRetineriUltimele6Luni(luna, an, angajat.getContract().getId());
		}
	}

	public boolean fixValuesMissing() {
		var wrapper = new Object(){ boolean value = true; };

		realizariRetineriRepository.findAll().forEach(rr -> {
			try {
				realizariRetineriRepository.save(rr.fixValuesMissing());
			} catch (ResourceNotFoundException e) {
				e.printStackTrace();
				wrapper.value = false;
			}
		});

		return wrapper.value;
	}
}
