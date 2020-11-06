package net.guides.springboot2.crud.services;

import java.io.IOException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.SocietateRepository;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class Dec112Service {
	@Autowired
	private SocietateRepository societateRepository;

	@Autowired
	private ZileService zileService;

	private String homeLocation = "src\\main\\java\\net\\guides\\springboot2\\crud\\";

	public boolean createDec112(int luna, int an, int idsocietate, int userID, int drec, String numeDeclarant,
			String prenumeDeclarant, String functieDeclarant) throws IOException, ResourceNotFoundException {
		Societate societate = societateRepository.findById((int) idsocietate)
				.orElseThrow(() -> new ResourceNotFoundException("Societate not found for this id :: " + idsocietate));
		String lunaNume = zileService.getNumeLunaByNr(luna);
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element frmMAIN = doc.createElement("frmMAIN");
			doc.appendChild(frmMAIN);

			Element sbfrmPage1Ang = doc.createElement("sbfrmPage1Ang");
			frmMAIN.appendChild(sbfrmPage1Ang);

			Element sfmIdentif = doc.createElement("sfmIdentif");
			sbfrmPage1Ang.appendChild(sfmIdentif);

			// beginning sfmIdentif

			Element d_rec = doc.createElement("d_rec");
			d_rec.appendChild(doc.createTextNode(String.valueOf(drec)));
			sfmIdentif.appendChild(d_rec);

			Element luna_r = doc.createElement("luna_r");
			luna_r.appendChild(doc.createTextNode(lunaNume));
			sfmIdentif.appendChild(luna_r);

			Element an_r = doc.createElement("an_r");
			an_r.appendChild(doc.createTextNode(String.valueOf(an)));
			sfmIdentif.appendChild(an_r);

			Element den = doc.createElement("den");
			den.appendChild(doc.createTextNode(societate.getNume()));
			sfmIdentif.appendChild(den);

			Element adrFisc = doc.createElement("adrFisc");
			adrFisc.appendChild(doc.createTextNode(societate.getAdresa().getAdresa()));
			sfmIdentif.appendChild(adrFisc);

			Element telFisc = doc.createElement("telFisc");
			telFisc.appendChild(doc.createTextNode(societate.getTelefon()));
			sfmIdentif.appendChild(telFisc);

			Element faxFisc = doc.createElement("faxFisc");
			faxFisc.appendChild(doc.createTextNode(""));
			sfmIdentif.appendChild(faxFisc);

			Element mailFisc = doc.createElement("mailFisc");
			mailFisc.appendChild(doc.createTextNode(societate.getEmail()));
			sfmIdentif.appendChild(mailFisc);

			Element datCAM = doc.createElement("datCAM");
			datCAM.appendChild(doc.createTextNode("1"));
			sfmIdentif.appendChild(datCAM);

			Element caen = doc.createElement("caen");
			caen.appendChild(doc.createTextNode(String.valueOf(societate.getIdcaen())));
			sfmIdentif.appendChild(caen);

			Element cif = doc.createElement("cif");
			cif.appendChild(doc.createTextNode(String.valueOf(societate.getCif())));
			sfmIdentif.appendChild(cif);

			Element RO = doc.createElement("RO");
			RO.appendChild(doc.createTextNode("RO"));
			sfmIdentif.appendChild(RO);

			Element Bifa_FdGar = doc.createElement("Bifa_FdGar");
			Bifa_FdGar.appendChild(doc.createTextNode("1"));
			sfmIdentif.appendChild(Bifa_FdGar);

			Element tRisc = doc.createElement("tRisc");
			tRisc.appendChild(doc.createTextNode("0.000"));
			sfmIdentif.appendChild(tRisc);

			Element Bifa_UM = doc.createElement("Bifa_UM");
			Bifa_UM.appendChild(doc.createTextNode("0"));
			sfmIdentif.appendChild(Bifa_UM);

			Element art90 = doc.createElement("art90");
			art90.appendChild(doc.createTextNode("0"));
			sfmIdentif.appendChild(art90);

			// end sfmIdentif

			// beginning sfrmIdentif

			Element sfmIdentif2 = doc.createElement("sfmIdentif2");
			sbfrmPage1Ang.appendChild(sfmIdentif2);

			Element art90reg = doc.createElement("art90");
			art90reg.appendChild(doc.createTextNode(societate.getRegcom()));
			sfmIdentif2.appendChild(art90reg);

			Element adrSoc = doc.createElement("adrSoc");
			adrSoc.appendChild(doc.createTextNode(societate.getAdresa().getAdresa()));
			sfmIdentif2.appendChild(adrSoc);

			Element telSoc = doc.createElement("telSoc");
			telSoc.appendChild(doc.createTextNode(societate.getTelefon()));
			sfmIdentif2.appendChild(telSoc);

			Element faxSoc = doc.createElement("faxSoc");
			faxSoc.appendChild(doc.createTextNode(""));
			sfmIdentif2.appendChild(faxSoc);

			Element mailSoc = doc.createElement("mailSoc");
			mailSoc.appendChild(doc.createTextNode(societate.getEmail()));
			sfmIdentif2.appendChild(mailSoc);

			Element casaAng = doc.createElement("casaAng");
			casaAng.appendChild(doc.createTextNode("_B"));
			sfmIdentif2.appendChild(casaAng);

			Element caen2 = doc.createElement("caen");
			caen2.appendChild(doc.createTextNode(String.valueOf(societate.getIdcaen())));
			sfmIdentif2.appendChild(caen2);

			Element datCAM2 = doc.createElement("datCAM");
			datCAM2.appendChild(doc.createTextNode("1"));
			sfmIdentif2.appendChild(datCAM2);

			Element tRisc2 = doc.createElement("tRisc");
			tRisc2.appendChild(doc.createTextNode("0.000"));
			sfmIdentif2.appendChild(tRisc2);

			Element bifa_CAM = doc.createElement("bifa_CAM");
			bifa_CAM.appendChild(doc.createTextNode("0"));
			sfmIdentif.appendChild(bifa_CAM);

			// end sfmIdentif2

			// beginning sbfrmFooter

			Element sbfrmFooter = doc.createElement("sbfrmFooter");
			sbfrmPage1Ang.appendChild(sbfrmFooter);

			Element nume_declar = doc.createElement("nume_declar");
			nume_declar.appendChild(doc.createTextNode(numeDeclarant));
			sbfrmFooter.appendChild(nume_declar);

			Element prenume_declar = doc.createElement("prenume_declar");
			prenume_declar.appendChild(doc.createTextNode(prenumeDeclarant));
			sbfrmFooter.appendChild(prenume_declar);

			Element functie_declar = doc.createElement("functie_declar");
			functie_declar.appendChild(doc.createTextNode(functieDeclarant));
			sbfrmFooter.appendChild(functie_declar);

			// end sbfrmFooter

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			Files.createDirectories(Paths.get(homeLocation + "downloads\\" + userID));
			String newFileLocation = String.format("%s\\downloads\\%d\\Declaratia 112 - %s - %s %d.xml", homeLocation,
					userID, societate.getNume(), lunaNume, an);

			StreamResult result = new StreamResult(new File(newFileLocation));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
		return true;
	}

}
