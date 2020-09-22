package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
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
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.AdresaRepository;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.PersoanaRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class StatSalariiService {
	@Autowired
	private RealizariRetineriService realizariRetineriService;
	@Autowired
	private RetineriService retineriService;
	@Autowired
	private ZileService zileService;

	@Autowired
	private PersoanaRepository persoanaRepository;
	@Autowired
	private AngajatRepository angajatRepository;
	@Autowired
	private SocietateRepository societateRepository;
	@Autowired
	private AdresaRepository adresaRepository;

	//! WRAP IN TRY-CATCH BLOCK
	public void createStatSalarii(int luna, int an, int idsocietate) throws IOException, ResourceNotFoundException {
		Societate societate = societateRepository.findById((long) idsocietate)
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + idsocietate));
		Adresa adresaSocietate = adresaRepository.findById(societate.getIdadresa())
				.orElseThrow(() -> new ResourceNotFoundException("Adresa not found for this societate :: " + societate.getNume()));

		// List<Angajat> angajati = angajatRepository.findByIdsocietateAndIdcontractNotNull(idsocietate);
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		String downloadsLocation = "C:\\Users\\florin\\code\\webapp-spring\\server\\src\\main\\java\\net\\guides\\springboot2\\crud\\downloads";
		String statTemplateLocation = downloadsLocation + "\\templates\\StatSalarii.xlsx";
		

		FileInputStream file = new FileInputStream(new File(statTemplateLocation));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet stat = workbook.getSheetAt(0);

		//* write date societate
		Cell writerCell = stat.getRow(0).getCell(0);
		writerCell.setCellValue(societate.getNume()); // nume soc
		writerCell = stat.getRow(1).getCell(0);
		writerCell.setCellValue("CUI: " + societate.getCif()); // cif
		writerCell = stat.getRow(2).getCell(0);
		writerCell.setCellValue("Nr. Reg. Com.: " + societate.getRegcom()); // nr reg com
		writerCell = stat.getRow(3).getCell(0);
		writerCell.setCellValue("Strada: " + adresaSocietate.getAdresa());
		writerCell = stat.getRow(4).getCell(0);
		writerCell.setCellValue(adresaSocietate.getJudet() + ", " + adresaSocietate.getLocalitate());

		// write luna, an
		writerCell = stat.getRow(4).getCell(11);
		String lunaNume = zileService.getNumeLunaByNr(luna);
		writerCell.setCellValue("- " + lunaNume + " " + an + " -");

		String newFileLocation = downloadsLocation + "\\Stat Salarii - " + societate.getNume() + " - " + lunaNume + ' ' + an + ".xlsx";
		
		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();
	}
}
