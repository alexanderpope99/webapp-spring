package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Societate;

@Service
public class FluturasSalariuService {

	@Autowired
	private AngajatService angajatService;

	@Autowired
	private ZileService zileService;

	@Autowired
	private RealizariRetineriService realizariRetineriService;

	private String homeLocation = "src/main/java/net/guides/springboot2/crud/";

	public boolean createFluturas(int luna, int an, int idangajat, int userID) throws IOException, ResourceNotFoundException {

		Angajat angajat = angajatService.findById(idangajat);
		Societate societate = angajat.getSocietate();
		RealizariRetineri rr = realizariRetineriService.saveOrGetRealizariRetineri(luna, an, angajat.getContract().getId());

		String sheetTemplateLocation = homeLocation + "/templates";

		FileInputStream file = new FileInputStream(new File(sheetTemplateLocation, "Fluturas.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet sheet = workbook.getSheetAt(0);

		// nume societate
		Cell writerCell = sheet.getRow(2).getCell(0);
		writerCell.setCellValue(societate.getNume()); // nume soc
		// data creeari fisierului
		writerCell = sheet.getRow(2).getCell(8);
		LocalDate today = LocalDate.now();
		writerCell.setCellValue(String.format("%d/%d/%d", today.getDayOfMonth(), today.getMonthValue(), today.getYear()));

		// nume angajat
		writerCell = sheet.getRow(3).getCell(0);
		writerCell.setCellValue(angajat.getPersoana().getNumeIntreg());
		// luna nume
		writerCell = sheet.getRow(3).getCell(8);
		writerCell.setCellValue(zileService.getNumeLunaByNr(luna) + ' ' + an);

		// salariu de incadrare (din contract)
		writerCell = sheet.getRow(4).getCell(3);
		writerCell.setCellValue(angajat.getContract().getSalariutarifar());
		// total deduceri
		writerCell = sheet.getRow(4).getCell(8);
		writerCell.setCellValue(rr.getDeducere());

		// valoare ore lucrate (nrOre, valoareOre)
		writerCell = sheet.getRow(5).getCell(1);
		writerCell.setCellValue(rr.getOrelucrate());
		writerCell = sheet.getRow(5).getCell(3);
		writerCell.setCellValue(rr.getSalariurealizat() + rr.getTotaloresuplimentare());

		// concediu odihna
		writerCell = sheet.getRow(6).getCell(1);
		writerCell.setCellValue(rr.getZileco());
		writerCell = sheet.getRow(6).getCell(3);
		writerCell.setCellValue(rr.getValco());

		// concediu medical
		writerCell = sheet.getRow(7).getCell(1);
		writerCell.setCellValue(rr.getZilecm());
		writerCell = sheet.getRow(7).getCell(3);
		writerCell.setCellValue(rr.getValcm());

		// total prime
		writerCell = sheet.getRow(8).getCell(3);
		writerCell.setCellValue(rr.getPrimabruta());

		// alte drepturi
		writerCell = sheet.getRow(9).getCell(3);
		writerCell.setCellValue(0);

		// venit brut
		writerCell = sheet.getRow(10).getCell(3);
		writerCell.setCellValue(rr.getVenitbrut());

		// cas 25%
		writerCell = sheet.getRow(5).getCell(8);
		writerCell.setCellValue(rr.getCas());

		// sanatate 10%
		writerCell = sheet.getRow(6).getCell(8);
		writerCell.setCellValue(rr.getCass());

		// impozit
		writerCell = sheet.getRow(7).getCell(8);
		writerCell.setCellValue(rr.getImpozit());

		// alte retineri 
		writerCell = sheet.getRow(8).getCell(8);
		writerCell.setCellValue(rr.getRetineri().getAlteRetineri());

		// avans
		writerCell = sheet.getRow(9).getCell(8);
		writerCell.setCellValue(rr.getRetineri().getAvansnet());

		// rest plata
		writerCell = sheet.getRow(10).getCell(8);
		writerCell.setCellValue(rr.getRestplata());

		/* ------ ENDING ------ **/
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String newFileLocation = String.format("%s/downloads/%d/Fluturas - %s - %s %d.xlsx", homeLocation, userID, angajat.getPersoana().getNumeIntreg(), zileService.getNumeLunaByNr(luna), an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}
}
