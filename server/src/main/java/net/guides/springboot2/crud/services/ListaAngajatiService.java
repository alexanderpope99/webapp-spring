package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderExtent;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class ListaAngajatiService {

	@Autowired
	SocietateRepository societateRepository;

	@Autowired
	AngajatRepository angajatRepository;

	@Autowired
	ZileService zileService;

	@Autowired
	RealizariRetineriService realizariRetineriService;
	
	private String homeLocation = "src/main/java/net/guides/springboot2/crud/";

	public boolean createListaAngajati(int luna, int an, int idsocietate, int userID) throws IOException, ResourceNotFoundException {

		Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
		List<Angajat> angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(idsocietate);

		String listaAngajatiTemplateLocation = homeLocation + "/templates";

		FileInputStream file = new FileInputStream(new File(listaAngajatiTemplateLocation, "Lista Angajati.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet listaAngajati = workbook.getSheetAt(0);

		Cell writerCell = listaAngajati.getRow(0).getCell(0);
		writerCell.setCellValue(societate.getNume()); // nume soc
		writerCell = listaAngajati.getRow(1).getCell(0);
		writerCell.setCellValue("CUI: " + societate.getCif()); // cif
		writerCell = listaAngajati.getRow(2).getCell(0);
		writerCell.setCellValue("Str: " + societate.getAdresa().getAdresa()); // adresa
		writerCell = listaAngajati.getRow(4).getCell(0);
		writerCell.setCellValue(societate.getAdresa().getLocalitate()+societate.getAdresa().getJudet());//judet,localitate

		writerCell = listaAngajati.getRow(6).getCell(7);
		String lunaNume = zileService.getNumeLunaByNr(luna);
		writerCell.setCellValue("- " + lunaNume + " " + an + " -");

		writerCell = listaAngajati.getRow(1).getCell(14);
		writerCell.setCellValue(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

		CellStyle centered = workbook.createCellStyle();
		CellStyle left = workbook.createCellStyle();
		CellStyle right = workbook.createCellStyle();
		centered.setAlignment(HorizontalAlignment.CENTER);
		left.setAlignment(HorizontalAlignment.LEFT);
		right.setAlignment(HorizontalAlignment.RIGHT);
		
		int nrCrt = 0;
		for (Angajat ang : angajati) {
			int rowNr = 10 + nrCrt;
			Contract contract = ang.getContract();
			RealizariRetineri realizariRetineri = realizariRetineriService.saveOrGetRealizariRetineri(luna, an, contract.getId());
			Row row = listaAngajati.createRow(rowNr);
			writerCell = row.createCell(0);
			writerCell.setCellValue(nrCrt + 1);
			writerCell = row.createCell(1);
			writerCell.setCellStyle(left);
			writerCell.setCellValue(ang.getPersoana().getNume() + " " + ang.getPersoana().getPrenume());
			writerCell = row.createCell(6);
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(ang.getContract().getFunctie());
			writerCell = row.createCell(8);
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(ang.getPersoana().getCnp());
			writerCell = row.createCell(10);
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(ang.getContract().getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			writerCell = row.createCell(12);
			writerCell.setCellStyle(right);
			writerCell.setCellValue(ang.getContract().getSalariutarifar());
			writerCell = row.createCell(14);
			writerCell.setCellStyle(right);
			writerCell.setCellValue(realizariRetineri.getVenitnet());

			listaAngajati.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 1, 5));
			listaAngajati.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 6, 7));
			listaAngajati.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 8, 9));
			listaAngajati.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 10, 11));
			listaAngajati.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 12, 13));
			listaAngajati.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 14, 15));
			PropertyTemplate allCellsBordered = new PropertyTemplate();
			String cellRange = "$A$"+(rowNr+1)+":$P$" + (rowNr+1);
			allCellsBordered.drawBorders(CellRangeAddress.valueOf(cellRange), BorderStyle.MEDIUM, BorderExtent.ALL);
			allCellsBordered.applyBorders(listaAngajati);
			nrCrt++;
		}

		Row row=listaAngajati.createRow(10+nrCrt);
		row.setHeight((short)100);
		row=listaAngajati.createRow(10+nrCrt+1);
		writerCell=row.createCell(0);
		Font font= workbook.createFont();
		font.setBold(true);
		CellStyle leftBold = workbook.createCellStyle();
		leftBold.setAlignment(HorizontalAlignment.LEFT);
		leftBold.setFont(font);
		writerCell.setCellStyle(leftBold);
		writerCell.setCellValue("Total salariati");
		writerCell=row.createCell(4);
		writerCell.setCellStyle(right);
		writerCell.setCellValue(nrCrt);
		
		listaAngajati.addMergedRegion(new CellRangeAddress(10+nrCrt+1, 10+nrCrt+1, 0, 3));
		listaAngajati.addMergedRegion(new CellRangeAddress(10+nrCrt+1, 10+nrCrt+1, 4, 5));
		PropertyTemplate allCellsBordered = new PropertyTemplate();
		String cellRange = "$A$"+(10+nrCrt+2)+":$F$" + (10+nrCrt+2);
		allCellsBordered.drawBorders(CellRangeAddress.valueOf(cellRange), BorderStyle.MEDIUM, BorderExtent.ALL);
		allCellsBordered.applyBorders(listaAngajati);



		/* ------ ENDING ------ **/
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String newFileLocation = String.format("%s/downloads/%d/Lista Angajati - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}
}
