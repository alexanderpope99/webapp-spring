package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderExtent;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.RealizariRetineriRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class MTAService {
	@Autowired
	private AngajatRepository angajatRepository;
	@Autowired
	private RealizariRetineriRepository realizariRetineriRepository;
	@Autowired
	private SocietateRepository societateRepository;

	@Autowired
	private ZileService zileService;

	private String homeLocation = "src/main/java/net/guides/springboot2/crud/";

	public boolean createMTA(int idsocietate, int luna, int an, int userID) throws IOException,
			ResourceNotFoundException {

		// * READ THE FILE
		String templateLocation = homeLocation + "/templates";
		FileInputStream file = new FileInputStream(new File(templateLocation, "PlatiSalariiMTA.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet sheet = workbook.getSheetAt(0);

		List<Angajat> angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNull(idsocietate);
		Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + idsocietate));

		Font arial10 = workbook.createFont();
		arial10.setFontHeightInPoints((short) 10);
		arial10.setFontName("Arial");
		CellStyle style = workbook.createCellStyle();
		style.setFont(arial10);

		// * initialize writerCell;
		Cell writerCell;

		int nrAngajat = 1;
		for (Angajat angajat : angajati) {
			Persoana persoana = angajat.getPersoana();
			Contract contract = angajat.getContract();
			if (contract.getContbancar() == null)
				continue;

			RealizariRetineri realizariRetineri = realizariRetineriRepository.findByLunaAndAnAndContract_Id(luna, an,
					contract.getId());

			int rowNr = 2 + nrAngajat;
			Row row = sheet.createRow(rowNr);

			// * Descriere tranzactie cont beneficiar
			writerCell = row.createCell(0);
			writerCell.setCellStyle(style);
			writerCell.setCellValue("Plată salariu");

			// * Cont beneficiar
			writerCell = row.createCell(1);
			writerCell.setCellStyle(style);
			writerCell.setCellValue(contract.getContbancar().getIban());

			// * Suma
			writerCell = row.createCell(2);
			writerCell.setCellStyle(style);
			writerCell.setCellValue(realizariRetineri.getRestplata());

			// * CNP/CUI beneficiar
			writerCell = row.createCell(3);
			writerCell.setCellStyle(style);
			writerCell.setCellValue(persoana.getCnp());

			// * Nume beneficiar (nume + prenume)
			writerCell = row.createCell(4);
			writerCell.setCellStyle(style);
			writerCell.setCellValue(persoana.getNume() + " " + persoana.getPrenume());

			// * Nr. evidenta plata
			writerCell = row.createCell(5);
			writerCell.setCellStyle(style);
			writerCell.setCellValue(nrAngajat);

			nrAngajat++;
		}

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		// sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);

		// * set borders
		PropertyTemplate allCellsBordered = new PropertyTemplate();
		String cellRange = "$A$4:$F$" + (2 + nrAngajat);
		allCellsBordered.drawBorders(CellRangeAddress.valueOf(cellRange), BorderStyle.THIN, BorderExtent.ALL);
		allCellsBordered.applyBorders(sheet);

		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String lunaNume = zileService.getNumeLunaByNr(luna);
		String newFileLocation = String.format("%s/downloads/%d/FisierMTA - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);
		// String newFileLocation = String.format("%s/downloads/%d/FisierMTA - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}
}
