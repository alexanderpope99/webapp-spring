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
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
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
	private COService coService;
	@Autowired
	private CMService cmService; 

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
	
	private String sumFormula(char colName, int row, int nrAngajat) {
		StringBuilder formula = new StringBuilder();
		formula.append(colName).append(row);
		
		for(int i = 1; i < nrAngajat; i++) {
			formula.append('+').append(colName).append(row+i*3);
		}

		return formula.toString();
	}

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
		CellStyle centered = workbook.createCellStyle();
		centered.setAlignment(HorizontalAlignment.CENTER);
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

			long idcontract = contract.getId();
		
			RealizariRetineri realizariRetineri = realizariRetineriService.saveRealizariRetineri(luna, an, idcontract);
			Retineri retineri = retineriService.getRetinereByIdstat(realizariRetineri.getId());
				
			//* 3. insert data:
			writerCell = row1.createCell(0);
			writerCell.setCellValue(nrAngajat+1); // indexing
			
			//* nume, functie, cnp
			stat.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 1, 2));
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
		
			//* ORE
			writerCell = row1.createCell(5); // NZ
			writerCell.setCellValue(8);
			writerCell = row2.createCell(5); // NO
			writerCell.setCellValue(contract.getNormalucru());
			writerCell = row3.createCell(5); // SPL = ore suplimentare
			writerCell.setCellValue(realizariRetineri.getNroresuplimentare());

			//* ZILE CO
			stat.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 6, 7));
			writerCell = row1.createCell(6); // ZL
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(realizariRetineri.getZilelucrate());
			writerCell = row2.createCell(6); // CS
			writerCell.setCellValue(coService.getZileCS(luna, an, idcontract));
			writerCell = row2.createCell(7); // CO
			writerCell.setCellValue(coService.getZileCO(luna, an, idcontract));
			writerCell = row3.createCell(6); // CFP
			writerCell.setCellValue(coService.getZileCFP(luna, an, idcontract));
			writerCell = row3.createCell(7); // ST
			writerCell.setCellValue(coService.getZileST(luna, an, idcontract));
		
			//* ZILE CM
			writerCell = row1.createCell(8); // CM
			writerCell.setCellValue(cmService.getZileCM(luna, an, idcontract));
			writerCell = row2.createCell(8); // FNUASS
			writerCell.setCellValue(0);
			writerCell = row3.createCell(8); // FAAMBP
			writerCell.setCellValue(0);

			//* ORE
			writerCell = row1.createCell(9); // ore ind 75%
			writerCell.setCellValue(0);
			writerCell = row2.createCell(9); // ore somaj
			writerCell.setCellValue(0);
			writerCell = row1.createCell(10); // ore lucrate
			writerCell.setCellValue(realizariRetineri.getOrelucrate());
			writerCell = row2.createCell(10); // ore absent
			writerCell.setCellValue(0);
			writerCell = row3.createCell(10); // Val. ore supl.
			writerCell.setCellValue(realizariRetineri.getTotaloresuplimentare());
			
			//* Salariu
			writerCell = row1.createCell(11); // sal realizat
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getSalariurealizat());
			writerCell = row2.createCell(11); // valoare CO
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getZilecolucratoare() * realizariRetineri.getSalariupezi());
			writerCell = row3.createCell(11); // CO neefect.
			writerCell.setCellValue(0);

			//* CM valoare
			writerCell = row1.createCell(12); // cm societate
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getValcm());
			writerCell = row2.createCell(12); // CM din FNUASS
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(0); // TODO
			writerCell = row3.createCell(12); // cm din FAAMBP
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(0); // TODO
			
			//* drepturi
			writerCell = row1.createCell(13); // total sporuri
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(0); // TODO
			writerCell = row2.createCell(13); // total prime
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getPrimabruta());
			writerCell = row3.createCell(12); // alte drepturi
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(0);

			//* drepturi
			writerCell = row1.createCell(14); // val tichetemasa
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getValoaretichete());
			writerCell = row2.createCell(14); // venit brut
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getTotaldrepturi());
			writerCell = row3.createCell(14); // somaj 0.5%
			writerCell.setCellValue(0);

			//* 
			writerCell = row1.createCell(15); // total sporuri
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(0); // TODO
			writerCell = row2.createCell(15); // total prime
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getPrimabruta());
			writerCell = row3.createCell(15); // alte drepturi
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(0); // TODO

			//*
			writerCell = row1.createCell(16); // CAS
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getCas()); 
			writerCell = row2.createCell(16); // CASS
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getCass());
			writerCell = row3.createCell(16); // Ded. pers.
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getDeducere());

			//*
			writerCell = row1.createCell(17); // pensie pilon 3
			writerCell.setCellValue(0); 
			writerCell = row2.createCell(17); // venit net
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getRestplata());
			writerCell = row3.createCell(17); // baza impozit
			writerCell.setCellValue(0); 

			//*
			writerCell = row1.createCell(18); // impozit
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getImpozit());
			writerCell.setCellStyle(salariuStyle);
			writerCell = row2.createCell(18); // rest plata brut
			writerCell.setCellValue(realizariRetineri.getRestplata() + retineri.getAvansnet());
			writerCell.setCellStyle(salariuStyle);
			writerCell = row3.createCell(18); // alte retineri
			writerCell.setCellValue(0); // TODO

			//*
			writerCell = row1.createCell(19); // sume neimpozabile
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(0);
			writerCell = row2.createCell(19); // avans
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(retineri.getAvansnet());
			writerCell = row3.createCell(19); // rest plata net
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getRestplata());

			//* set borders
			String cellRange="$A$" + (15+nrAngajat*3) + ":$U$" + (17+nrAngajat*3);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			nrAngajat++;
		}

		//* tabel total

		XSSFFormulaEvaluator formulaEvaluator = (XSSFFormulaEvaluator) workbook.getCreationHelper().createFormulaEvaluator();
		int rowNr = 14 + nrAngajat * 3;
		Row row1 = stat.createRow(rowNr);
		Row row2 = stat.createRow(rowNr+1);
		Row row3 = stat.createRow(rowNr+2);

		stat.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 1, 2));
		writerCell = row1.createCell(1); // nume complet
		writerCell.setCellValue("TOTAL");

		//* SALARIU
		salariuWriter = row1.createCell(4); // salariu din contract
		salariuWriter.setCellStyle(salariuStyle);
		String formula = this.sumFormula('E', 15, nrAngajat);
		salariuWriter.setCellFormula(formula);
		formulaEvaluator.evaluateFormulaCell(salariuWriter);

		salariuWriter = row2.createCell(4); // sume incluse ...
		salariuWriter.setCellStyle(salariuStyle);
		formula = this.sumFormula('E', 16, nrAngajat);
		salariuWriter.setCellFormula(formula);
		formulaEvaluator.evaluateFormulaCell(salariuWriter);

		salariuWriter = row3.createCell(4); // spor weekend
		salariuWriter.setCellStyle(salariuStyle);
		formula = this.sumFormula('E', 17, nrAngajat);
		salariuWriter.setCellFormula(formula);
		formulaEvaluator.evaluateFormulaCell(salariuWriter);
		salariuWriter = row3.createCell(3); // spor vechime
		salariuWriter.setCellStyle(salariuStyle);
		formula = this.sumFormula('D', 17, nrAngajat);
		salariuWriter.setCellFormula(formula);
		formulaEvaluator.evaluateFormulaCell(salariuWriter);
	
		//* ORE
		writerCell = row1.createCell(5); // NZ
		writerCell.setCellValue(8);
		writerCell = row2.createCell(5); // NO
		writerCell.setCellValue(0);
		writerCell = row3.createCell(5); // SPL = ore suplimentare
		writerCell.setCellValue(0);

		//* ZILE CO
		stat.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 6, 7));
		writerCell = row1.createCell(6); // ZL
		writerCell.setCellStyle(centered);
		writerCell.setCellValue(0);
		writerCell = row2.createCell(6); // CS
		writerCell.setCellValue(0);
		writerCell = row2.createCell(7); // CO
		writerCell.setCellValue(0);
		writerCell = row3.createCell(6); // CFP
		writerCell.setCellValue(0);
		writerCell = row3.createCell(7); // ST
		writerCell.setCellValue(0);
	
		//* ZILE CM
		writerCell = row1.createCell(8); // CM
		writerCell.setCellValue(0);
		writerCell = row2.createCell(8); // FNUASS
		writerCell.setCellValue(0);
		writerCell = row3.createCell(8); // FAAMBP
		writerCell.setCellValue(0);

		//* ORE
		writerCell = row1.createCell(9); // ore ind 75%
		writerCell.setCellValue(0);
		writerCell = row2.createCell(9); // ore somaj
		writerCell.setCellValue(0);
		writerCell = row1.createCell(10); // ore lucrate
		writerCell.setCellValue(0);
		writerCell = row2.createCell(10); // ore absent
		writerCell.setCellValue(0);
		writerCell = row3.createCell(10); // Val. ore supl.
		writerCell.setCellValue(0);
		
		//* Salariu
		writerCell = row1.createCell(11); // sal realizat
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);
		writerCell = row2.createCell(11); // valoare CO
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);
		writerCell = row3.createCell(11); // CO neefect.
		writerCell.setCellValue(0);

		//* CM valoare
		writerCell = row1.createCell(12); // cm societate
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);
		writerCell = row2.createCell(12); // CM din FNUASS
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0); // TODO
		writerCell = row3.createCell(12); // cm din FAAMBP
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0); // TODO
		
		//* drepturi
		writerCell = row1.createCell(13); // total sporuri
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0); // TODO
		writerCell = row2.createCell(13); // total prime
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);
		writerCell = row3.createCell(12); // alte drepturi
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);

		//* drepturi
		writerCell = row1.createCell(14); // val tichetemasa
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);
		writerCell = row2.createCell(14); // venit brut
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);
		writerCell = row3.createCell(14); // somaj 0.5%
		writerCell.setCellValue(0);

		//* 
		writerCell = row1.createCell(15); // total sporuri
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0); // TODO
		writerCell = row2.createCell(15); // total prime
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);
		writerCell = row3.createCell(15); // alte drepturi
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0); // TODO

		//*
		writerCell = row1.createCell(16); // CAS
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);
		writerCell = row2.createCell(16); // CASS
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);
		writerCell = row3.createCell(16); // Ded. pers.
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);

		//*
		writerCell = row1.createCell(17); // pensie pilon 3
		writerCell.setCellValue(0); 
		writerCell = row2.createCell(17); // venit net
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);
		writerCell = row3.createCell(17); // baza impozit
		writerCell.setCellValue(0); 

		//*
		writerCell = row1.createCell(18); // impozit
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);
		writerCell.setCellStyle(salariuStyle);
		writerCell = row2.createCell(18); // rest plata brut
		writerCell.setCellValue(0);
		writerCell.setCellStyle(salariuStyle);
		writerCell = row3.createCell(18); // alte retineri
		writerCell.setCellValue(0); // TODO

		//*
		writerCell = row1.createCell(19); // sume neimpozabile
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);
		writerCell = row2.createCell(19); // avans
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);
		writerCell = row3.createCell(19); // rest plata net
		writerCell.setCellStyle(salariuStyle);
		writerCell.setCellValue(0);

		String cellRange="$A$" + (15+nrAngajat*3) + ":$U$" + (17+nrAngajat*3);
		setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

		//* output the file
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
