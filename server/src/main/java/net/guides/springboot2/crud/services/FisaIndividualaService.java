package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Adresa;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.CM;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.ParametriiSalariu;
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Societate;

@Service
public class FisaIndividualaService {

	@Autowired
	private AngajatService angajatService;

	@Autowired
	private ZileService zileService;

	@Autowired
	private ParametriiSalariuService parametriiSalariuService;

	@Autowired
	private RealizariRetineriService realizariRetineriService;

	@Autowired
	private CMService cmService;

	private String homeLocation = "src/main/java/net/guides/springboot2/crud/";

	public boolean create(int lunaDela, int lunaPanala, int an, int idangajat, int userID) throws IOException, ResourceNotFoundException {
		ParametriiSalariu parametriiSalariu = parametriiSalariuService.getParametriiSalariu();
		Angajat angajat = angajatService.findById(idangajat);
		Persoana persoana = angajat.getPersoana();
		Contract contract = angajat.getContract();
		List<RealizariRetineri> realizariRetineri = realizariRetineriService.saveOrGetFromTo(lunaDela, lunaPanala, an, contract.getId());

		String sheetTemplateLocation = homeLocation + "/templates";

		FileInputStream file = new FileInputStream(new File(sheetTemplateLocation, "AdeverintaDeVenit.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet sheet = workbook.getSheetAt(0);
		
		// nume societate:
		Cell writerCell = sheet.getRow(0).getCell(0);
		writerCell.setCellValue(angajat.getSocietate().getNume());
		// cui
		writerCell = sheet.getRow(1).getCell(0);
		writerCell.setCellValue("CUI: " + angajat.getSocietate().getCif());
		// data crearii fisierului
		writerCell = sheet.getRow(0).getCell(15);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/mm/yyyy");
		writerCell.setCellValue(LocalDate.now().format(formatter));

		// titlu
		writerCell = sheet.getRow(1).getCell(2);
		writerCell.setCellValue("FISA INDIVIDUALA PENTRU ANUL " + an);
		// nume angajat
		writerCell = sheet.getRow(2).getCell(2);
		writerCell.setCellValue(angajat.getPersoana().getNumeIntreg());

		Row row = sheet.getRow(4);
		// functia
		writerCell = row.getCell(1);
		writerCell.setCellValue(contract.getFunctie());
		// cnp angajat
		writerCell = row.getCell(6);
		writerCell.setCellValue(angajat.getPersoana().getCnp());
		// nr contract de munca
		writerCell = row.getCell(9);
		writerCell.setCellValue(contract.getNr());
		// data angajarii
		writerCell = row.getCell(12);
		writerCell.setCellValue(contract.getData());
		// data incetarii activ.
		writerCell = row.getCell(15);
		writerCell.setCellValue(contract.getUltimazilucru());

		// get styles
		CellStyle normal = sheet.getRow(8).getCell(3).getCellStyle();
		CellStyle total = sheet.getRow(10).getCell(3).getCellStyle();

		// ! TABLE CONTENT
		Row row1, row2;
		int index = 0;
		for (RealizariRetineri rr : realizariRetineri) {
			List<CM> concediiMedicale = cmService.getCMInLunaAnul(rr.getLuna(), an, contract.getId());
			row1 = sheet.createRow(8);
			row2 = sheet.createRow(9);

			// NZ
			writerCell = row1.createCell(1);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(8);
			// NO
			writerCell = row2.createCell(1);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(contract.getNormalucru());
			
			// Salariu de incadrare
			writerCell = row.createCell(2);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(contract.getSalariutarifar());
			
			// ZL
			writerCell = row1.createCell(3);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getZilelucrate());
			// CO
			writerCell = row2.createCell(3);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getZileco());

			// CM
			int zilecmfnuass = cmService.getZilecmFNUASS(concediiMedicale);
			int zilecmfaambp = cmService.getZilecmFAAMBP(concediiMedicale);
			writerCell = row1.createCell(4);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getZilecm() - zilecmfnuass - zilecmfaambp);
			// FNUASS
			writerCell = row2.createCell(4);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(zilecmfnuass);

			// SPL
			writerCell = row1.createCell(5);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getNroresuplimentare());
			// CFS
			writerCell = row2.createCell(5);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getZilecfp());

			// Salariu realizat
			writerCell = row1.createCell(6);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getSalariurealizat());
			// Valoare CO
			writerCell = row2.createCell(6);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getValco());

			// CM Societate
			int valcmfnuass = cmService.getValcmFNUASS(concediiMedicale);
			int valcmfaambp = cmService.getValcmFAAMBP(concediiMedicale);
			writerCell = row1.createCell(7);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getValcm() - valcmfnuass - valcmfaambp);
			// CM din FNUASS
			writerCell = row2.createCell(7);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(valcmfnuass);

			// ore supl.
			writerCell = row1.createCell(8);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getTotaloresuplimentare());
			// alte drepturi
			writerCell = row2.createCell(8);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(0);

			// Total sporuri
			writerCell = row1.createCell(9);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(0);
			// total prime
			writerCell = row2.createCell(9);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getPrimabruta());

			// Venit brut
			writerCell = row1.createCell(10);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getVenitbrut());
			// Venit net
			writerCell = row2.createCell(10);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getVenitnet());

			// Somaj
			writerCell = row1.createCell(11);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(0);
			// CAS
			writerCell = row2.createCell(11);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getCas());

			// CASS
			writerCell = row1.createCell(12);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getCass());
			// CASS FNUASS
			writerCell = row2.createCell(12);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(valcmfnuass * parametriiSalariu.getCass());

			// Ded. pers
			writerCell = row1.createCell(13);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getDeducere());
			// Impozit
			writerCell = row2.createCell(13);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getImpozit());

			// alte retineri
			writerCell = row1.createCell(14);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getRetineri().getAlteRetineri());
			// Alte venituri
			writerCell = row2.createCell(14);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(0);

			// Avans
			writerCell = row1.createCell(15);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getRetineri().getAvansnet());
			// Rest plata
			writerCell = row2.createCell(15);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getRestplata());

			index++;
		}

		 // TOTAL

		/* ------ ENDING ------ **/
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		evaluator.evaluateAll();
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String newFileLocation = String.format("%s/downloads/%d/Adeverinta de venit - %s - %d.xlsx", homeLocation, userID, persoana.getNumeIntreg(), an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}
}
