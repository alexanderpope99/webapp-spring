package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Societate;

@Service
public class AdeverintaVenitService {

	@Autowired
	private AngajatService angajatService;

	@Autowired
	private ZileService zileService;

	@Autowired
	private RealizariRetineriService realizariRetineriService;

	private String homeLocation = "src/main/java/net/guides/springboot2/crud/";

	public boolean createAdeverintaVenit(int lunaDela, int lunaPanala, int an, int idangajat, int userID) throws IOException, ResourceNotFoundException {

		Angajat angajat = angajatService.findById(idangajat);
		Persoana persoana = angajat.getPersoana();
		Societate societate = angajat.getSocietate();
		List<RealizariRetineri> realizariRetineri = realizariRetineriService.saveOrGetFromTo(lunaDela, lunaPanala, an, angajat.getContract().getId());

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
		writerCell.setCellValue("      Prin prezenta se atesta faptul ca dl./dna " + persoana.getNumeIntreg() + " domiciliat(a)");

		Adresa adrAngajat = persoana.getAdresa();
		String adrStr, judStr;
		if (adrAngajat.isCapitala()) {
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
		writerCell.setCellValue("salariat incepand cu data de " + angajat.getContract().getDataincepere().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " si a inregistrat in anul " + an + " urmatoarele venituri");

		writerCell = sheet.getRow(18).getCell(0);
		YearMonth yearMonthObject = YearMonth.of(an, lunaPanala);
		int daysInMonth = yearMonthObject.lengthOfMonth();
		writerCell.setCellValue(String.format("Perioada: 01/%d/%d - %d/%d/%d", lunaDela, an, daysInMonth, lunaPanala, an));

		DataFormat format = workbook.createDataFormat();
		CellStyle total = sheet.getRow(24).getCell(1).getCellStyle();
		total.setDataFormat(format.getFormat("#,##0"));
		total.setAlignment(HorizontalAlignment.RIGHT);
		CellStyle normal = sheet.getRow(23).getCell(2).getCellStyle();
		// normal.setDataFormat(format.getFormat("#,##0"));
		// normal.setAlignment(HorizontalAlignment.RIGHT);

		// TABLE CONTENTS
		int index = 0;
		for (RealizariRetineri rr : realizariRetineri) {
			Row row = sheet.createRow(23 + index);

			// col Luna
			writerCell = row.createCell(1);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(zileService.getNumeLunaByNr(rr.getLuna()) + ' ' + an);

			// col Venit brut
			writerCell = row.createCell(2);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getTotaldrepturi() + rr.getValoaretichete());

			// col Venit baza de calcul
			writerCell = row.createCell(3);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getBazaimpozit());

			// Impozit lunar calculat si retinut
			writerCell = row.createCell(4);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getImpozit());

			// Venit net
			writerCell = row.createCell(5);
			writerCell.setCellStyle(normal);
			writerCell.setCellValue(rr.getVenitnet());

			index++;
		}

		// TOTAL
		Row totalRow = sheet.createRow(24 + index - 1);
		writerCell = totalRow.createCell(1);
		writerCell.setCellStyle(total);
		writerCell.setCellValue("TOTAL");

		// venit brut total
		writerCell = totalRow.createCell(2);
		writerCell.setCellStyle(total);
		writerCell.setCellFormula("SUM(C24:C" + (24 + index - 1) + ")");

		// venit baza de calcul total
		writerCell = totalRow.createCell(3);
		writerCell.setCellStyle(total);
		writerCell.setCellFormula("SUM(D24:D" + (24 + index - 1) + ")");

		// impozit lunar calculat si retinut total
		writerCell = totalRow.createCell(4);
		writerCell.setCellStyle(total);
		writerCell.setCellFormula("SUM(E24:E" + (24 + index - 1) + ")");

		// venit net total
		writerCell = totalRow.createCell(5);
		writerCell.setCellStyle(total);
		writerCell.setCellFormula("SUM(F24:F" + (24 + index - 1) + ")");

		// nume functia semnatura
		writerCell = sheet.createRow(24+index+3).createCell(0);
		writerCell.setCellValue("Nume si prenume:");

		writerCell = sheet.createRow(24+index+4).createCell(0);
		writerCell.setCellValue("Functia:");

		writerCell = sheet.createRow(24+index+5).createCell(0);
		writerCell.setCellValue("Semnatura si stampila:");

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
