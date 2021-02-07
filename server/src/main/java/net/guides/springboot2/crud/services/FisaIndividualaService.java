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

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.CM;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.ParametriiSalariu;
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.model.RealizariRetineri;

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

		FileInputStream file = new FileInputStream(new File(sheetTemplateLocation, "FisaIndividuala.xlsx"));
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
		writerCell.setCellValue(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

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

		// get styles | create styles
		CellStyle lunaStyle = sheet.getRow(8).getCell(0).getCellStyle();
		CellStyle centered = sheet.getRow(8).getCell(1).getCellStyle();
		CellStyle salariu = sheet.getRow(8).getCell(2).getCellStyle();
		CellStyle number = sheet.getRow(8).getCell(6).getCellStyle();

		CellStyle lunaStyleTotal = sheet.getRow(10).getCell(0).getCellStyle();
		CellStyle centeredTotal = sheet.getRow(10).getCell(3).getCellStyle();
		CellStyle salariuTotal = sheet.getRow(10).getCell(2).getCellStyle();
		CellStyle numberTotal = sheet.getRow(10).getCell(6).getCellStyle();

		CellStyle emptyBox1 = sheet.getRow(9).getCell(0).getCellStyle();
		CellStyle emptyBox2 = sheet.getRow(9).getCell(2).getCellStyle();


		int salariuDeIncadrareT = 0, zlT = 0, coT = 0, cmT = 0, fnuassT = 0, splT = 0, cfsT = 0, 
			salariuRealizatT = 0, valoareCoT = 0, cmSocietateT = 0, cmFnuassT = 0, oreSuplT = 0, totalPrimeT = 0, 
			venitBrutT = 0, venitNetT = 0, casT = 0, cassT = 0, cassFnuassT = 0, dedPersT = 0, impozitT = 0, 
			alteRetineriT = 0, avansT = 0, restPlataT = 0;

		// ! TABLE CONTENT
		Row row1, row2;
		int index = 0;
		for (RealizariRetineri rr : realizariRetineri) {
			List<CM> concediiMedicale = cmService.getCMInLunaAnul(rr.getLuna(), an, contract.getId());
			row1 = sheet.createRow(8+index);
			row2 = sheet.createRow(9+index);

			// luna
			writerCell = row1.createCell(0);
			writerCell.setCellStyle(lunaStyle);
			writerCell.setCellValue(zileService.getNumeLunaByNr(rr.getLuna()).toUpperCase());
			writerCell = row2.createCell(0);
			writerCell.setCellStyle(emptyBox1);

			// NZ
			writerCell = row1.createCell(1);
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(8);
			// NO
			writerCell = row2.createCell(1);
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(contract.getNormalucru());
			
			// Salariu de incadrare
			writerCell = row1.createCell(2);
			writerCell.setCellStyle(salariu);
			writerCell.setCellValue(contract.getSalariutarifar());
			salariuDeIncadrareT += contract.getSalariutarifar();
			writerCell = row2.createCell(2);
			writerCell.setCellStyle(emptyBox2);
			
			// ZL
			writerCell = row1.createCell(3);
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(rr.getZilelucrate());
			zlT += rr.getZilelucrate();
			// CO
			writerCell = row2.createCell(3);
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(rr.getZileco());
			coT += rr.getZileco();

			// CM
			int zilecmfnuass = cmService.getZilecmFNUASS(concediiMedicale);
			int zilecmfaambp = cmService.getZilecmFAAMBP(concediiMedicale);
			writerCell = row1.createCell(4);
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(rr.getZilecm() - zilecmfnuass - zilecmfaambp);
			cmT += rr.getZilecm() - zilecmfnuass - zilecmfaambp;
			// FNUASS
			writerCell = row2.createCell(4);
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(zilecmfnuass);
			fnuassT += zilecmfnuass;

			// SPL
			writerCell = row1.createCell(5);
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(rr.getNroresuplimentare());
			splT += rr.getNroresuplimentare();
			// CFS
			writerCell = row2.createCell(5);
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(rr.getZilecfp());
			cfsT += rr.getZilecfp();

			// Salariu realizat
			writerCell = row1.createCell(6);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(rr.getSalariurealizat());
			salariuRealizatT += rr.getSalariurealizat();
			// Valoare CO
			writerCell = row2.createCell(6);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(rr.getValco());
			valoareCoT += rr.getValco();

			// CM Societate
			int valcmfnuass = cmService.getValcmFNUASS(concediiMedicale);
			int valcmfaambp = cmService.getValcmFAAMBP(concediiMedicale);
			writerCell = row1.createCell(7);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(rr.getValcm() - valcmfnuass - valcmfaambp);
			cmSocietateT += rr.getValcm() - valcmfnuass - valcmfaambp;
			// CM din FNUASS
			writerCell = row2.createCell(7);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(valcmfnuass);
			cmFnuassT+= valcmfnuass;

			// ore supl.
			writerCell = row1.createCell(8);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(rr.getTotaloresuplimentare());
			oreSuplT+= rr.getTotaloresuplimentare();
			// alte drepturi
			writerCell = row2.createCell(8);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(0);

			// Total sporuri
			writerCell = row1.createCell(9);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(0);

			// total prime
			writerCell = row2.createCell(9);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(rr.getPrimabruta());
			totalPrimeT += rr.getPrimabruta();

			// Venit brut
			writerCell = row1.createCell(10);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(rr.getVenitbrut());
			venitBrutT += rr.getVenitbrut();
			// Venit net
			writerCell = row2.createCell(10);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(rr.getVenitnet());
			venitNetT += rr.getVenitnet();

			// Somaj
			writerCell = row1.createCell(11);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(0);
			// CAS
			writerCell = row2.createCell(11);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(rr.getCas());
			casT += rr.getCas();

			// CASS
			writerCell = row1.createCell(12);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(rr.getCass());
			cassT += rr.getCass();
			// CASS FNUASS
			writerCell = row2.createCell(12);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(valcmfnuass * parametriiSalariu.getCass());
			cassFnuassT += valcmfnuass * parametriiSalariu.getCass();

			// Ded. pers
			writerCell = row1.createCell(13);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(rr.getDeducere());
			dedPersT += rr.getDeducere();
			// Impozit
			writerCell = row2.createCell(13);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(rr.getImpozit());
			impozitT += rr.getImpozit();

			// alte retineri
			writerCell = row1.createCell(14);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(rr.getRetineri().getAlteRetineri());
			alteRetineriT += rr.getRetineri().getAlteRetineri();
			// Alte venituri
			writerCell = row2.createCell(14);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(0);

			// Avans
			writerCell = row1.createCell(15);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(rr.getRetineri().getAvansnet());
			avansT += rr.getRetineri().getAvansnet();
			// Rest plata
			writerCell = row2.createCell(15);
			writerCell.setCellStyle(number);
			writerCell.setCellValue(rr.getRestplata());
			restPlataT += rr.getRestplata();

			index+=2;
		}

		// ! TOTAL
		row1 = sheet.createRow(8+index);
		row2 = sheet.createRow(9+index);

		// TOTAL AN
		writerCell = row1.createCell(0);
		writerCell.setCellStyle(lunaStyleTotal);
		writerCell.setCellValue("TOTAL " + an);
		
		// Salariu de incadrare
		writerCell = row1.createCell(2);
		writerCell.setCellStyle(salariuTotal);
		writerCell.setCellValue(salariuDeIncadrareT);
		
		// ZL
		writerCell = row1.createCell(3);
		writerCell.setCellStyle(centeredTotal);
		writerCell.setCellValue(zlT);
		// CO
		writerCell = row2.createCell(3);
		writerCell.setCellStyle(centeredTotal);
		writerCell.setCellValue(coT);

		// CM
		writerCell = row1.createCell(4);
		writerCell.setCellStyle(centeredTotal);
		writerCell.setCellValue(cmT);
		// FNUASS
		writerCell = row2.createCell(4);
		writerCell.setCellStyle(centeredTotal);
		writerCell.setCellValue(fnuassT);

		// SPL
		writerCell = row1.createCell(5);
		writerCell.setCellStyle(centeredTotal);
		writerCell.setCellValue(splT);
		// CFS
		writerCell = row2.createCell(5);
		writerCell.setCellStyle(centeredTotal);
		writerCell.setCellValue(cfsT);

		// Salariu realizat
		writerCell = row1.createCell(6);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(salariuRealizatT);
		// Valoare CO
		writerCell = row2.createCell(6);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(valoareCoT);

		// CM Societate
		writerCell = row1.createCell(7);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(cmSocietateT);
		// CM din FNUASS
		writerCell = row2.createCell(7);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(cmFnuassT);

		// ore supl.
		writerCell = row1.createCell(8);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(oreSuplT);
		// alte drepturi
		writerCell = row2.createCell(8);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(0);

		// Total sporuri
		writerCell = row1.createCell(9);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(0);
		// total prime
		writerCell = row2.createCell(9);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(totalPrimeT);

		// Venit brut
		writerCell = row1.createCell(10);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(venitBrutT);
		// Venit net
		writerCell = row2.createCell(10);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(venitNetT);

		// Somaj
		writerCell = row1.createCell(11);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(0);
		// CAS
		writerCell = row2.createCell(11);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(casT);

		// CASS
		writerCell = row1.createCell(12);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(cassT);
		// CASS FNUASS
		writerCell = row2.createCell(12);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(cassFnuassT);

		// Ded. pers
		writerCell = row1.createCell(13);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(dedPersT);
		// Impozit
		writerCell = row2.createCell(13);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(impozitT);

		// alte retineri
		writerCell = row1.createCell(14);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(alteRetineriT);
		// Alte venituri
		writerCell = row2.createCell(14);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(0);

		// Avans
		writerCell = row1.createCell(15);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(avansT);
		// Rest plata
		writerCell = row2.createCell(15);
		writerCell.setCellStyle(numberTotal);
		writerCell.setCellValue(restPlataT);

		// border
		writerCell = row2.createCell(1);
		CellStyle borderLeftBottom = workbook.createCellStyle();
		CellStyle borderBottom = workbook.createCellStyle();
		borderBottom.setBorderBottom(BorderStyle.THIN);
		borderLeftBottom.setBorderBottom(BorderStyle.THIN);
		borderLeftBottom.setBorderLeft(BorderStyle.THIN);
		writerCell.setCellStyle(borderBottom);
		
		writerCell = row2.createCell(0);
		writerCell.setCellStyle(borderLeftBottom);
		
		writerCell = row2.createCell(2);
		borderLeftBottom.setBorderLeft(BorderStyle.THIN);
		writerCell.setCellStyle(borderLeftBottom);

		/* ------ ENDING ------ **/
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		evaluator.evaluateAll();
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String newFileLocation = String.format("%s/downloads/%d/Fisa individuala - %s - %d.xlsx", homeLocation, userID, persoana.getNumeIntreg(), an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}
}
