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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Client;
import net.guides.springboot2.crud.model.Caiet;
import net.guides.springboot2.crud.model.Factura;
import net.guides.springboot2.crud.model.ParametriiSalariu;
import net.guides.springboot2.crud.model.Produs;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.FacturaRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

@Service
public class FacturaService {

	@Autowired
	private FacturaRepository facturaRepository;

	@Autowired
	private SocietateRepository societateRepository;

	@Autowired
	private ParametriiSalariuService parametriiSalariuService;

	@Autowired
	private CaietService caietService;

	private static double roundAvoid(double value, int places) {
		double scale = Math.pow(10, places);
		return Math.round(value * scale) / scale;
	}

	
	FacturaService() {
	}
	
	public List<Factura> findAll() {
		return facturaRepository.findAll(Sort.by(Sort.Direction.DESC, "dataexpedierii", "numar"));
	}
	
	public Factura findById(int id) throws ResourceNotFoundException {
		return facturaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Factura not found for this id :: " + id));
	}
	
	public List<Factura> findBySocietate_Id(int idsocietate) {
		return facturaRepository.findByClient_Societate_IdOrderByDataexpedieriiDescNumarDesc(idsocietate);
	}
	
	public int findNumarFactura() {
		return facturaRepository.findNumarFactura();
	}
	
	public Factura save(Factura newFactura) throws ResourceNotFoundException {
		for (Produs produs : newFactura.getProduse()) {
			produs.setFactura(newFactura);
		}
		Caiet caiet = caietService.findBySerie(newFactura.getSerie());
		newFactura.setCaiet(caiet);
		return facturaRepository.save(newFactura);
	}
	
	public Factura update(Factura newFactura, int id) throws ResourceNotFoundException {
		Factura factura = findById(id);
		return save(factura.update(newFactura));
	}
	
	public void delete(int facturaId) throws ResourceNotFoundException {
		Factura factura = facturaRepository.findById(facturaId).orElseThrow(() -> new ResourceNotFoundException("Factura not found for this id :: " + facturaId));
		
		facturaRepository.delete(factura);
	}
	
	private String homeLocation = "src/main/java/net/guides/springboot2/crud/";

	public boolean createFactura(int ids,int id,int uid) throws IOException, ResourceNotFoundException {

		Societate societate = societateRepository.findById(ids).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + ids));
		Factura factura = facturaRepository.findById(id).get();
		ParametriiSalariu parametriiSalariu = parametriiSalariuService.getParametriiSalariu();
		
		String facturaTemplate = homeLocation + "/templates";

		FileInputStream file = new FileInputStream(new File(facturaTemplate, "FacturaTemplate.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet facturaWb = workbook.getSheetAt(0);

		Client client=factura.getClient();
		LocalDate dataExpedierii=factura.getDataexpedierii();

		Row row=facturaWb.getRow(1);
		Cell writerCell = row.getCell(11);
		writerCell.setCellValue(factura.getSerie());
		writerCell = row.getCell(13);
		writerCell.setCellValue(factura.getNumar());
		writerCell = row.getCell(16);
		writerCell.setCellValue(dataExpedierii.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

		row=facturaWb.getRow(8);
		writerCell = row.getCell(4);
		writerCell.setCellValue(societate.getNume());
		writerCell = row.getCell(13);
		writerCell.setCellValue(client.getNumecomplet());

		row=facturaWb.getRow(10);
		writerCell = row.getCell(4);
		writerCell.setCellValue(societate.getRegcom());
		writerCell = row.getCell(13);
		writerCell.setCellValue(client.getNrregcom());

		row=facturaWb.getRow(11);
		writerCell = row.getCell(4);
		writerCell.setCellValue(societate.getCif());
		writerCell = row.getCell(13);
		writerCell.setCellValue(client.getCodfiscal());

		row=facturaWb.getRow(12);
		writerCell = row.getCell(4);
		writerCell.setCellValue(societate.getAdresa().getAdresa());
		writerCell = row.getCell(13);
		writerCell.setCellValue(client.getAdresa().getAdresa());

		if(societate.getContbancar().size()==1)
		{
		row=facturaWb.getRow(14);
		writerCell = row.getCell(4);
		writerCell.setCellValue(societate.getContbancar().get(0).getIban());
		row=facturaWb.getRow(15);
		writerCell = row.getCell(4);
		writerCell.setCellValue(societate.getContbancar().get(0).getNumebanca());
		}
		else if(societate.getContbancar().size()>1)
		{
			row=facturaWb.getRow(14);
			writerCell = row.getCell(4);
			writerCell.setCellValue(societate.getContbancar().get(0).getIban());
			row=facturaWb.getRow(15);
			writerCell = row.getCell(4);
			writerCell.setCellValue(societate.getContbancar().get(0).getNumebanca());
			row=facturaWb.getRow(16);
			writerCell = row.getCell(4);
			writerCell.setCellValue(societate.getContbancar().get(1).getIban());
			row=facturaWb.getRow(17);
			writerCell = row.getCell(4);
			writerCell.setCellValue(societate.getContbancar().get(1).getNumebanca());
		}
		row=facturaWb.getRow(16);
		writerCell = row.getCell(13);
		writerCell.setCellValue(client.getCont());
		row=facturaWb.getRow(17);
		writerCell = row.getCell(13);
		writerCell.setCellValue(client.getBanca());

		row=facturaWb.getRow(18);
		writerCell = row.getCell(4);
		writerCell.setCellValue(societate.getCapsoc());
		row=facturaWb.getRow(19);
		writerCell = row.getCell(4);
		writerCell.setCellValue(societate.getCapsoc());
		row=facturaWb.getRow(20);
		writerCell = row.getCell(4);
		writerCell.setCellValue(String.valueOf(parametriiSalariu.getTva())+" %");

		int i=1;
		float sumFaraTva=0,sumTva=0;
		for(Produs produs : factura.getProduse())
		{
			row=facturaWb.getRow(25+2*i);
			writerCell = row.getCell(1);
			writerCell.setCellValue(i);
			writerCell = row.getCell(2);
			writerCell.setCellValue(produs.getDenumire());
			writerCell = row.getCell(8);
			writerCell.setCellValue(produs.getUm());
			writerCell = row.getCell(9);
			writerCell.setCellValue(produs.getCantitate());
			writerCell = row.getCell(11);
			writerCell.setCellValue(roundAvoid(produs.getPretUnitar(),2));
			writerCell = row.getCell(13);
			writerCell.setCellValue(roundAvoid(produs.getCantitate()*produs.getPretUnitar(),2));
			sumFaraTva+=produs.getCantitate()*produs.getPretUnitar();
			writerCell = row.getCell(16);
			writerCell.setCellValue(roundAvoid((produs.getCantitate()*produs.getPretUnitar())*parametriiSalariu.getTva()/100,2));
			sumTva+=(produs.getCantitate()*produs.getPretUnitar())*parametriiSalariu.getTva()/100;
			i++;
		}

		row=facturaWb.getRow(65);
		writerCell = row.getCell(13);
		writerCell.setCellValue(roundAvoid(sumFaraTva,2));
		writerCell = row.getCell(16);
		writerCell.setCellValue(roundAvoid(sumTva,2));

		row=facturaWb.getRow(68);
		writerCell = row.getCell(13);
		writerCell.setCellValue(roundAvoid(sumFaraTva+sumTva,2));


		/* ------ ENDING ------ **/
		// * OUTPUT THE FILE
		Files.createDirectories(Paths.get(homeLocation + "downloads/" + uid));
		String newFileLocation = String.format("%s/downloads/%d/Factură - %s - %s - %s %s.xlsx", homeLocation, uid, societate.getNume(), factura.getClient().getNume(), factura.getDataexpedierii().toString(),factura.getOraexpedierii());

		FileOutputStream outputStream = new FileOutputStream(newFileLocation);
		workbook.write(outputStream);
		workbook.close();

		return true;
	}

}
