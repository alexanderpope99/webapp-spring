package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

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
	@Autowired
	private RealizariRetineriService realizariRetineriService;
	@Autowired
	private AngajatRepository angajatRepository;
	@Autowired
	private SocietateRepository societateRepository;

	private String homeLocation = "src/main/java/net/guides/springboot2/crud/";

	public int getNrTichete(int luna, int an, int idcontract) throws ResourceNotFoundException {
		Contract contract = contractService.findById(idcontract);
		// daca nu are functie de baza, nu are tichete
		if (!contract.isFunctiedebaza())
			return 0;

		int zileSarbatori = 0;
		int zileCMLucratoare = cmService.getZileCMLucratoare(luna, an, idcontract);
		int zileCOLucratoare = coService.getZileCOLucratoare(luna, an, idcontract);

		int wd = zileService.getZileLucratoareInLunaAnul(luna, an);
		return wd - zileCMLucratoare - zileCOLucratoare - zileSarbatori;
	}

	public boolean createRaportTichete(int luna, int an, int idsocietate, int userID) throws IOException, ResourceNotFoundException {

		Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
		List<Angajat> angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(idsocietate);
		String lunaNume = zileService.getNumeLunaByNr(luna);

		String raportTicheteTemplateLocation = homeLocation + "/templates";

		FileInputStream file = new FileInputStream(new File(raportTicheteTemplateLocation, "RaportTichete.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet raportTichete = workbook.getSheetAt(0);

		Cell writerCell;
		int nrCrt = 0;
		for (Angajat ang : angajati) {
			Contract contract = ang.getContract();
			RealizariRetineri realizariRetineri = realizariRetineriService.saveOrGetRealizariRetineri(luna, an, contract.getId());
			Row row1 = raportTichete.createRow(1 + nrCrt);
			writerCell = row1.createCell(0);
			writerCell.setCellValue(nrCrt + 1);
			writerCell = row1.createCell(1);
			writerCell.setCellValue(ang.getPersoana().getNume() + " " + ang.getPersoana().getPrenume());
			writerCell = row1.createCell(2);
			writerCell.setCellValue(ang.getPersoana().getCnp());
			writerCell = row1.createCell(3);
			writerCell.setCellValue(realizariRetineri.getZilelucrate());
			writerCell = row1.createCell(4);
			writerCell.setCellValue(realizariRetineri.getNrtichete());
			nrCrt++;
		}

		raportTichete.autoSizeColumn(1);
		raportTichete.autoSizeColumn(2);

		/* ------ ENDING ------ **/
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String newFileLocation = String.format("%s/downloads/%d/Tichete - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}

} // class
