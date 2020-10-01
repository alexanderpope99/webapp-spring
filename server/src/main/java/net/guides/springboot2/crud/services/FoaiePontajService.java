package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderExtent;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.CM;
import net.guides.springboot2.crud.model.CO;
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.CMRepository;
import net.guides.springboot2.crud.repository.CORepository;
import net.guides.springboot2.crud.repository.OresuplimentareRepository;
import net.guides.springboot2.crud.repository.PersoanaRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class FoaiePontajService {
	@Autowired
	private RealizariRetineriService realizariRetineriService;
	@Autowired
	private ZileService zileService;

	@Autowired
	private PersoanaRepository persoanaRepository;
	@Autowired
	private SocietateRepository societateRepository;
	@Autowired
	private AngajatRepository angajatRepository;
	@Autowired
	private CORepository coRepository;
	@Autowired
	private CMRepository cmRepository;
	@Autowired
	private OresuplimentareRepository oresuplimentareRepository;

	private String homeLocation = "src\\main\\java\\net\\guides\\springboot2\\crud\\";

	public boolean createFoaiePontaj(int luna, int an, int idsocietate, long userID) throws IOException, ResourceNotFoundException {
		Societate societate = societateRepository.findById((long) idsocietate)
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + idsocietate));

		List<Persoana> persoane = persoanaRepository.getPersoanaByIdsocietateWithContract(idsocietate);

		String statTemplateLocation = homeLocation + "\\templates";
		
		FileInputStream file = new FileInputStream(new File(statTemplateLocation, "FoaiePontaj.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet sheet = workbook.getSheetAt(0);

		Cell writerCell = sheet.getRow(0).getCell(0);
		writerCell.setCellValue("Unitatea: " + societate.getNume());

		//* luna, an
		// Row a = sheet.getRow(4);
		writerCell = sheet.getRow(4).getCell(17);
		String lunaNume = zileService.getNumeLunaByNr(luna);
		writerCell.setCellValue("pentru luna " + lunaNume + " anul " + an);

		//? write angajati
		int nrAngajat = 0;
		for(Persoana persoana : persoane) {
			int rowNr = 14 + nrAngajat;

			Row row = sheet.createRow(rowNr);
			
			//* Nr. Crt.
			writerCell = row.createCell(0);
			writerCell.setCellValue(nrAngajat + 1);
			// set border

			//* Nume angajat
			writerCell = row.createCell(1);
			writerCell.setCellValue(persoana.getNume() + " " + persoana.getPrenume());

			//* nr de marca ??

			// set border
			PropertyTemplate allCellsBordered = new PropertyTemplate();
			
			long idcontract = angajatRepository.findIdcontractByIdpersoana(persoana.getId());
			
			RealizariRetineri realizariRetineri = realizariRetineriService.saveRealizariRetineri(luna, an, idcontract);

			// get concediu odihna
			List<CO> co = coRepository.findByIdcontract(idcontract);
			// get concediu medical
			List<CM> cm = cmRepository.findByIdcontract(idcontract);
			// get oresuplimentare
			int oresuplimentare200 = oresuplimentareRepository.countByIdstatsalariatAndProcent(realizariRetineri.getId(), 200);
			int oresuplimentare175 = oresuplimentareRepository.countByIdstatsalariatAndProcent(realizariRetineri.getId(), 175);
			int oresuplimentare150 = oresuplimentareRepository.countByIdstatsalariatAndProcent(realizariRetineri.getId(), 150);
			int oresuplimentare100 = oresuplimentareRepository.countByIdstatsalariatAndProcent(realizariRetineri.getId(), 100);

			// get weekends
			int weekdayNr;
			int nrZileLuna = YearMonth.of(an, luna).lengthOfMonth();
			int[] oreInZiua = new int[nrZileLuna+1];
			boolean[] ziLibera = new boolean[nrZileLuna+1];
			Arrays.fill(oreInZiua, 8);
			LocalDate _zi;
			for(int i=1; i <= nrZileLuna; ++i) {
					_zi = LocalDate.of(an, luna, i);

					weekdayNr = _zi.getDayOfWeek().getValue();
					if(weekdayNr == 6 || weekdayNr == 7) {
							oreInZiua[i] = 0;
							ziLibera[i] = true;
					}
			}
			// get zile concediu odihna
			LocalDate dela, panala;
			for(CO concediu : co) {
				dela = concediu.getDela();
				panala = concediu.getPanala();

				for(int i=1; i <= nrZileLuna; ++i) {
						_zi = LocalDate.of(an, luna, i);
						if(_zi.compareTo(dela) >= 0 && _zi.compareTo(panala) <= 0)
								oreInZiua[i] = 0;
				}
			}
			// get zile concediu medical
			for(CM concediu : cm) {
				dela = concediu.getDela();
				panala = concediu.getPanala();

				for(int i=1; i <= nrZileLuna; ++i) {
						_zi = LocalDate.of(an, luna, i);
						if(_zi.compareTo(dela) >= 0 && _zi.compareTo(panala) <= 0)
								oreInZiua[i] = 0;
				}
			}

			// should get zile sarbatori here

			CellStyle greyed = workbook.createCellStyle();
			greyed.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			greyed.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			//* write ore 1-15
			for(int i = 1; i <= 15; ++i) {
				writerCell = row.createCell(3+i);
				writerCell.setCellValue(oreInZiua[i]);
				if(ziLibera[i])
					writerCell.setCellStyle(greyed);
			}
			//* Total 1-15 = formula
			writerCell = row.createCell(19);
			writerCell.setCellFormula("SUM($E$"+(rowNr+1)+":$S$"+(rowNr+1)+")");
			
			//* write ore 16-31
			for(int i = 16; i <= nrZileLuna; ++i) {
				writerCell = row.createCell(4+i);
				writerCell.setCellValue(oreInZiua[i]);
				if(ziLibera[i])
					writerCell.setCellStyle(greyed);
			}
			//* Total ore lucrate
			writerCell = row.createCell(36);
			writerCell.setCellFormula("SUM($T$"+(rowNr+1)+":$AJ$"+(rowNr+1)+")"
															 +"+SUM($AL"+(rowNr+1)+":$AP$"+(rowNr+1)+")");

			//* ore suplimentare 200%
			writerCell = row.createCell(37);
			writerCell.setCellValue(oresuplimentare200);
			//* ore suplimentare 175%
			writerCell = row.createCell(38);
			writerCell.setCellValue(oresuplimentare175);
			//* ore suplimentare 150%
			writerCell = row.createCell(39);
			writerCell.setCellValue(oresuplimentare150);
			//* ore suplimentare 100%
			writerCell = row.createCell(40);
			writerCell.setCellValue(oresuplimentare100);
			//* ore noapte
			writerCell = row.createCell(41);
			writerCell.setCellValue(0);

			//* ore normate
			writerCell = row.createCell(42);
			writerCell.setCellFormula( "$AK$"+(rowNr+1)+"-SUM($AL$"+(rowNr+1)+":$AP$"+(rowNr+1)+")" );
		
			int norma = realizariRetineri.getDuratazilucru();
			//* ore nelucrate
			writerCell = row.createCell(43);
			int nrZileNelucrate =  zileService.getZileLucratoareInLunaAnul(luna, an) - realizariRetineri.getZilelucrate();
			writerCell.setCellValue(nrZileNelucrate * norma);

			//* ore Intr
			writerCell = row.createCell(44);
			writerCell.setCellValue(0);

			//* ore Co <- nu include CFP
			int nrZileCO = realizariRetineri.getZilecolucratoare() - realizariRetineri.getZileconeplatitlucratoare();
			writerCell = row.createCell(45);
			writerCell.setCellValue(nrZileCO * norma);

			//* ore Bo = boala obisnuita
			int nrZileCM = realizariRetineri.getZilecmlucratoare();
			writerCell = row.createCell(46);
			writerCell.setCellValue(nrZileCM * norma);

			//* ore Bp = boala profesionala
			writerCell = row.createCell(47);
			writerCell.setCellValue(0);

			//* ore Am = accidente de munca
			writerCell = row.createCell(48);
			writerCell.setCellValue(0);

			//* ore M = maternitate
			writerCell = row.createCell(49);
			writerCell.setCellValue(0);

			//* ore I = invoiri
			writerCell = row.createCell(50);
			writerCell.setCellValue(0);

			//* ore O = obligatii cetatenesti
			writerCell = row.createCell(51);
			writerCell.setCellValue(0);

			//* ore N = absente nemotivate
			writerCell = row.createCell(52);
			writerCell.setCellValue(0);

			//* ore Prm = program redus maternitate
			writerCell = row.createCell(53);
			writerCell.setCellValue(0);

			//* ore Prb = program redus boala
			writerCell = row.createCell(54);
			writerCell.setCellValue(0);

			//* ore CFP
			int nrZileCFP = realizariRetineri.getZileconeplatitlucratoare();
			writerCell = row.createCell(55);
			writerCell.setCellValue(nrZileCFP * norma);

			//* ore CS = concediu suplimentar
			writerCell = row.createCell(56);
			writerCell.setCellValue(0);

			/*------------ENDING------------*/
			//* autofit coloana cu nume
			sheet.autoSizeColumn(1);

			//* evaluate all formulas
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			evaluator.evaluateAll();

			//* set borders
			String cellRange = "$A$"+(rowNr+1)+":$BE$"+(rowNr+1);
			allCellsBordered.drawBorders(CellRangeAddress.valueOf(cellRange), BorderStyle.THIN, BorderExtent.ALL);
			allCellsBordered.applyBorders(sheet);

			nrAngajat++;
		} //! for loop end

		Files.createDirectories(Paths.get(homeLocation + "downloads\\" + userID));
		String newFileLocation = String.format("%s\\downloads\\%d\\Foaie Pontaj - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);
		
		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}
}
