package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
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
import net.guides.springboot2.crud.model.ContBancar;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.RealizariRetineriRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class MTAService {
	@Autowired
	private AngajatService angajatService;
	@Autowired
	private RealizariRetineriRepository realizariRetineriRepository;
	@Autowired
	private SocietateRepository societateRepository;
	@Autowired
	private ContBancarService contBancarService;

	@Autowired
	private ZileService zileService;

	private String homeLocation = "src/main/java/net/guides/springboot2/crud/";

	private String faraDiacritice(String str) {
		str = str.replace("ă", "a");
		str = str.replace("â", "a");
		str = str.replace("Â", "A");
		str = str.replace("Ă", "A");
		str = str.replace("ț", "t");
		str = str.replace("Ț", "T");
		str = str.replace("ș", "s");
		str = str.replace("Ș", "S");
		str = str.replace("î", "i");
		str = str.replace("Î", "I");

		return str;
	}

	public boolean createMTA_Raiffeisen(int idsocietate, int luna, int an, int userID, int idContBancar)
			throws IOException, ResourceNotFoundException {

		Societate societate = societateRepository.findById(idsocietate)
				.orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));

		ContBancar contSocietate = contBancarService.findById(idContBancar);

		// daca soc are doar un cont ia toti angajatii
		List<Angajat> angajati = angajatService.getAngajatiContracteValide(idsocietate, an, luna);
		angajati.removeIf(ang -> ang.getContract().getContbancar().getIban().isEmpty());
		// daca soc are mai multe conturi, ia-i doar pe cei cu cont la raiff == scoate-i pe cei care nu au raiff
		if (societate.getContbancar().size() > 1)
			angajati.removeIf(ang -> !ang.getContract().getContbancar().getNumebanca().toLowerCase().contains("raiff"));

		if (angajati.isEmpty())
			throw new ResourceNotFoundException("Nu există angajați cu cont la Raiffeisen Bank");

		// * READ THE FILE
		String templateLocation = homeLocation + "/templates";
		FileInputStream file = new FileInputStream(new File(templateLocation, "MTA_Raiffeisen.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet sheet = workbook.getSheetAt(0);

		Font arial10 = workbook.createFont();
		arial10.setFontHeightInPoints((short) 10);
		arial10.setFontName("Arial");
		CellStyle style = workbook.createCellStyle();
		style.setFont(arial10);

		CellStyle ibanSocietate = workbook.createCellStyle();
		ibanSocietate.setFont(arial10);
		ibanSocietate.setBorderRight(BorderStyle.THIN);

		// * initialize writerCell;
		Cell writerCell;

		// * iban-ul platitorului
		writerCell = sheet.getRow(1).getCell(1);
		writerCell.setCellStyle(ibanSocietate);
		writerCell.setCellValue(contSocietate.getIban());

		int nrAngajat = 1;
		for (Angajat angajat : angajati) {
			Persoana persoana = angajat.getPersoana();
			Contract contract = angajat.getContract();
			if (contract.getContbancar().getIban().isEmpty())
				continue;

			RealizariRetineri realizariRetineri = realizariRetineriRepository.findByLunaAndAnAndContract_Id(luna, an,
					contract.getId());
			if (realizariRetineri == null) {
				workbook.close();
				throw new ResourceNotFoundException(
						persoana.getNumeIntreg() + " nu are salariul calculat pe " + luna + "/" + an);
			}

			int rowNr = 2 + nrAngajat;
			Row row = sheet.createRow(rowNr);

			// * Descriere tranzactie cont beneficiar
			writerCell = row.createCell(0);
			writerCell.setCellStyle(style);
			writerCell.setCellValue("Plata salariu");

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
			writerCell.setCellValue(faraDiacritice(persoana.getNumeIntreg()));

			// * Nr. evidenta plata
			writerCell = row.createCell(5);
			writerCell.setCellStyle(style);
			writerCell.setCellValue(nrAngajat);

			nrAngajat++;
		}

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
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
		String newFileLocation = String.format("%s/downloads/%d/MTA %s - %s - %s %d.xlsx", homeLocation, userID,
				contSocietate.getNumebanca(), societate.getNume(), lunaNume, an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	} // createMTA

	public boolean createMTA_Unicredit(int idsocietate, int luna, int an, int userID, int idContBancar)
			throws IOException, ResourceNotFoundException {

		Societate societate = societateRepository.findById(idsocietate)
				.orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
		// faster than java filtering??
		ContBancar contSocietate = contBancarService.findById(idContBancar);

		// daca soc are doar un cont ia toti angajatii
		List<Angajat> angajati = angajatService.getAngajatiContracteValide(idsocietate, an, luna);
		angajati.removeIf(ang -> ang.getContract().getContbancar().getIban().isEmpty());
		// daca soc are mai multe conturi, scoate-i pe cei care au raiff
		if (societate.getContbancar().size() > 1)
			angajati.removeIf(ang -> ang.getContract().getContbancar().getNumebanca().toLowerCase().contains("raiff"));

		PrintWriter pw = null;
		try {
			Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
			String lunaNume = zileService.getNumeLunaByNr(luna);
			String newFileLocation = String.format("%s/downloads/%d/MTA %s - %s - %s %d.csv", homeLocation, userID,
					contSocietate.getNumebanca(), societate.getNume(), lunaNume, an);
			File output = new File(newFileLocation);
			output.createNewFile();
			pw = new PrintWriter(newFileLocation);
			// pw = new BufferedWriter(new OutputStreamWriter(new
			// FileOutputStream(newFileLocation), StandardCharsets.UTF_8));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int nrAngajat = 1;
		StringBuilder csv = new StringBuilder();
		for (Angajat angajat : angajati) {
			Persoana persoana = angajat.getPersoana();
			Contract contract = angajat.getContract();
			if (contract.getContbancar() == null)
				continue;

			RealizariRetineri realizariRetineri = realizariRetineriRepository.findByLunaAndAnAndContract_Id(luna, an,
					contract.getId());
			if (realizariRetineri == null) {
				pw.close();
				throw new ResourceNotFoundException(
						persoana.getNumeIntreg() + " nu are salariul calculat pe " + luna + "/" + an);
			}

			StringBuilder row = new StringBuilder();
			// * index
			row.append(String.valueOf(nrAngajat)).append(',');

			// * Amount + RON
			row.append(realizariRetineri.getRestplata().toString()).append(",RON,");

			// * Payer Account IBAN
			row.append(contSocietate.getIban()).append(',');

			// * Payer name
			row.append(faraDiacritice(persoana.getNumeIntreg())).append(',');

			// * Payee address 1
			// row.append(faraDiacritice(persoana.getAdresa().getAdresaCompleta())).append(',');
			row.append(',');

			// * Payee address 2
			row.append(',');

			// * Payee CUI
			row.append(persoana.getCnp()).append(',');

			// * Payee Account IBAN
			row.append(contract.getContbancar().getIban()).append(',');

			// * Details 1
			row.append("Plata salariu,");

			// * Details 2
			row.append(',');

			// * Details 3
			row.append(',');

			// * Processing date [YYMMDD]
			LocalDate today = LocalDate.now();
			String yymmdd = String.valueOf(an).substring(2);
			yymmdd += String.format("%02d", luna);
			yymmdd += String.format("%02d", today.getDayOfMonth());
			row.append(yymmdd).append(",NORMAL\n");
			csv.append(row);
			nrAngajat++;
		}

		pw.write(csv.toString());
		pw.close();

		return true;
	} // createMTA_Unicredit
}
