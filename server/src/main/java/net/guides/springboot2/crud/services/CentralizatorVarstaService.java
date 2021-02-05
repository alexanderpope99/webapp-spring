package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
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
public class CentralizatorVarstaService {

	@Autowired
	SocietateRepository societateRepository;

	@Autowired
	AngajatRepository angajatRepository;

	@Autowired
	ZileService zileService;

	@Autowired
	RealizariRetineriService realizariRetineriService;
	
	private String homeLocation = "src/main/java/net/guides/springboot2/crud/";

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
			vb[i]+=realizariRetineri.getTotaldrepturi();
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
			writerCell.setCellValue(nr[i]!=0?vb[i]/nr[i]:0); // venit brut mediu
			writerCell = listaAngajati.getRow(10+i).getCell(4);
			writerCell.setCellValue(vn[i]); // venit net
			writerCell = listaAngajati.getRow(10+i).getCell(5);
			writerCell.setCellValue(nr[i]!=0?vn[i]/nr[i]:0); // venit net mediu
			writerCell = listaAngajati.getRow(10+i).getCell(6);
			writerCell.setCellValue(im[i]); // impozit
		}




		/* ------ ENDING ------ **/
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String newFileLocation = String.format("%s/downloads/%d/Centralizator Vârstă - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}

	public boolean createCentralizatorVarstaComplet(int luna, int an, int idsocietate, int userID) throws IOException, ResourceNotFoundException {

		Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
		List<Angajat> angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(idsocietate);

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

		int[] nr={0,0,0,0};
		float[] vb={0,0,0,0};
		float[] vn={0,0,0,0};
		float[] im={0,0,0,0};

		
		for (Angajat ang : angajati) {
			RealizariRetineri realizariRetineri = realizariRetineriService.saveOrGetRealizariRetineri(luna, an, ang.getContract().getId());
			int varsta=ang.getPersoana().getVarsta();

		}

		for(int i=0;i<=3;i++)
		{

		}




		/* ------ ENDING ------ **/
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
		String newFileLocation = String.format("%s/downloads/%d/Centralizator Vârstă Complet - %s - %s %d.xlsx", homeLocation, userID, societate.getNume(), lunaNume, an);

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}
}