package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.YearMonth;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Adresa;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.model.Societate;

@Service
public class AdeverintaVenitService {

	@Autowired
	private AngajatService angajatService;

	@Autowired
	private ZileService zileService;
	
	private String homeLocation = "src/main/java/net/guides/springboot2/crud/";

	public boolean createAdeverintaVenit(int lunaDela, int lunaPanala, int an, int idangajat, int userID)
			throws IOException, ResourceNotFoundException {

		Angajat angajat = angajatService.findById(idangajat);
		Persoana persoana = angajat.getPersoana();
		Societate societate = angajat.getSocietate();

		String sheetTemplateLocation = homeLocation + "/templates";

		FileInputStream file = new FileInputStream(new File(sheetTemplateLocation, "AdeverintaDeVenit.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet sheet = workbook.getSheetAt(0);

		// * date societate:
		Cell writerCell = sheet.getRow(0).getCell(0);
		writerCell.setCellValue(societate.getNume()); // nume soc
		writerCell = sheet.getRow(1).getCell(0);
		writerCell.setCellValue(societate.getAdresa().toString("alj")); // adresa
		writerCell = sheet.getRow(2).getCell(1); // tel
		writerCell.setCellValue(societate.getTelefon());
		writerCell = sheet.getRow(2).getCell(2); // fax
		writerCell.setCellValue("Fax: " + societate.getFax());
		writerCell = sheet.getRow(3).getCell(1); // CUI/CIF
		writerCell.setCellValue(societate.getCif());

		// TITLU
		writerCell = sheet.getRow(7).getCell(0);
		writerCell.setCellValue("PE ANUL " + an);

		// text
		writerCell = sheet.getRow(9).getCell(0);
		writerCell.setCellValue("      Prin prezenta se atesta faptul ca dl./dna "+persoana.getNumeIntreg()+" domiciliat(a)");
		
		Adresa adrAngajat = persoana.getAdresa();
		String adrStr, judStr;
		if(adrAngajat.isCapitala()) {
			adrStr = adrAngajat.getLocalitate() + ", " + adrAngajat.getAdresa() + ", " + adrAngajat.getJudet();
			judStr = adrAngajat.getLocalitate();
		} else {
			adrStr = adrAngajat.getLocalitate() + ", " + adrAngajat.getAdresa();
			judStr = adrAngajat.getJudet();
		}
		writerCell = sheet.getRow(10).getCell(0);
		writerCell.setCellValue("in " + adrStr);

		writerCell = sheet.getRow(11).getCell(0);
		writerCell.setCellValue(judStr + ", posesor al BI/CI seria" + persoana.getActidentitate().getSerie() + ", nr. " + persoana.getActidentitate().getNumar() + ", CNP " + persoana.getActidentitate().getCnp() + " are calitatea de");
		writerCell = sheet.getRow(12).getCell(0);
		writerCell.setCellValue("salariat incepant cu data de " + angajat.getContract().getDataincepere().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " si a inregistrat in anul " + an + " urmatoarele venituri");

		writerCell = sheet.getRow(16).getCell(0);
		YearMonth yearMonthObject = YearMonth.of(an, lunaDela);
		int daysInMonth = yearMonthObject.lengthOfMonth();
		writerCell.setCellValue(String.format("Perioada: 01/%d/%d - %d/%d/%d", lunaDela, an, daysInMonth,lunaPanala, an));


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
