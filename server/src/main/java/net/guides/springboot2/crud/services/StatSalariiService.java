package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Adresa;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Retineri;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.AdresaRepository;
import net.guides.springboot2.crud.repository.ContractRepository;
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
	private SocietateRepository societateRepository;
	@Autowired
	private AdresaRepository adresaRepository;
	@Autowired
	private ContractRepository contractRepository;

	private void setRegionBorder(CellRangeAddress region, Sheet sheet) {
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
    }

	//! WRAP IN TRY-CATCH BLOCK
	public void createStatSalarii(int luna, int an, int idsocietate) throws IOException, ResourceNotFoundException {
		try {
		Societate societate = societateRepository.findById((long) idsocietate)
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + idsocietate));
		Adresa adresaSocietate = adresaRepository.findById(societate.getIdadresa())
				.orElseThrow(() -> new ResourceNotFoundException("Adresa not found for this societate :: " + societate.getNume()));

		// List<Angajat> angajati = angajatRepository.findByIdsocietateAndIdcontractNotNull(idsocietate);
		List<Persoana> persoane = persoanaRepository.getPersoanaByIdsocietateWithContract(idsocietate);

		// File currDir = new File(".");
		// String path = currDir.getAbsolutePath();
		// System.out.println(path);
		// String downloadsLocation = 
		// 	path.substring(0, path.length() - 1) + "src\\main\\java\\net\\guides\\springboot2\\crud\\downloads";
		String downloadsLocation = "D:\\code\\webapp-spring\\server\\src\\main\\java\\net\\guides\\springboot2\\crud\\downloads\\";
		String statTemplateLocation = downloadsLocation + "\\templates";
		
		FileInputStream file = new FileInputStream(new File(statTemplateLocation, "StatSalarii.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet stat = workbook.getSheetAt(0);
		
		//* create styles
		CellStyle salariuStyle = workbook.createCellStyle();
		CellStyle functieStyle = workbook.createCellStyle();
		CellStyle nrContractStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short)7);
		functieStyle.setFont(font);
		functieStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		DataFormat format = workbook.createDataFormat();
		salariuStyle.setDataFormat(format.getFormat("#,##0"));
		
		nrContractStyle.setAlignment(HorizontalAlignment.RIGHT);
		
		Cell salariuWriter = stat.getRow(0).getCell(0);
		Cell functieWriter = stat.getRow(0).getCell(0);
		
		//* write date societate
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
		
		//* write luna, an
		writerCell = stat.getRow(4).getCell(11);
		String lunaNume = zileService.getNumeLunaByNr(luna);
		writerCell.setCellValue("- " + lunaNume + " " + an + " -");
		
		
		//* write angajati:
		int nrAngajat = 0;
		for(Persoana persoana : persoane) {
			int rowNr = 14 + nrAngajat * 3;
			Row row1 = stat.createRow(rowNr);
			Row row2 = stat.createRow(rowNr+1);
			Row row3 = stat.createRow(rowNr+2);
		
			//* 1. get contract + stat(luna, an, idcontract);
			Contract contract = contractRepository.findByIdPersoana(persoana.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Contract not found for this idpersoana :: " + persoana.getId()));
		
			RealizariRetineri realizariRetineri = realizariRetineriService.saveRealizariRetineri(luna, an, contract.getId());
			Retineri retineri = retineriService.getRetinereByIdstat(realizariRetineri.getId());
				
			//* 2. set styles, merged cells etc
			stat.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 1, 2));
		
			
			//* 3. insert data:
			writerCell = row1.createCell(0);
			writerCell.setCellValue(nrAngajat+1); // indexing
		
			//* nume, functie, cnp
			writerCell = row1.createCell(1); // nume complet
			writerCell.setCellValue(persoana.getNume() + " " + persoana.getPrenume());
			functieWriter = row2.createCell(1); // functie
			functieWriter.setCellStyle(functieStyle);
			functieWriter.setCellValue(contract.getFunctie());
			writerCell = row3.createCell(1); // cnp
			writerCell.setCellValue(persoana.getCnp());
		
			writerCell = row3.createCell(2); // nr contract
			writerCell.setCellStyle(nrContractStyle);
			writerCell.setCellValue(contract.getNr());
		
			//* SALARIU
			salariuWriter = row1.createCell(4); // salariu din contract
			salariuWriter.setCellStyle(salariuStyle);
			salariuWriter.setCellValue(contract.getSalariutarifar());
			salariuWriter = row2.createCell(4); // sume incluse ...
			salariuWriter.setCellValue(0);
			salariuWriter = row3.createCell(4); // spor weekend
			salariuWriter.setCellValue(0);
			salariuWriter = row3.createCell(3); // spor vechime
			salariuWriter.setCellValue(0);
		
			//* ZILE
			writerCell = row1.createCell(5); // NZ
			writerCell.setCellValue(8);
			writerCell = row2.createCell(5); // NO
			writerCell.setCellValue(contract.getNormalucru());
			writerCell = row3.createCell(5); // SPL = ore suplimentare
			writerCell.setCellValue(realizariRetineri.getNroresuplimentare());
		
			// writerCell = row1.getCell()
			//* set borders
			String cellRange="$A$" + (15+nrAngajat*3) + ":$U$" + (17+nrAngajat*3);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			nrAngajat++;
		}

		String newFileLocation = downloadsLocation + "\\Stat Salarii - " + societate.getNume() + " - " + lunaNume + ' ' + an + ".xlsx";
		
		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();
		}
		catch (IOException e) {
            e.printStackTrace();
		}
	}
}
