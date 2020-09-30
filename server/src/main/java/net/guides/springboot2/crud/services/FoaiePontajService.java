package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderExtent;
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
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Adresa;
import net.guides.springboot2.crud.model.CM;
import net.guides.springboot2.crud.model.CO;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Retineri;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.AdresaRepository;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.CMRepository;
import net.guides.springboot2.crud.repository.CORepository;
import net.guides.springboot2.crud.repository.ContractRepository;
import net.guides.springboot2.crud.repository.OresuplimentareRepository;
import net.guides.springboot2.crud.repository.PersoanaRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class FoaiePontajService {
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
	@Autowired
	private AngajatRepository angajatRepository;
	@Autowired
	private CORepository coRepository;
	@Autowired
	private CMRepository cmRepository;
	@Autowired
	private OresuplimentareRepository oresuplimentareRepository;

	private String homeLocation = "D:\\code\\webapp-spring\\server\\src\\main\\java\\net\\guides\\springboot2\\crud\\";

	private void setRegionBorder(CellRangeAddress region, Sheet sheet) {
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
	}

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
			int oresuplimentare = realizariRetineri.getNroresuplimentare();

			// get weekends
			int weekdayNr;
			int nrZileLuna = YearMonth.of(an, luna).lengthOfMonth();
			int[] oreInZiua = new int[nrZileLuna+1];
			Arrays.fill(oreInZiua, 8);
			LocalDate _zi;
			for(int i=1; i <= nrZileLuna; ++i) {
					_zi = LocalDate.of(an, luna, i);

					weekdayNr = _zi.getDayOfWeek().getValue();
					if(weekdayNr == 6 || weekdayNr == 7)
							oreInZiua[i] = 0;
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

			//* write ore 1-15
			for(int i = 1; i <= 15; ++i) {
				writerCell = row.createCell(3+i);
				writerCell.setCellValue(oreInZiua[i]);
			}
			//* Total 1-15 = formula
			writerCell = row.createCell(19);
			writerCell.setCellFormula("SUM($E$"+(15+nrAngajat)+":$S$"+(15+nrAngajat)+")");
			

			//* write ore 16-31
			for(int i = 16; i <= nrZileLuna; ++i) {
				writerCell = row.createCell(4+i);
				writerCell.setCellValue(oreInZiua[i]);
			}

			//* evaluate all formulas
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			evaluator.evaluateAll();

			//* set borders
			String cellRange = "$A$"+(15+nrAngajat)+":$BF$"+(15+nrAngajat);
			allCellsBordered.drawBorders(CellRangeAddress.valueOf(cellRange), BorderStyle.THIN, BorderExtent.ALL);
			allCellsBordered.applyBorders(sheet);

			nrAngajat++;
		}

		Files.createDirectories(Paths.get(homeLocation + "downloads\\" + userID));
		String newFileLocation = String.format("%s\\downloads\\%d\\Foaie Pontaj - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);
		
		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}
}
