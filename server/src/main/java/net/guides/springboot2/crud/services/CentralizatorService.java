package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Period;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class CentralizatorService {

	@Autowired
	SocietateRepository societateRepository;

	@Autowired
	AngajatRepository angajatRepository;

	@Autowired
	ZileService zileService;

	@Autowired
	RealizariRetineriService realizariRetineriService;
	
	private String homeLocation = "src/main/java/net/guides/springboot2/crud/";

	private static double roundAvoid(double value, int places) {
		double scale = Math.pow(10, places);
		return Math.round(value * scale) / scale;
	}

	public boolean createCentralizatorVarsta(int luna, int an, int idsocietate, int userID) throws IOException, ResourceNotFoundException {

		Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
		List<Angajat> angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(idsocietate);

		String centralizatorVarstaTemplateLocation = homeLocation + "/templates";

		FileInputStream file = new FileInputStream(new File(centralizatorVarstaTemplateLocation, "Centralizator pe varsta.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet listaAngajati = workbook.getSheetAt(0);

		Cell writerCell = listaAngajati.getRow(0).getCell(0);
		writerCell.setCellValue(societate.getNume()); // nume soc
		writerCell = listaAngajati.getRow(1).getCell(0);
		writerCell.setCellValue("CUI: " + societate.getCif()); // cif
		writerCell = listaAngajati.getRow(2).getCell(0);
		writerCell.setCellValue("Nr. reg. com: " + societate.getRegcom());// reg com
		writerCell = listaAngajati.getRow(3).getCell(0);
		writerCell.setCellValue(societate.getAdresa().getLocalitate()+", "+societate.getAdresa().getJudet());//judet,localitate
		writerCell = listaAngajati.getRow(4).getCell(0);
		writerCell.setCellValue(societate.getAdresa().getAdresa());//judet,localitate

		writerCell = listaAngajati.getRow(6).getCell(2);
		String lunaNume = zileService.getNumeLunaByNr(luna);
		writerCell.setCellValue("luna " + lunaNume + " anul " + an);

		CellStyle centered = workbook.createCellStyle();
		CellStyle left = workbook.createCellStyle();
		CellStyle right = workbook.createCellStyle();
		centered.setAlignment(HorizontalAlignment.CENTER);
		left.setAlignment(HorizontalAlignment.LEFT);
		right.setAlignment(HorizontalAlignment.RIGHT);

		int[] nr={0,0,0,0};
		float[] vb={0,0,0,0};
		float[] vn={0,0,0,0};
		float[] im={0,0,0,0};

		
		for (Angajat ang : angajati) {
			RealizariRetineri realizariRetineri = realizariRetineriService.saveOrGetRealizariRetineri(luna, an, ang.getContract().getId());
			int varsta=ang.getPersoana().getVarsta();
			int i=-1;
			if(varsta>=0 && varsta<20)i=0;
			else if (varsta>=20 && varsta<30)i=1;
			else if(varsta>=30 && varsta<40)i=2;
			else if(varsta>=40 && varsta<50)i=3;
			else i=-1;

			if(i!=-1)
			{
			nr[i]+=1;
			vb[i]+=realizariRetineri.getVenitbrut();
			vn[i]+=realizariRetineri.getVenitnet();
			im[i]+=realizariRetineri.getImpozit();
			}
		}

		for(int i=0;i<=3;i++)
		{
			writerCell = listaAngajati.getRow(10+i).getCell(1);
			writerCell.setCellValue(nr[i]); // numar interval
			writerCell = listaAngajati.getRow(10+i).getCell(2);
			writerCell.setCellValue(vb[i]); // venit brut
			writerCell = listaAngajati.getRow(10+i).getCell(3);
			writerCell.setCellValue(nr[i]!=0?(roundAvoid(((double)vb[i])/nr[i],2)):0); // venit brut mediu
			writerCell = listaAngajati.getRow(10+i).getCell(4);
			writerCell.setCellValue(vn[i]); // venit net
			writerCell = listaAngajati.getRow(10+i).getCell(5);
			writerCell.setCellValue(nr[i]!=0?(roundAvoid(((double)vn[i])/nr[i],2)):0); // venit net mediu
			writerCell = listaAngajati.getRow(10+i).getCell(6);
			writerCell.setCellValue(im[i]); // impozit
		}




		/* ------ ENDING ------ **/
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String newFileLocation = String.format("%s/downloads/%d/Centralizator Vârstă Rezumat - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}

	public boolean createCentralizatorVarstaComplet(int luna, int an, int idsocietate, int userID) throws IOException, ResourceNotFoundException {

		Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
		List<Angajat> angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(idsocietate);
		angajati.sort((Angajat a1, Angajat a2) -> a1.getPersoana().getVarsta().compareTo(a2.getPersoana().getVarsta()));

		String centralizatorVarstaTemplateLocation = homeLocation + "/templates";

		FileInputStream file = new FileInputStream(new File(centralizatorVarstaTemplateLocation, "Centralizator pe varsta complet.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet listaAngajati = workbook.getSheetAt(0);

		Cell writerCell = listaAngajati.getRow(0).getCell(0);
		writerCell.setCellValue(societate.getNume()); // nume soc
		writerCell = listaAngajati.getRow(1).getCell(0);
		writerCell.setCellValue("CUI: " + societate.getCif()); // cif
		writerCell = listaAngajati.getRow(2).getCell(0);
		writerCell.setCellValue("Nr. reg. com: " + societate.getRegcom());// reg com
		writerCell = listaAngajati.getRow(3).getCell(0);
		writerCell.setCellValue(societate.getAdresa().getLocalitate()+", "+societate.getAdresa().getJudet());//judet,localitate
		writerCell = listaAngajati.getRow(4).getCell(0);
		writerCell.setCellValue(societate.getAdresa().getAdresa());//judet,localitate

		writerCell = listaAngajati.getRow(6).getCell(3);
		String lunaNume = zileService.getNumeLunaByNr(luna);
		writerCell.setCellValue("luna " + lunaNume + " anul " + an);

		CellStyle centered = workbook.createCellStyle();
		CellStyle left = workbook.createCellStyle();
		CellStyle right = workbook.createCellStyle();
		CellStyle cellRightMiddle = workbook.createCellStyle();
		centered.setAlignment(HorizontalAlignment.CENTER);
		left.setAlignment(HorizontalAlignment.LEFT);
		right.setAlignment(HorizontalAlignment.RIGHT);
		cellRightMiddle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellRightMiddle.setAlignment(HorizontalAlignment.RIGHT);

		int[] interval={0,20,30,40,50};
		int nrRow=0;

		for(int i=0;i<interval.length-1;i++)
		{
		int nrCrt=0,sumavb=0,sumavn=0;
		for (Angajat ang : angajati) {
			RealizariRetineri realizariRetineri = realizariRetineriService.saveOrGetRealizariRetineri(luna, an, ang.getContract().getId());
			int varsta=ang.getPersoana().getVarsta();
			Row row=listaAngajati.createRow(10+nrRow);

			if(varsta>=interval[i] && varsta<interval[i+1])
			{
				writerCell = row.createCell(1);
				writerCell.setCellValue(nrCrt+1);
				writerCell = row.createCell(2);
				writerCell.setCellValue(ang.getPersoana().getNume());
				writerCell = row.createCell(3);
				writerCell.setCellValue(ang.getPersoana().getPrenume());
				writerCell = row.createCell(4);
				writerCell.setCellValue(ang.getPersoana().getCnp());
				writerCell = row.createCell(5);
				writerCell.setCellValue(realizariRetineri.getVenitbrut());
				sumavb+=realizariRetineri.getVenitbrut();
				writerCell = row.createCell(7);
				writerCell.setCellValue(realizariRetineri.getVenitnet());
				sumavn+=realizariRetineri.getVenitnet();
				writerCell = row.createCell(9);
				writerCell.setCellValue(realizariRetineri.getImpozit());
				nrCrt++;
				nrRow++;
			}

		}
		if(nrCrt==0)
		{
			Row row=listaAngajati.createRow(10+nrRow);
			writerCell = row.createCell(0);
			writerCell.setCellValue(String.valueOf(interval[i])+"-"+String.valueOf(interval[i+1]));
			writerCell.setCellStyle(right);
			writerCell = row.createCell(1);
			writerCell.setCellValue(0);
			writerCell = row.createCell(2);
			writerCell.setCellValue("-");
			writerCell.setCellStyle(centered);
			writerCell = row.createCell(3);
			writerCell.setCellValue("-");
			writerCell.setCellStyle(centered);
			writerCell = row.createCell(4);
			writerCell.setCellValue("-");
			writerCell.setCellStyle(centered);
			writerCell = row.createCell(5);
			writerCell.setCellValue(0);
			writerCell = row.createCell(6);
			writerCell.setCellValue(0);
			writerCell = row.createCell(7);
			writerCell.setCellValue(0);
			writerCell = row.createCell(8);
			writerCell.setCellValue(0);
			writerCell = row.createCell(9);
			writerCell.setCellValue(0);
			nrRow++;
		}
		else
		{
			Row row=listaAngajati.getRow(10+nrRow-nrCrt);
			writerCell = row.createCell(0);
			writerCell.setCellValue(String.valueOf(interval[i])+"-"+String.valueOf(interval[i+1]));
			writerCell.setCellStyle(right);
			writerCell = row.createCell(6);
			writerCell.setCellValue(roundAvoid(((double)sumavb)/nrCrt,2));
			writerCell = row.createCell(8);
			writerCell.setCellValue(roundAvoid(((double)sumavn)/nrCrt,2));
			if(nrCrt>1)
			{
			listaAngajati.addMergedRegion(new CellRangeAddress(nrRow-nrCrt+10, nrRow-1+10, 0,0));
			listaAngajati.addMergedRegion(new CellRangeAddress(nrRow-nrCrt+10, nrRow-1+10, 6,6));
			listaAngajati.addMergedRegion(new CellRangeAddress(nrRow-nrCrt+10, nrRow-1+10, 8,8));
			writerCell=row.getCell(0);
			writerCell.setCellStyle(cellRightMiddle);
			writerCell=row.getCell(6);
			writerCell.setCellStyle(cellRightMiddle);
			writerCell=row.getCell(8);
			writerCell.setCellStyle(cellRightMiddle);
			}
		}
		}


		listaAngajati.autoSizeColumn(2);
		listaAngajati.autoSizeColumn(3);
		listaAngajati.autoSizeColumn(4);



		/* ------ ENDING ------ **/
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String newFileLocation = String.format("%s/downloads/%d/Centralizator Vârstă Complet - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}

	public boolean createCentralizatorSex(int luna, int an, int idsocietate, int userID) throws IOException, ResourceNotFoundException {

		Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
		List<Angajat> angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(idsocietate);

		String centralizatorVarstaTemplateLocation = homeLocation + "/templates";

		FileInputStream file = new FileInputStream(new File(centralizatorVarstaTemplateLocation, "Centralizator pe sex.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet listaAngajati = workbook.getSheetAt(0);

		Cell writerCell = listaAngajati.getRow(0).getCell(0);
		writerCell.setCellValue(societate.getNume()); // nume soc
		writerCell = listaAngajati.getRow(1).getCell(0);
		writerCell.setCellValue("CUI: " + societate.getCif()); // cif
		writerCell = listaAngajati.getRow(2).getCell(0);
		writerCell.setCellValue("Nr. reg. com: " + societate.getRegcom());// reg com
		writerCell = listaAngajati.getRow(3).getCell(0);
		writerCell.setCellValue(societate.getAdresa().getLocalitate()+societate.getAdresa().getJudet());//judet,localitate
		writerCell = listaAngajati.getRow(4).getCell(0);
		writerCell.setCellValue(societate.getAdresa().getAdresa());//judet,localitate

		writerCell = listaAngajati.getRow(6).getCell(2);
		String lunaNume = zileService.getNumeLunaByNr(luna);
		writerCell.setCellValue("luna " + lunaNume + " anul " + an);

		CellStyle centered = workbook.createCellStyle();
		CellStyle left = workbook.createCellStyle();
		CellStyle right = workbook.createCellStyle();
		centered.setAlignment(HorizontalAlignment.CENTER);
		left.setAlignment(HorizontalAlignment.LEFT);
		right.setAlignment(HorizontalAlignment.RIGHT);

		int[] nr={0,0};
		float[] vb={0,0};
		float[] vn={0,0};
		float[] im={0,0};

		
		for (Angajat ang : angajati) {
			RealizariRetineri realizariRetineri = realizariRetineriService.saveOrGetRealizariRetineri(luna, an, ang.getContract().getId());
			char sex=(ang.getPersoana().getCnp()).charAt(0);
			int x = (int)sex - 48;
			int i=-1;
			if(x%2!=0)i=0;
			else if(x%2==0)i=1;

			nr[i]+=1;
			vb[i]+=realizariRetineri.getVenitbrut();
			vn[i]+=realizariRetineri.getVenitnet();
			im[i]+=realizariRetineri.getImpozit();
			
		}

		for(int i=0;i<=1;i++)
		{
			writerCell = listaAngajati.getRow(10+i).getCell(1);
			writerCell.setCellValue(nr[i]); // numar interval
			writerCell = listaAngajati.getRow(10+i).getCell(2);
			writerCell.setCellValue(vb[i]); // venit brut
			writerCell = listaAngajati.getRow(10+i).getCell(3);
			writerCell.setCellValue(nr[i]!=0?roundAvoid(((double)vb[i])/nr[i],2):0); // venit brut mediu
			writerCell = listaAngajati.getRow(10+i).getCell(4);
			writerCell.setCellValue(vn[i]); // venit net
			writerCell = listaAngajati.getRow(10+i).getCell(5);
			writerCell.setCellValue(nr[i]!=0?roundAvoid(((double)vn[i])/nr[i],2):0); // venit net mediu
			writerCell = listaAngajati.getRow(10+i).getCell(6);
			writerCell.setCellValue(im[i]); // impozit
		}

		/* ------ ENDING ------ **/
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String newFileLocation = String.format("%s/downloads/%d/Centralizator Sex Rezumat - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}

	public boolean createCentralizatorSexComplet(int luna, int an, int idsocietate, int userID) throws IOException, ResourceNotFoundException {

		Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
		List<Angajat> angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(idsocietate);

		String centralizatorVarstaTemplateLocation = homeLocation + "/templates";

		FileInputStream file = new FileInputStream(new File(centralizatorVarstaTemplateLocation, "Centralizator pe sex complet.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet listaAngajati = workbook.getSheetAt(0);

		Cell writerCell = listaAngajati.getRow(0).getCell(0);
		writerCell.setCellValue(societate.getNume()); // nume soc
		writerCell = listaAngajati.getRow(1).getCell(0);
		writerCell.setCellValue("CUI: " + societate.getCif()); // cif
		writerCell = listaAngajati.getRow(2).getCell(0);
		writerCell.setCellValue("Nr. reg. com: " + societate.getRegcom());// reg com
		writerCell = listaAngajati.getRow(3).getCell(0);
		writerCell.setCellValue(societate.getAdresa().getLocalitate()+societate.getAdresa().getJudet());//judet,localitate
		writerCell = listaAngajati.getRow(4).getCell(0);
		writerCell.setCellValue(societate.getAdresa().getAdresa());//judet,localitate

		writerCell = listaAngajati.getRow(6).getCell(3);
		String lunaNume = zileService.getNumeLunaByNr(luna);
		writerCell.setCellValue("luna " + lunaNume + " anul " + an);

		CellStyle centered = workbook.createCellStyle();
		CellStyle left = workbook.createCellStyle();
		CellStyle right = workbook.createCellStyle();
		CellStyle cellRightMiddle = workbook.createCellStyle();
		centered.setAlignment(HorizontalAlignment.CENTER);
		left.setAlignment(HorizontalAlignment.LEFT);
		right.setAlignment(HorizontalAlignment.RIGHT);
		cellRightMiddle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellRightMiddle.setAlignment(HorizontalAlignment.RIGHT);

		int nrRow=0;

		for(int i=0;i<=1;i++)
		{
		int nrCrt=0,sumavb=0,sumavn=0;
		for (Angajat ang : angajati) {
			RealizariRetineri realizariRetineri = realizariRetineriService.saveOrGetRealizariRetineri(luna, an, ang.getContract().getId());
			char sex=(ang.getPersoana().getCnp()).charAt(0);
			int x = (int)sex - 48;
			Row row=listaAngajati.createRow(10+nrRow);

			if(x%2==i)
			{
				writerCell = row.createCell(1);
				writerCell.setCellValue(nrCrt+1);
				writerCell = row.createCell(2);
				writerCell.setCellValue(ang.getPersoana().getNume());
				writerCell = row.createCell(3);
				writerCell.setCellValue(ang.getPersoana().getPrenume());
				writerCell = row.createCell(4);
				writerCell.setCellValue(ang.getPersoana().getCnp());
				writerCell = row.createCell(5);
				writerCell.setCellValue(realizariRetineri.getVenitbrut());
				sumavb+=realizariRetineri.getVenitbrut();
				writerCell = row.createCell(7);
				writerCell.setCellValue(realizariRetineri.getVenitnet());
				sumavn+=realizariRetineri.getVenitnet();
				writerCell = row.createCell(9);
				writerCell.setCellValue(realizariRetineri.getImpozit());
				nrCrt++;
				nrRow++;
			}

		}
		if(nrCrt==0)
		{
			Row row=listaAngajati.createRow(10+nrRow);
			writerCell = row.createCell(0);
			if(i==0)writerCell.setCellValue("Masculin");
			else writerCell.setCellValue("Feminin");
			writerCell.setCellStyle(centered);
			writerCell = row.createCell(1);
			writerCell.setCellValue(0);
			writerCell = row.createCell(2);
			writerCell.setCellValue("-");
			writerCell.setCellStyle(centered);
			writerCell = row.createCell(3);
			writerCell.setCellValue("-");
			writerCell.setCellStyle(centered);
			writerCell = row.createCell(4);
			writerCell.setCellValue("-");
			writerCell.setCellStyle(centered);
			writerCell = row.createCell(5);
			writerCell.setCellValue(0);
			writerCell = row.createCell(6);
			writerCell.setCellValue(0);
			writerCell = row.createCell(7);
			writerCell.setCellValue(0);
			writerCell = row.createCell(8);
			writerCell.setCellValue(0);
			writerCell = row.createCell(9);
			writerCell.setCellValue(0);
			nrRow++;
		}
		else
		{
			Row row=listaAngajati.getRow(10+nrRow-nrCrt);
			writerCell = row.createCell(0);
			if(i==0)writerCell.setCellValue("Masculin");
			else writerCell.setCellValue("Feminin");
			writerCell.setCellStyle(centered);
			writerCell = row.createCell(6);
			writerCell.setCellValue(roundAvoid(((double)sumavb)/nrCrt,2));
			writerCell = row.createCell(8);
			writerCell.setCellValue(roundAvoid(((double)sumavn)/nrCrt,2));
			if(nrCrt>1)
			{
			listaAngajati.addMergedRegion(new CellRangeAddress(nrRow-nrCrt+10, nrRow-1+10, 0,0));
			listaAngajati.addMergedRegion(new CellRangeAddress(nrRow-nrCrt+10, nrRow-1+10, 6,6));
			listaAngajati.addMergedRegion(new CellRangeAddress(nrRow-nrCrt+10, nrRow-1+10, 8,8));
			writerCell=row.getCell(0);
			writerCell.setCellStyle(cellRightMiddle);
			writerCell=row.getCell(6);
			writerCell.setCellStyle(cellRightMiddle);
			writerCell=row.getCell(8);
			writerCell.setCellStyle(cellRightMiddle);
			}
		}
		}


		listaAngajati.autoSizeColumn(2);
		listaAngajati.autoSizeColumn(3);
		listaAngajati.autoSizeColumn(4);




		/* ------ ENDING ------ **/
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String newFileLocation = String.format("%s/downloads/%d/Centralizator Sex Complet - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}

	public boolean createCentralizatorVechime(int luna, int an, int idsocietate, int userID) throws IOException, ResourceNotFoundException {

		Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
		List<Angajat> angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(idsocietate);

		String centralizatorVarstaTemplateLocation = homeLocation + "/templates";

		FileInputStream file = new FileInputStream(new File(centralizatorVarstaTemplateLocation, "Centralizator pe vechime.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet listaAngajati = workbook.getSheetAt(0);

		Cell writerCell = listaAngajati.getRow(0).getCell(0);
		writerCell.setCellValue(societate.getNume()); // nume soc
		writerCell = listaAngajati.getRow(1).getCell(0);
		writerCell.setCellValue("CUI: " + societate.getCif()); // cif
		writerCell = listaAngajati.getRow(2).getCell(0);
		writerCell.setCellValue("Nr. reg. com: " + societate.getRegcom());// reg com
		writerCell = listaAngajati.getRow(3).getCell(0);
		writerCell.setCellValue(societate.getAdresa().getLocalitate()+societate.getAdresa().getJudet());//judet,localitate
		writerCell = listaAngajati.getRow(4).getCell(0);
		writerCell.setCellValue(societate.getAdresa().getAdresa());//judet,localitate

		writerCell = listaAngajati.getRow(6).getCell(2);
		String lunaNume = zileService.getNumeLunaByNr(luna);
		writerCell.setCellValue("luna " + lunaNume + " anul " + an);

		CellStyle centered = workbook.createCellStyle();
		CellStyle left = workbook.createCellStyle();
		CellStyle right = workbook.createCellStyle();
		centered.setAlignment(HorizontalAlignment.CENTER);
		left.setAlignment(HorizontalAlignment.LEFT);
		right.setAlignment(HorizontalAlignment.RIGHT);

		int[] nr={0,0,0,0,0,0};
		float[] vb={0,0,0,0,0,0};
		float[] vn={0,0,0,0,0,0};
		float[] im={0,0,0,0,0,0};

		
		for (Angajat ang : angajati) {
			RealizariRetineri realizariRetineri = realizariRetineriService.saveOrGetRealizariRetineri(luna, an, ang.getContract().getId());
			int vechime=Period.between(ang.getContract().getData(), LocalDate.now()).getYears();
			int i=-1;
			if(vechime>=0 && vechime<5)i=0;
			else if (vechime>=5 && vechime<10)i=1;
			else if(vechime>=10 && vechime<15)i=2;
			else if(vechime>=15 && vechime<20)i=3;
			else if(vechime>=20 && vechime<25)i=4;
			else if(vechime>=25 && vechime<30)i=5;
			else i=-1;

			if(i!=-1)
			{
			nr[i]+=1;
			vb[i]+=realizariRetineri.getVenitbrut();
			vn[i]+=realizariRetineri.getVenitnet();
			im[i]+=realizariRetineri.getImpozit();
			}
		}

		for(int i=0;i<=5;i++)
		{
			writerCell = listaAngajati.getRow(10+i).getCell(1);
			writerCell.setCellValue(nr[i]); // numar interval
			writerCell = listaAngajati.getRow(10+i).getCell(2);
			writerCell.setCellValue(vb[i]); // venit brut
			writerCell = listaAngajati.getRow(10+i).getCell(3);
			writerCell.setCellValue(nr[i]!=0?roundAvoid(((double)vb[i])/nr[i],2):0); // venit brut mediu
			writerCell = listaAngajati.getRow(10+i).getCell(4);
			writerCell.setCellValue(vn[i]); // venit net
			writerCell = listaAngajati.getRow(10+i).getCell(5);
			writerCell.setCellValue(nr[i]!=0?roundAvoid(((double)vn[i])/nr[i],2):0); // venit net mediu
			writerCell = listaAngajati.getRow(10+i).getCell(6);
			writerCell.setCellValue(im[i]); // impozit
		}





		/* ------ ENDING ------ **/
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String newFileLocation = String.format("%s/downloads/%d/Centralizator Vechime Rezumat - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}

	public boolean createCentralizatorVechimeComplet(int luna, int an, int idsocietate, int userID) throws IOException, ResourceNotFoundException {

		Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
		List<Angajat> angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(idsocietate);

		String centralizatorVarstaTemplateLocation = homeLocation + "/templates";

		FileInputStream file = new FileInputStream(new File(centralizatorVarstaTemplateLocation, "Centralizator pe vechime complet.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet listaAngajati = workbook.getSheetAt(0);

		Cell writerCell = listaAngajati.getRow(0).getCell(0);
		writerCell.setCellValue(societate.getNume()); // nume soc
		writerCell = listaAngajati.getRow(1).getCell(0);
		writerCell.setCellValue("CUI: " + societate.getCif()); // cif
		writerCell = listaAngajati.getRow(2).getCell(0);
		writerCell.setCellValue("Nr. reg. com: " + societate.getRegcom());// reg com
		writerCell = listaAngajati.getRow(3).getCell(0);
		writerCell.setCellValue(societate.getAdresa().getLocalitate()+", "+societate.getAdresa().getJudet());//judet,localitate
		writerCell = listaAngajati.getRow(4).getCell(0);
		writerCell.setCellValue(societate.getAdresa().getAdresa());//judet,localitate

		writerCell = listaAngajati.getRow(6).getCell(3);
		String lunaNume = zileService.getNumeLunaByNr(luna);
		writerCell.setCellValue("luna " + lunaNume + " anul " + an);

		CellStyle centered = workbook.createCellStyle();
		CellStyle left = workbook.createCellStyle();
		CellStyle right = workbook.createCellStyle();
		CellStyle cellRightMiddle = workbook.createCellStyle();
		centered.setAlignment(HorizontalAlignment.CENTER);
		left.setAlignment(HorizontalAlignment.LEFT);
		right.setAlignment(HorizontalAlignment.RIGHT);
		cellRightMiddle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellRightMiddle.setAlignment(HorizontalAlignment.RIGHT);

		int nrRow=0;

		int[] interval={0,1,3,5,7,8,50};

		for(int i=0;i<interval.length-1;i++)
		{
		int nrCrt=0,sumavb=0,sumavn=0;
		for (Angajat ang : angajati) {
			RealizariRetineri realizariRetineri = realizariRetineriService.saveOrGetRealizariRetineri(luna, an, ang.getContract().getId());
			int vechime=Period.between(ang.getContract().getData(), LocalDate.now()).getYears();
			Row row=listaAngajati.createRow(10+nrRow);

			if(vechime>=interval[i] && vechime<interval[i+1])
			{
				writerCell = row.createCell(1);
				writerCell.setCellValue(nrCrt+1);
				writerCell = row.createCell(2);
				writerCell.setCellValue(ang.getPersoana().getNume());
				writerCell = row.createCell(3);
				writerCell.setCellValue(ang.getPersoana().getPrenume());
				writerCell = row.createCell(4);
				writerCell.setCellValue(ang.getPersoana().getCnp());
				writerCell = row.createCell(5);
				writerCell.setCellValue(realizariRetineri.getVenitbrut());
				sumavb+=realizariRetineri.getVenitbrut();
				writerCell = row.createCell(7);
				writerCell.setCellValue(realizariRetineri.getVenitnet());
				sumavn+=realizariRetineri.getVenitnet();
				writerCell = row.createCell(9);
				writerCell.setCellValue(realizariRetineri.getImpozit());
        writerCell = row.createCell(10);
				writerCell.setCellValue(realizariRetineri.getContract().getDataincepere().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				nrCrt++;
				nrRow++;
			}

		}
		if(nrCrt==0)
		{
			Row row=listaAngajati.createRow(10+nrRow);
			writerCell = row.createCell(0);
			writerCell.setCellValue(String.valueOf(interval[i])+"-"+String.valueOf(interval[i+1]));
			writerCell.setCellStyle(right);
			writerCell = row.createCell(1);
			writerCell.setCellValue(0);
			writerCell = row.createCell(2);
			writerCell.setCellValue("-");
			writerCell.setCellStyle(centered);
			writerCell = row.createCell(3);
			writerCell.setCellValue("-");
			writerCell.setCellStyle(centered);
			writerCell = row.createCell(4);
			writerCell.setCellValue("-");
			writerCell.setCellStyle(centered);
			writerCell = row.createCell(5);
			writerCell.setCellValue(0);
			writerCell = row.createCell(6);
			writerCell.setCellValue(0);
			writerCell = row.createCell(7);
			writerCell.setCellValue(0);
			writerCell = row.createCell(8);
			writerCell.setCellValue(0);
			writerCell = row.createCell(9);
			writerCell.setCellValue(0);
			nrRow++;
		}
		else
		{
			Row row=listaAngajati.getRow(10+nrRow-nrCrt);
			writerCell = row.createCell(0);
			writerCell.setCellValue(String.valueOf(interval[i])+"-"+String.valueOf(interval[i+1]));
			writerCell.setCellStyle(right);
			writerCell = row.createCell(6);
			writerCell.setCellValue(roundAvoid(((double)sumavb)/nrCrt,2));
			writerCell = row.createCell(8);
			writerCell.setCellValue(roundAvoid(((double)sumavn)/nrCrt,2));
			if(nrCrt>1)
			{
			listaAngajati.addMergedRegion(new CellRangeAddress(nrRow-nrCrt+10, nrRow-1+10, 0,0));
			listaAngajati.addMergedRegion(new CellRangeAddress(nrRow-nrCrt+10, nrRow-1+10, 6,6));
			listaAngajati.addMergedRegion(new CellRangeAddress(nrRow-nrCrt+10, nrRow-1+10, 8,8));
			writerCell=row.getCell(0);
			writerCell.setCellStyle(cellRightMiddle);
			writerCell=row.getCell(6);
			writerCell.setCellStyle(cellRightMiddle);
			writerCell=row.getCell(8);
			writerCell.setCellStyle(cellRightMiddle);
			}
		}
		}


		listaAngajati.autoSizeColumn(2);
		listaAngajati.autoSizeColumn(3);
		listaAngajati.autoSizeColumn(4);



		/* ------ ENDING ------ **/
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String newFileLocation = String.format("%s/downloads/%d/Centralizator Vechime Complet - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}
}
