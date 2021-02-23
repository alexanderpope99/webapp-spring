package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
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
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.CM;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Retineri;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.AngajatRepository;
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
	private SocietateRepository societateRepository;
	@Autowired
	private AngajatRepository angajatRepository;

	private String homeLocation = "src/main/java/net/guides/springboot2/crud/";

	private void setRegionBorder(CellRangeAddress region, Sheet sheet) {
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
	}

	private String sumFormula(char colName, int row, int nrAngajat) {
		StringBuilder formula = new StringBuilder();
		formula.append(colName).append(row);

		for (int i = 1; i < nrAngajat; i++) {
			formula.append('+').append(colName).append(row + i * 3);
		}

		return formula.toString();
	}

	public boolean createStatSalarii(int luna, int an, int idsocietate, String intocmitDe, int userID, int angajatiScutitiImpozit) throws IOException, ResourceNotFoundException {
		try {
			Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
			Adresa adresaSocietate = societate.getAdresa();

			List<Angajat> angajati = new ArrayList<>();

			if (angajatiScutitiImpozit == 0)
				angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(idsocietate);
			else if (angajatiScutitiImpozit == 1)
				angajati = angajatRepository.findBySocietate_IdAndContract_CalculdeduceriAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(idsocietate, true);
			else if (angajatiScutitiImpozit == 2)
				angajati = angajatRepository.findBySocietate_IdAndContract_CalculdeduceriAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(idsocietate, false);

			angajati.removeIf(angajat -> {
				LocalDate ultimaZiLucru = angajat.getContract().getUltimazilucru();
				if (ultimaZiLucru != null) {
					if (ultimaZiLucru.getYear() < an)
						return true;
					else if (ultimaZiLucru.getYear() == an)
						return ultimaZiLucru.getMonthValue() < luna;
					else
						return false;
				} else
					return false;
			});

			String statTemplateLocation = homeLocation + "/templates";

			FileInputStream file = new FileInputStream(new File(statTemplateLocation, "StatSalarii.xlsx"));
			Workbook workbook = new XSSFWorkbook(file);
			Sheet stat = workbook.getSheetAt(0);

			// * create styles
			CellStyle salariuStyle = workbook.createCellStyle();
			CellStyle functieStyle = workbook.createCellStyle();
			CellStyle nrContractStyle = workbook.createCellStyle();
			CellStyle centered = workbook.createCellStyle();
			CellStyle font10 = workbook.createCellStyle();
			CellStyle salariu10Style = workbook.createCellStyle();

			Font font7 = workbook.createFont();
			Font font = workbook.createFont();

			centered.setAlignment(HorizontalAlignment.CENTER);

			font7.setFontHeightInPoints((short) 7);
			font7.setFontName("Tahoma");
			functieStyle.setFont(font7);
			functieStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			font.setFontHeightInPoints((short) 10);
			font.setFontName("Tahoma");
			font10.setFont(font);

			DataFormat format = workbook.createDataFormat();
			salariuStyle.setDataFormat(format.getFormat("#,##0"));

			salariu10Style.setFont(font);
			salariu10Style.setDataFormat(format.getFormat("#,##0"));

			nrContractStyle.setAlignment(HorizontalAlignment.RIGHT);

			Cell salariuWriter;
			Cell functieWriter;

			// * write date societate
			Cell writerCell = stat.getRow(0).getCell(0);
			writerCell.setCellValue(societate.getNume()); // nume soc
			writerCell = stat.getRow(1).getCell(0);
			writerCell.setCellValue("CUI: " + societate.getCif()); // cif
			writerCell = stat.getRow(2).getCell(0);
			writerCell.setCellValue("Nr. Reg. Com.: " + societate.getRegcom()); // nr reg com
			writerCell = stat.getRow(3).getCell(0);
			writerCell.setCellValue("Strada: " + adresaSocietate.getAdresa()); // adresa
			writerCell = stat.getRow(4).getCell(0);
			// judet + localitate
			writerCell.setCellValue(adresaSocietate.getJudet() + ", " + adresaSocietate.getLocalitate());

			// * write luna, an
			writerCell = stat.getRow(4).getCell(11);
			String lunaNume = zileService.getNumeLunaByNr(luna);
			writerCell.setCellValue("- " + lunaNume + " " + an + " -");

			// ? write angajati:
			int nrAngajat = 0;
			int impozitScutit = 0;
			for (Angajat angajat : angajati) {
				int rowNr = 14 + nrAngajat * 3;
				Row row1 = stat.createRow(rowNr);
				Row row2 = stat.createRow(rowNr + 1);
				Row row3 = stat.createRow(rowNr + 2);

				// * 1. get contract + stat(luna, an, idcontract); -- contract should not be null
				Contract contract = angajat.getContract();
				Persoana persoana = angajat.getPersoana();
				List<CM> concediiMedicale = cmService.getCMInLunaAnul(luna, an, contract.getId());

				int idcontract = contract.getId();

				RealizariRetineri realizariRetineri = realizariRetineriService.saveOrGetRealizariRetineri(luna, an, idcontract);
				Retineri retineri = retineriService.getRetinereByIdstat(realizariRetineri.getId());

				impozitScutit += realizariRetineri.getImpozitscutit();

				// * 2. insert data:
				writerCell = row1.createCell(0);
				writerCell.setCellValue(nrAngajat + 1); // indexing

				// * nume, functie, cnp
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

				// * SALARIU
				salariuWriter = row1.createCell(4); // salariu din contract
				salariuWriter.setCellStyle(salariuStyle);
				salariuWriter.setCellValue(contract.getSalariutarifar());
				salariuWriter = row2.createCell(4); // sume incluse ...
				salariuWriter.setCellValue(0);
				salariuWriter = row3.createCell(4); // spor weekend
				salariuWriter.setCellValue(0);
				salariuWriter = row3.createCell(3); // spor vechime
				salariuWriter.setCellValue(0);

				// * ORE
				writerCell = row1.createCell(5); // NZ
				writerCell.setCellStyle(centered);
				writerCell.setCellValue(8);
				writerCell = row2.createCell(5); // NO
				writerCell.setCellStyle(centered);
				writerCell.setCellValue(contract.getNormalucru());
				writerCell = row3.createCell(5); // SPL = ore suplimentare
				writerCell.setCellStyle(centered);
				writerCell.setCellValue(realizariRetineri.getNroresuplimentare());

				// * ZILE CO
				stat.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 6, 7));
				writerCell = row1.createCell(6); // ZL
				writerCell.setCellStyle(centered);
				writerCell.setCellValue(realizariRetineri.getZilelucrate());
				writerCell = row2.createCell(6); // CS
				writerCell.setCellStyle(centered);
				writerCell.setCellValue(coService.getZileCS(luna, an, idcontract));
				writerCell = row2.createCell(7); // CO
				writerCell.setCellStyle(centered);
				writerCell.setCellValue(realizariRetineri.getZilecolucratoare());
				writerCell = row3.createCell(6); // CFP
				writerCell.setCellStyle(centered);
				writerCell.setCellValue(realizariRetineri.getZilecfplucratoare());
				writerCell = row3.createCell(7); // ST
				writerCell.setCellStyle(centered);
				writerCell.setCellValue(coService.getZileST(luna, an, idcontract));

				// * ZILE CM
				int zilecmfnuass = cmService.getZilecmFNUASS(concediiMedicale);
				int zilecmfaambp = cmService.getZilecmFAAMBP(concediiMedicale);
				writerCell = row1.createCell(8); // zile CM
				writerCell.setCellStyle(centered);
				writerCell.setCellValue(realizariRetineri.getZilecmlucratoare() - zilecmfnuass - zilecmfaambp);
				writerCell = row2.createCell(8); // zile FNUASS
				writerCell.setCellStyle(centered);
				writerCell.setCellValue(cmService.getZilecmFNUASS(concediiMedicale));
				writerCell = row3.createCell(8); // zile FAAMBP
				writerCell.setCellStyle(centered);
				writerCell.setCellValue(cmService.getZilecmFAAMBP(concediiMedicale));

				// * ORE
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

				// * Salariu
				writerCell = row1.createCell(11); // sal realizat
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getSalariurealizat());
				writerCell = row2.createCell(11); // valoare CO
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getValco());
				writerCell = row3.createCell(11); // CO neefect.
				writerCell.setCellValue(0);

				// * CM valoare
				// int valcmfnuass = cmService.getValcmFNUASS(concediiMedicale);
				// int valcmfaambp = cmService.getValcmFAAMBP(concediiMedicale);
				writerCell = row1.createCell(12); // cm societate
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getValcmsocietate());
				writerCell = row2.createCell(12); // CM din FNUASS
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getValcmfnuass());
				writerCell = row3.createCell(12); // cm din FAAMBP
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getValcmfaambp());

				// * drepturi
				writerCell = row1.createCell(13); // total sporuri
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(0);
				writerCell = row2.createCell(13); // total prime
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getPrimabruta());
				writerCell = row3.createCell(13); // alte drepturi
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(0);

				// * drepturi
				writerCell = row1.createCell(14); // val tichetemasa
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getValoaretichete());
				writerCell = row2.createCell(14); // venit brut
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getTotaldrepturi() + realizariRetineri.getValoaretichete());
				writerCell = row3.createCell(14); // somaj 0.5%
				writerCell.setCellValue(0);

				// *
				writerCell = row1.createCell(15); // Val zile libere B
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(0);
				writerCell = row2.createCell(15); // Val zile libere N
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(0);
				writerCell = row3.createCell(15); // Val ind somaj
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(0);

				// *
				writerCell = row1.createCell(16); // CAS
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getCas());
				writerCell = row2.createCell(16); // CASS
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getCass());
				writerCell = row3.createCell(16); // Ded. pers.
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getDeducere());

				// *
				writerCell = row1.createCell(17); // pensie pilon 3
				writerCell.setCellValue(0);
				writerCell = row2.createCell(17); // venit net
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getVenitnet());
				writerCell = row3.createCell(17); // baza impozit
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getBazaimpozit());

				// *
				writerCell = row1.createCell(18); // impozit
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getImpozit());
				writerCell = row2.createCell(18); // rest plata brut
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getRestplatabrut());
				writerCell = row3.createCell(18); // alte retineri
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(retineri.getImprumuturi());

				// *
				writerCell = row1.createCell(19); // sume neimpozabile
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(0);
				writerCell = row2.createCell(19); // avans
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(retineri.getAvansnet());
				writerCell = row3.createCell(19); // rest plata net
				writerCell.setCellStyle(salariuStyle);
				writerCell.setCellValue(realizariRetineri.getVenitnet() - retineri.getAvansnet() - realizariRetineri.getImpozit());

				// * set borders
				String cellRange = "$A$" + (15 + nrAngajat * 3) + ":$U$" + (17 + nrAngajat * 3);
				setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
				nrAngajat++;
			}

			// * tabel total
			XSSFFormulaEvaluator formulaEvaluator = (XSSFFormulaEvaluator) workbook.getCreationHelper().createFormulaEvaluator();
			int rowNr = 14 + nrAngajat * 3;
			Row row1 = stat.createRow(rowNr);
			Row row2 = stat.createRow(rowNr + 1);
			Row row3 = stat.createRow(rowNr + 2);

			stat.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 1, 2));
			writerCell = row1.createCell(1); // nume complet
			writerCell.setCellValue("TOTAL");

			// * SALARIU TOTAL
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

			// * ORE TOTAL
			writerCell = row3.createCell(5); // SPL = ore suplimentare
			writerCell.setCellStyle(centered);
			formula = this.sumFormula('F', 17, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);

			// * ZILE CO TOTAL
			stat.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 6, 7));
			writerCell = row1.createCell(6); // ZL
			writerCell.setCellStyle(centered);
			formula = this.sumFormula('G', 15, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row2.createCell(6); // CS
			writerCell.setCellStyle(centered);
			formula = this.sumFormula('G', 16, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row2.createCell(7); // CO
			writerCell.setCellStyle(centered);
			formula = this.sumFormula('H', 16, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row3.createCell(6); // CFP
			writerCell.setCellStyle(centered);
			formula = this.sumFormula('G', 17, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row3.createCell(7); // ST
			writerCell.setCellStyle(centered);
			formula = this.sumFormula('H', 17, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);

			// * ZILE CM TOTAL
			writerCell = row1.createCell(8); // CM
			writerCell.setCellStyle(centered);
			formula = this.sumFormula('I', 15, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row2.createCell(8); // FNUASS
			writerCell.setCellStyle(centered);
			formula = this.sumFormula('I', 16, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row3.createCell(8); // FAAMBP
			writerCell.setCellStyle(centered);
			formula = this.sumFormula('I', 17, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);

			// * ORE TOTAL
			writerCell = row1.createCell(9); // ore ind 75%
			formula = this.sumFormula('J', 15, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row2.createCell(9); // ore somaj
			formula = this.sumFormula('J', 16, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row1.createCell(10); // ore lucrate
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('K', 15, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row2.createCell(10); // ore absent
			formula = this.sumFormula('K', 16, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row3.createCell(10); // Val. ore supl.
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('K', 17, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);

			// * Salariu TOTAL
			writerCell = row1.createCell(11); // sal realizat
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('L', 15, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row2.createCell(11); // valoare CO
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('L', 16, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row3.createCell(11); // CO neefect.
			formula = this.sumFormula('L', 17, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);

			// * CM valoare TOTAL
			writerCell = row1.createCell(12); // cm societate
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('M', 15, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row2.createCell(12); // CM din FNUASS
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('M', 16, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row3.createCell(12); // cm din FAAMBP
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('M', 17, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);

			// * drepturi TOTAL
			writerCell = row1.createCell(13); // total sporuri
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('N', 15, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row2.createCell(13); // total prime
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('N', 16, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row3.createCell(13); // alte drepturi
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('N', 17, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);

			// * drepturi TOTAL
			writerCell = row1.createCell(14); // val tichetemasa
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('O', 15, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row2.createCell(14); // venit brut
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('O', 16, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row3.createCell(14); // somaj 0.5%
			formula = this.sumFormula('O', 17, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);

			// * TOTAL
			writerCell = row1.createCell(15); // total sporuri
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('P', 15, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row2.createCell(15); // total prime
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('P', 16, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row3.createCell(15); // alte drepturi
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('P', 17, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);

			// * TOTAL
			writerCell = row1.createCell(16); // CAS
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('Q', 15, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row2.createCell(16); // CASS
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('Q', 16, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row3.createCell(16); // Ded. pers.
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('Q', 17, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);

			// * TOTAL
			writerCell = row1.createCell(17); // pensie pilon 3
			formula = this.sumFormula('R', 15, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row2.createCell(17); // venit net
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('R', 16, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row3.createCell(17); // baza impozit
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('R', 17, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);

			// * TOTAL
			writerCell = row1.createCell(18); // impozit
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('S', 15, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row2.createCell(18); // rest plata brut
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('S', 16, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row3.createCell(18); // alte retineri
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('S', 17, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);

			// * TOTAL
			writerCell = row1.createCell(19); // sume neimpozabile
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('T', 15, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row2.createCell(19); // avans
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('T', 16, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);
			writerCell = row3.createCell(19); // rest plata net
			writerCell.setCellStyle(salariuStyle);
			formula = this.sumFormula('T', 17, nrAngajat);
			writerCell.setCellFormula(formula);
			formulaEvaluator.evaluateFormulaCell(writerCell);

			// * SET BORDER TOTAL
			String cellRange = "$A$" + (15 + nrAngajat * 3) + ":$U$" + (17 + nrAngajat * 3);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			// * TABEL FINAL
			int totalRowNr = 14 + nrAngajat * 3;
			rowNr = 14 + (nrAngajat + 1) * 3 + 1;
			row1 = stat.createRow(rowNr);
			row2 = stat.createRow(rowNr + 1);
			row3 = stat.createRow(rowNr + 2);
			Row row4 = stat.createRow(rowNr + 3);
			Row row5 = stat.createRow(rowNr + 4);
			Row row6 = stat.createRow(rowNr + 5);
			Row row8 = stat.createRow(rowNr + 7);
			Row row9 = stat.createRow(rowNr + 8);

			stat.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 0, 3)); // obligatii angajator
			stat.addMergedRegion(new CellRangeAddress(rowNr + 2, rowNr + 2, 0, 3)); // cas cond. speciale.
			stat.addMergedRegion(new CellRangeAddress(rowNr + 3, rowNr + 3, 0, 3)); // contributie cam 2.25%
			stat.addMergedRegion(new CellRangeAddress(rowNr + 4, rowNr + 4, 0, 3)); // contributie cam 0.3375%
			stat.addMergedRegion(new CellRangeAddress(rowNr + 5, rowNr + 5, 0, 3)); // fond 4% pers cu handicap
			stat.addMergedRegion(new CellRangeAddress(rowNr + 7, rowNr + 7, 0, 3)); // recapitulare salariati
			stat.addMergedRegion(new CellRangeAddress(rowNr + 8, rowNr + 8, 0, 3)); // total retineri salariati

			stat.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 4, 7)); // baza de calcul
			stat.addMergedRegion(new CellRangeAddress(rowNr + 2, rowNr + 2, 4, 7));
			stat.addMergedRegion(new CellRangeAddress(rowNr + 3, rowNr + 3, 4, 7));
			stat.addMergedRegion(new CellRangeAddress(rowNr + 4, rowNr + 4, 4, 7));
			stat.addMergedRegion(new CellRangeAddress(rowNr + 5, rowNr + 5, 4, 7));
			stat.addMergedRegion(new CellRangeAddress(rowNr + 7, rowNr + 7, 4, 7));
			stat.addMergedRegion(new CellRangeAddress(rowNr + 8, rowNr + 8, 4, 7));

			stat.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 8, 10)); // contributia
			stat.addMergedRegion(new CellRangeAddress(rowNr + 1, rowNr + 1, 8, 10)); // calculata
			stat.addMergedRegion(new CellRangeAddress(rowNr + 2, rowNr + 2, 8, 10));
			stat.addMergedRegion(new CellRangeAddress(rowNr + 3, rowNr + 3, 8, 10));
			stat.addMergedRegion(new CellRangeAddress(rowNr + 4, rowNr + 4, 8, 10));
			stat.addMergedRegion(new CellRangeAddress(rowNr + 5, rowNr + 5, 8, 10));
			stat.addMergedRegion(new CellRangeAddress(rowNr + 7, rowNr + 7, 8, 10));
			stat.addMergedRegion(new CellRangeAddress(rowNr + 8, rowNr + 8, 8, 10));

			stat.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 12, 15)); // obligatii angajator
			stat.addMergedRegion(new CellRangeAddress(rowNr + 1, rowNr + 1, 12, 15)); // defalcate
			stat.addMergedRegion(new CellRangeAddress(rowNr + 2, rowNr + 2, 12, 15)); // cas cond speciale 8%
			stat.addMergedRegion(new CellRangeAddress(rowNr + 3, rowNr + 3, 12, 15)); // cas cond speciale 4%

			stat.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 16, 18)); // baza de calcul
			stat.addMergedRegion(new CellRangeAddress(rowNr + 2, rowNr + 2, 16, 18));
			stat.addMergedRegion(new CellRangeAddress(rowNr + 3, rowNr + 3, 16, 18));

			stat.addMergedRegion(new CellRangeAddress(rowNr, rowNr, 19, 20)); // contributia
			stat.addMergedRegion(new CellRangeAddress(rowNr + 1, rowNr + 1, 19, 20)); // calculata
			stat.addMergedRegion(new CellRangeAddress(rowNr + 2, rowNr + 2, 19, 20));
			stat.addMergedRegion(new CellRangeAddress(rowNr + 3, rowNr + 3, 19, 20));

			stat.addMergedRegion(new CellRangeAddress(rowNr + 7, rowNr + 7, 11, 13)); // cass 10%***
			stat.addMergedRegion(new CellRangeAddress(rowNr + 8, rowNr + 8, 11, 13));

			stat.addMergedRegion(new CellRangeAddress(rowNr + 7, rowNr + 7, 14, 16)); // Impozit scutit cf. art. 60 din
			// CF
			stat.addMergedRegion(new CellRangeAddress(rowNr + 8, rowNr + 8, 14, 16));

			stat.addMergedRegion(new CellRangeAddress(rowNr + 7, rowNr + 7, 17, 19)); // impozit scutit cf. art 60 din
			// CF
			stat.addMergedRegion(new CellRangeAddress(rowNr + 8, rowNr + 8, 17, 19));

			// * OBLIGATII ANGAJATOR
			writerCell = row1.createCell(0);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("OBLIGATII ANGAJATOR");
			cellRange = "$A$" + (rowNr + 1) + ":$D$" + (rowNr + 2);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row3.createCell(0);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("CAS cond. Speciale/Deosebite");
			cellRange = "$A$" + (rowNr + 3) + ":$D$" + (rowNr + 3);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row4.createCell(0);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("Contributie CAM 2.25%");
			cellRange = "$A$" + (rowNr + 4) + ":$D$" + (rowNr + 4);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row5.createCell(0);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("Contributie CAM 0.3375%***");
			cellRange = "$A$" + (rowNr + 5) + ":$D$" + (rowNr + 5);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row6.createCell(0);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("Fond 4% pers cu handicap");
			cellRange = "$A$" + (rowNr + 6) + ":$D$" + (rowNr + 6);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			// * BAZA DE CALCUL
			writerCell = row1.createCell(4);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("BAZA DE CALCUL");
			cellRange = "$E$" + (rowNr + 1) + ":$H$" + (rowNr + 2);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row3.createCell(4); // CAS cond. ...
			writerCell.setCellStyle(font10);
			writerCell.setCellValue(0);
			cellRange = "$E$" + (rowNr + 3) + ":$H$" + (rowNr + 3);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row4.createCell(4); // CAM 2.25%
			writerCell.setCellStyle(salariu10Style);
			writerCell.setCellValue(stat.getRow(totalRowNr + 1).getCell(14).getNumericCellValue() - stat.getRow(totalRowNr).getCell(14).getNumericCellValue() - stat.getRow(totalRowNr+1).getCell(12).getNumericCellValue());
			cellRange = "$E$" + (rowNr + 4) + ":$H$" + (rowNr + 4);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row5.createCell(4); // CAM 0.3375%
			writerCell.setCellStyle(salariu10Style);
			writerCell.setCellValue(0);
			cellRange = "$E$" + (rowNr + 5) + ":$H$" + (rowNr + 5);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row6.createCell(4); // CAM 0.3375%
			writerCell.setCellStyle(salariu10Style);
			writerCell.setCellValue(0);
			cellRange = "$E$" + (rowNr + 6) + ":$H$" + (rowNr + 6);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			// * CONTRIBUTIA CALCULATA
			writerCell = row1.createCell(8);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("CONTRIBUTIA");
			writerCell = row2.createCell(8);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("CALCULATA");
			cellRange = "$I$" + (rowNr + 1) + ":$K$" + (rowNr + 2);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row3.createCell(8); // CAS cond. ...
			writerCell.setCellStyle(font10);
			writerCell.setCellValue(0);
			cellRange = "$I$" + (rowNr + 3) + ":$K$" + (rowNr + 3);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row4.createCell(8); // CAM 2.25%
			writerCell.setCellStyle(salariu10Style);
			writerCell.setCellValue(row4.getCell(4).getNumericCellValue() * 0.0225);
			cellRange = "$I$" + (rowNr + 4) + ":$K$" + (rowNr + 4);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row5.createCell(8); // CAM 0.3375%
			writerCell.setCellStyle(salariu10Style);
			writerCell.setCellValue(0);
			cellRange = "$I$" + (rowNr + 5) + ":$K$" + (rowNr + 5);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row6.createCell(8); // Fond 4% pers cu handicap
			writerCell.setCellStyle(salariu10Style);
			writerCell.setCellValue(0);
			cellRange = "$I$" + (rowNr + 6) + ":$K$" + (rowNr + 6);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			// * OBLIGATII ANGAJATOR DEFALCATE
			writerCell = row1.createCell(12);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("OBLIGATII ANGAJATOR");
			writerCell = row2.createCell(12);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("DEFALCATE");
			cellRange = "$M$" + (rowNr + 1) + ":$P$" + (rowNr + 2);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row3.createCell(12);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("CAS cond speciale 8%");
			cellRange = "$M$" + (rowNr + 2) + ":$P$" + (rowNr + 3);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row4.createCell(12);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("CAS cond speciale 4%");
			cellRange = "$M$" + (rowNr + 3) + ":$P$" + (rowNr + 4);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			// * BAZA DE CALCUL
			writerCell = row1.createCell(16);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("BAZA DE CALCUL");
			cellRange = "$Q$" + (rowNr + 1) + ":$S$" + (rowNr + 2);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row3.createCell(16);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue(0);
			cellRange = "$Q$" + (rowNr + 3) + ":$S$" + (rowNr + 3);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row4.createCell(16);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue(0);
			cellRange = "$Q$" + (rowNr + 4) + ":$S$" + (rowNr + 4);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			// * CONTRIBUTIA CALCULATA
			writerCell = row1.createCell(19);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("CONTRIBUTIA");
			writerCell = row2.createCell(19);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("CALCULATA");
			cellRange = "$T$" + (rowNr + 1) + ":$U$" + (rowNr + 2);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row3.createCell(19);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue(0);
			cellRange = "$T$" + (rowNr + 3) + ":$U$" + (rowNr + 3);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row4.createCell(19);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue(0);
			cellRange = "$T$" + (rowNr + 4) + ":$U$" + (rowNr + 4);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			// * RECAPITULARE SALARIATI
			writerCell = row8.createCell(0);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("RECAPITULARE SALARIATI");
			cellRange = "A$" + (rowNr + 8) + ":$D$" + (rowNr + 8);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row9.createCell(0);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("Total retineri salariati");
			cellRange = "A$" + (rowNr + 9) + ":$D$" + (rowNr + 9);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			// * CAS 25%***
			writerCell = row8.createCell(4);
			writerCell.setCellStyle(salariu10Style);
			writerCell.setCellValue("CAS 25%***");
			cellRange = "E$" + (rowNr + 8) + ":$H$" + (rowNr + 8);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row9.createCell(4);
			writerCell.setCellStyle(salariu10Style);
			writerCell.setCellValue(stat.getRow(totalRowNr).getCell(16).getNumericCellValue());
			cellRange = "E$" + (rowNr + 9) + ":$H$" + (rowNr + 9);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			// * CAS 21.25%***
			writerCell = row8.createCell(8);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("CAS 21.25%***");
			cellRange = "I$" + (rowNr + 8) + ":$K$" + (rowNr + 8);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row9.createCell(8);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue(0);
			cellRange = "I$" + (rowNr + 9) + ":$K$" + (rowNr + 9);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			// * CASS 10%***
			writerCell = row8.createCell(11);
			writerCell.setCellStyle(salariu10Style);
			writerCell.setCellValue("CASS 10%***");
			cellRange = "L$" + (rowNr + 8) + ":$N$" + (rowNr + 8);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row9.createCell(11);
			writerCell.setCellStyle(salariu10Style);
			writerCell.setCellValue(stat.getRow(totalRowNr + 1).getCell(16).getNumericCellValue());
			cellRange = "L$" + (rowNr + 9) + ":$N$" + (rowNr + 9);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			// * CASS scutit***
			writerCell = row8.createCell(14);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("CASS scutit***");
			cellRange = "O$" + (rowNr + 8) + ":$Q$" + (rowNr + 8);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row9.createCell(14);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue(0);
			cellRange = "O$" + (rowNr + 9) + ":$Q$" + (rowNr + 9);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			// * Impozit scutit cf. art. 60 din CF
			writerCell = row8.createCell(17);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("Impozit scutit cf. art. 60 din CF");
			cellRange = "R$" + (rowNr + 8) + ":$T$" + (rowNr + 8);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);
			writerCell = row9.createCell(17);
			writerCell.setCellStyle(salariu10Style);
			writerCell.setCellValue(impozitScutit);
			cellRange = "R$" + (rowNr + 9) + ":$T$" + (rowNr + 9);
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			// * semnatura
			row1 = stat.createRow(rowNr + 14);

			writerCell = row1.createCell(1);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("INTOCMIT DE");

			writerCell = row1.createCell(10);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("DIRECTOR DEP. FINANCIAR");

			writerCell = row1.createCell(18);
			writerCell.setCellStyle(font10);
			writerCell.setCellValue("DIRECTOR GENERAL,");

			row1 = stat.createRow(rowNr + 15);

			writerCell = row1.createCell(1);
			writerCell.setCellValue(intocmitDe);
			writerCell.setCellStyle(font10);

			row1 = stat.createRow(rowNr + 16);
			writerCell = row1.createCell(0);
			writerCell.setCellValue("Legenda: R - retinut; C - calculat; *CAS 25% / 21.25% pt. societatile de constructii cf. OUG 114/2018; **CASS - 10% / 0% pt. societatile de constructii cf. OUG 114/2018; ***Contributii pt. societatile de constructii cf. ");
			writerCell.setCellStyle(functieStyle);

			stat.autoSizeColumn(16);

			// * OUTPUT THE FILE
			Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
			String tipStat = "";
			if (angajatiScutitiImpozit == 1)
				tipStat = " (doar impozit)";
			else if (angajatiScutitiImpozit == 2)
				tipStat = " (fara impozit)";
			String newFileLocation = String.format("%s/downloads/%d/Stat Salarii%s - %s - %s %d.xlsx", homeLocation, userID, tipStat, societate.getNume(), lunaNume, an);

			FileOutputStream outputStream = new FileOutputStream(newFileLocation);
			workbook.write(outputStream);
			workbook.close();

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	} // ! createStatSalarii per societate

	public boolean createStatIndividual(int luna, int an, int idangajat, int idsocietate, int userID, Boolean recalc) throws ResourceNotFoundException {
		try {
			Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
			Adresa adresaSocietate = societate.getAdresa();

			Angajat angajat = angajatRepository.findById(idangajat).orElseThrow(() -> new ResourceNotFoundException("Nu există persoană cu id: " + idangajat));

			Persoana persoana = angajat.getPersoana();
			Contract contract = angajat.getContract();
			List<CM> concediiMedicale = cmService.getCMInLunaAnul(luna, an, contract.getId());

			int idcontract = contract.getId();
			RealizariRetineri realizariRetineri;
			if (recalc == null || !recalc)
				realizariRetineri = realizariRetineriService.saveOrGetRealizariRetineri(luna, an, idcontract);
			else
				realizariRetineri = realizariRetineriService.getRealizariRetineri(luna, an, idcontract);

			Retineri retineri = retineriService.getRetinereByIdstat(realizariRetineri.getId());

			String statTemplateLocation = homeLocation + "/templates";
			FileInputStream file = new FileInputStream(new File(statTemplateLocation, "StatIndividual.xlsx"));
			Workbook workbook = new XSSFWorkbook(file);
			Sheet stat = workbook.getSheetAt(0);

			// * create styles
			CellStyle salariuStyle = workbook.createCellStyle();
			CellStyle functieStyle = workbook.createCellStyle();
			CellStyle nrContractStyle = workbook.createCellStyle();
			CellStyle centered = workbook.createCellStyle();
			CellStyle font10 = workbook.createCellStyle();
			CellStyle salariu10Style = workbook.createCellStyle();

			Font font7 = workbook.createFont();
			Font font = workbook.createFont();

			centered.setAlignment(HorizontalAlignment.CENTER);

			font7.setFontHeightInPoints((short) 7);
			font7.setFontName("Tahoma");
			functieStyle.setFont(font7);
			functieStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			font.setFontHeightInPoints((short) 10);
			font.setFontName("Tahoma");
			font10.setFont(font);

			DataFormat format = workbook.createDataFormat();
			salariuStyle.setDataFormat(format.getFormat("#,##0"));

			salariu10Style.setFont(font);
			salariu10Style.setDataFormat(format.getFormat("#,##0"));

			nrContractStyle.setAlignment(HorizontalAlignment.RIGHT);

			Cell salariuWriter;
			Cell functieWriter;

			// * write date societate
			Cell writerCell = stat.getRow(0).getCell(0);
			writerCell.setCellValue(societate.getNume()); // nume soc
			writerCell = stat.getRow(1).getCell(0);
			writerCell.setCellValue("CUI: " + societate.getCif()); // cif
			writerCell = stat.getRow(2).getCell(0);
			writerCell.setCellValue("Nr. Reg. Com.: " + societate.getRegcom()); // nr reg com
			writerCell = stat.getRow(3).getCell(0);
			writerCell.setCellValue("Strada: " + adresaSocietate.getAdresa()); // adresa
			writerCell = stat.getRow(4).getCell(0);
			writerCell.setCellValue(adresaSocietate.getJudet() + ", " + adresaSocietate.getLocalitate()); // judet +
			// localitate

			// * write luna, an
			writerCell = stat.getRow(4).getCell(11);
			String lunaNume = zileService.getNumeLunaByNr(luna);
			writerCell.setCellValue("- " + lunaNume + " " + an + " -");

			// * write date angajat
			int impozitScutit = 0;

			impozitScutit += realizariRetineri.getImpozitscutit();

			Row row1 = stat.getRow(14);
			Row row2 = stat.getRow(15);
			Row row3 = stat.getRow(16);

			writerCell = row1.getCell(0);
			writerCell.setCellValue(1);

			// * nume, functie, cnp
			writerCell = row1.getCell(1); // nume complet
			writerCell.setCellValue(persoana.getNume() + " " + persoana.getPrenume());
			functieWriter = row2.getCell(1); // functie
			functieWriter.setCellStyle(functieStyle);
			functieWriter.setCellValue(contract.getFunctie());
			writerCell = row3.getCell(1); // cnp
			writerCell.setCellValue(persoana.getCnp());

			writerCell = row3.getCell(2); // nr contract
			writerCell.setCellStyle(nrContractStyle);
			writerCell.setCellValue(contract.getNr());

			// * SALARIU
			salariuWriter = row1.getCell(4); // salariu din contract
			salariuWriter.setCellStyle(salariuStyle);
			salariuWriter.setCellValue(contract.getSalariutarifar());
			salariuWriter = row2.getCell(4); // sume incluse ...
			salariuWriter.setCellValue(0);
			salariuWriter = row3.getCell(4); // spor weekend
			salariuWriter.setCellValue(0);
			salariuWriter = row3.getCell(3); // spor vechime
			salariuWriter.setCellValue(0);

			// * ORE
			writerCell = row1.getCell(5); // NZ
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(8);
			writerCell = row2.getCell(5); // NO
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(contract.getNormalucru());
			writerCell = row3.getCell(5); // SPL = ore suplimentare
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(realizariRetineri.getNroresuplimentare());

			// * ZILE CO
			writerCell = row1.getCell(6); // ZL
			writerCell.setCellStyle(centered);
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(realizariRetineri.getZilelucrate());
			writerCell = row2.getCell(6); // CS
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(coService.getZileCS(luna, an, idcontract));
			writerCell = row2.getCell(7); // CO
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(realizariRetineri.getZilecolucratoare());
			writerCell = row3.getCell(6); // CFP
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(realizariRetineri.getZilecfplucratoare());
			writerCell = row3.getCell(7); // ST
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(coService.getZileST(luna, an, idcontract));

			// * ZILE CM
			int zilecmfnuass = cmService.getZilecmFNUASS(concediiMedicale);
			int zilecmfaambp = cmService.getZilecmFAAMBP(concediiMedicale);
			writerCell = row1.getCell(8); // zile CM
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(realizariRetineri.getZilecmlucratoare() - zilecmfnuass - zilecmfaambp);
			writerCell = row2.getCell(8); // zile FNUASS
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(zilecmfnuass);
			writerCell = row3.getCell(8); // zile FAAMBP
			writerCell.setCellStyle(centered);
			writerCell.setCellValue(zilecmfaambp);

			// * ORE
			writerCell = row1.getCell(9); // ore ind 75%
			writerCell.setCellValue(0);
			writerCell = row2.getCell(9); // ore somaj
			writerCell.setCellValue(0);
			writerCell = row1.getCell(10); // ore lucrate
			writerCell.setCellValue(realizariRetineri.getOrelucrate());
			writerCell = row2.getCell(10); // ore absent
			writerCell.setCellValue(0);
			writerCell = row3.getCell(10); // Val. ore supl.
			writerCell.setCellValue(realizariRetineri.getTotaloresuplimentare());

			// * Salariu
			writerCell = row1.getCell(11); // sal realizat
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getSalariurealizat());
			writerCell = row2.getCell(11); // valoare CO
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getZilecolucratoare() * realizariRetineri.getSalariupezi());
			writerCell = row3.getCell(11); // CO neefect.
			writerCell.setCellValue(0);

			// * CM valoare
			writerCell = row1.getCell(12); // cm societate
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getValcmsocietate());
			writerCell = row2.getCell(12); // CM din FNUASS
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getValcmfnuass());
			writerCell = row3.getCell(12); // cm din FAAMBP
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getValcmfaambp());

			// * drepturi
			writerCell = row1.getCell(13); // total sporuri
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(0);
			writerCell = row2.getCell(13); // total prime
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getPrimabruta());
			writerCell = row3.getCell(13); // alte drepturi
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(0);

			// * drepturi
			writerCell = row1.getCell(14); // val tichetemasa
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getValoaretichete());
			writerCell = row2.getCell(14); // venit brut
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getTotaldrepturi() + realizariRetineri.getValoaretichete());
			writerCell = row3.getCell(14); // somaj 0.5%
			writerCell.setCellValue(0);

			// *
			writerCell = row1.createCell(15); // Val zile libere B
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(0);
			writerCell = row2.createCell(15); // Val zile libere N
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(0);
			writerCell = row3.createCell(15); // Val ind somaj
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(0);

			// *
			writerCell = row1.getCell(16); // CAS
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getCas());
			writerCell = row2.getCell(16); // CASS
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getCass());
			writerCell = row3.getCell(16); // Ded. pers.
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getDeducere());

			// *
			writerCell = row1.getCell(17); // pensie pilon 3
			writerCell.setCellValue(0);
			writerCell = row2.getCell(17); // venit net
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getVenitnet());
			writerCell = row3.getCell(17); // baza impozit
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getBazaimpozit());

			// *
			writerCell = row1.getCell(18); // impozit
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getImpozit());
			writerCell.setCellStyle(salariuStyle);
			writerCell = row2.getCell(18); // rest plata brut
			writerCell.setCellValue(realizariRetineri.getRestplatabrut());
			writerCell.setCellStyle(salariuStyle);
			writerCell = row3.getCell(18); // alte retineri
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(retineri.getImprumuturi());

			// *
			writerCell = row1.getCell(19); // sume neimpozabile
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(0);
			writerCell = row2.getCell(19); // avans
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(retineri.getAvansnet());
			writerCell = row3.getCell(19); // rest plata net
			writerCell.setCellStyle(salariuStyle);
			writerCell.setCellValue(realizariRetineri.getVenitnet() - retineri.getAvansnet() - realizariRetineri.getImpozit());

			// * set borders
			String cellRange = "$A$15:$U$17";
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			// * Impozit scutit cf. art. 60 din CF
			writerCell = stat.getRow(29).getCell(17);
			writerCell.setCellStyle(salariu10Style);
			writerCell.setCellValue(impozitScutit);
			cellRange = "R$30:$T$30";
			setRegionBorder(CellRangeAddress.valueOf(cellRange), stat);

			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			evaluator.evaluateAll();

			// * OUTPUT THE FILE
			Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
			String newFileLocation = String.format("%s/downloads/%d/Stat Salarii - %s %s - %s %d.xlsx", homeLocation, userID, persoana.getNume(), persoana.getPrenume(), lunaNume, an);

			FileOutputStream outputStream = new FileOutputStream(newFileLocation);
			workbook.write(outputStream);
			workbook.close();

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	} // ! createStatIndivitual

}
