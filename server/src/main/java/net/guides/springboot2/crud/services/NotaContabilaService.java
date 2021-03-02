package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.NotaContabilaDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Adresa;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.ParametriiSalariu;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.AdresaRepository;
import net.guides.springboot2.crud.repository.ContractRepository;
import net.guides.springboot2.crud.repository.RealizariRetineriRepository;
import net.guides.springboot2.crud.repository.RetineriRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class NotaContabilaService {
	@Autowired
	private ZileService zileService;
	@Autowired
	private SocietateRepository societateRepository;
	@Autowired
	private AdresaRepository adresaRepository;
	@Autowired
	private RealizariRetineriRepository realizariRetineriRepository;
	@Autowired
	private RetineriRepository retineriRepository;
	@Autowired
	private ContractRepository contractRepository;
	@Autowired
	private ParametriiSalariuService parametriiSalariuService;

	private String homeLocation = "src/main/java/net/guides/springboot2/crud/";

	private float getFonduriHandicap(int luna, int an, Societate societate) throws ResourceNotFoundException {
		List<Contract> contracte = contractRepository.findByAngajat_Societate_Id(societate.getId());
		if(societate.getAngajati().size() < 50) return 0;

		// contracte.removeIf(contract )
		ParametriiSalariu ps = parametriiSalariuService.getParametriiSalariu();

		int daysInMonth = YearMonth.of(an, luna).lengthOfMonth();

		float nrMediuSalariati = 0f;
		int cuHandicap = 0;

		for(Contract contract : contracte) {
			if(contract.getGradinvaliditate().compareTo("invalid") == 0)
				cuHandicap++;
			else {
				nrMediuSalariati += ((float)contract.getNormalucru() / 8) * (contract.getZileAngajare(luna, an) / daysInMonth);
			}
		}
		float nrLocuriHandicap = (float) ((nrMediuSalariati) * 0.04);
		float fondHandicap = (nrLocuriHandicap - cuHandicap) * ps.getSalariumin();
		return fondHandicap < 0 ? 0 : fondHandicap;
	}

	public boolean createNotaContabila(int luna, int an, int idsocietate, int userID) throws IOException, ResourceNotFoundException {

		Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
		Adresa adresaSocietate = adresaRepository.findById(societate.getAdresa().getId()).orElseThrow(() -> new ResourceNotFoundException("Nu există adresă pentru societatea: " + societate.getNume()));

		String statTemplateLocation = homeLocation + "/templates";

		FileInputStream file = new FileInputStream(new File(statTemplateLocation, "NotaContabila.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet stat = workbook.getSheetAt(0);

		// * date societate
		Cell writerCell = stat.getRow(0).getCell(0);
		writerCell.setCellValue(societate.getNume()); // nume soc
		writerCell = stat.getRow(1).getCell(0);
		writerCell.setCellValue("CUI: " + societate.getCif()); // cif
		writerCell = stat.getRow(2).getCell(0);
		writerCell.setCellValue("Nr. Reg. Com.: " + societate.getRegcom()); // nr reg com
		writerCell = stat.getRow(3).getCell(0);
		writerCell.setCellValue("Strada: " + adresaSocietate.getAdresa()); // adresa
		writerCell = stat.getRow(4).getCell(0);
		writerCell.setCellValue(adresaSocietate.getJudet() + ", " + adresaSocietate.getLocalitate()); // judet + localitate

		// * - LUNA AN -
		writerCell = stat.getRow(7).getCell(2);
		String lunaNume = zileService.getNumeLunaByNr(luna);
		writerCell.setCellValue("- " + lunaNume + " " + an + " -");

		int idSocietate = societate.getId();

		NotaContabilaDTO notaContabila = realizariRetineriRepository.getNotaContabilaByLunaAndAnAndIdsocietate(luna, an, idSocietate);
		if (notaContabila == null) {
			workbook.close();
			throw new ResourceNotFoundException("Nu este salariul calculat pentru idsocietate " + idSocietate + " în " + luna + "/" + an);
		}

		// * Concedii medicale din fonduri
		writerCell = stat.getRow(14).getCell(5);
		long valcm = notaContabila.getValCM();
		writerCell.setCellValue(valcm);

		// * Salarii datorate personalului
		writerCell = stat.getRow(15).getCell(5);
		long salDatorat = notaContabila.getSalariuDatorat();
		writerCell.setCellValue(salDatorat);

		// * Avans
		writerCell = stat.getRow(21).getCell(5);
		long avans = retineriRepository.getAvansByLunaAndAnByIdsocietate(luna, an, idSocietate);
		writerCell.setCellValue(avans);

		// * CAS 25% angajat
		writerCell = stat.getRow(22).getCell(5);
		long cas25 = notaContabila.getCas25() - Math.round(notaContabila.getValCM() * 0.25);
		writerCell.setCellValue(cas25);

		// * CASS 10% angajat
		writerCell = stat.getRow(24).getCell(5);
		writerCell.setCellValue(notaContabila.getCass10() - Math.round(notaContabila.getValCM() * 0.065)); 

		// * Impozit
		writerCell = stat.getRow(25).getCell(5);
		long impozit = notaContabila.getImpozit();
		writerCell.setCellValue(impozit);

		// * CAM
		writerCell = stat.getRow(32).getCell(5);
		long cam = notaContabila.getCam();
		writerCell.setCellValue(cam);

		// * Fond Handicap
		writerCell = stat.getRow(38).getCell(5);
		writerCell.setCellValue(getFonduriHandicap(luna, an, societate));

		/* ------ ENDING ------ **/
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		evaluator.evaluateAll();
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String newFileLocation = String.format("%s/downloads/%d/Nota Contabila - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}
}