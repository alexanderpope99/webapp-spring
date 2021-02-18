package net.guides.springboot2.crud.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Caiet;
import net.guides.springboot2.crud.model.Factura;
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
	private CaietService caietService;
	
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
		
		String facturaTemplate = homeLocation + "/templates";

		FileInputStream file = new FileInputStream(new File(facturaTemplate, "FacturaTemplate.xlsx"));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet facturaWb = workbook.getSheetAt(0);


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
