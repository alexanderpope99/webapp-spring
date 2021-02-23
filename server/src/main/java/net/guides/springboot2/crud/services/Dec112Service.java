package net.guides.springboot2.crud.services;

import java.io.IOException;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.CM;
import net.guides.springboot2.crud.model.Contract;
import net.guides.springboot2.crud.model.PersoanaIntretinere;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.RealizariRetineriRepository;
import net.guides.springboot2.crud.repository.SocietateRepository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class Dec112Service {
	@Autowired
	private SocietateRepository societateRepository;

	@Autowired
	private AngajatRepository angajatRepository;

	@Autowired
	private RealizariRetineriRepository realizariRetineriRepository;

	@Autowired
	private ZileService zileService;

	@Autowired
	private CMService cmService;

	private String homeLocation = "src/main/java/net/guides/springboot2/crud/";

	public boolean createDec112(int luna, int an, int idsocietate, int userID, int drec, String numeDeclarant, String prenumeDeclarant, String functieDeclarant) throws IOException, ResourceNotFoundException {
		Societate societate = societateRepository.findById(idsocietate).orElseThrow(() -> new ResourceNotFoundException("Nu există societate cu id: " + idsocietate));
		societate.checkData();
		System.out.println(an);

		List<Angajat> angajati = angajatRepository.findBySocietate_IdAndContract_IdNotNullOrderByPersoana_NumeAscPersoana_PrenumeAsc(idsocietate);
		if (angajati.isEmpty())
			throw new ResourceNotFoundException("Societatea nu are angajați");

		String lunaNume = zileService.getNumeLunaByNr(luna);
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element frmMAIN = doc.createElement("frmMAIN");
			doc.appendChild(frmMAIN);

			String sectiune = "";
			String erori = "";

			Element sbfrmPage1Ang = doc.createElement("sbfrmPage1Ang");
			frmMAIN.appendChild(sbfrmPage1Ang);

			Element sfmIdentif = doc.createElement("sfmIdentif");
			sbfrmPage1Ang.appendChild(sfmIdentif);

			// beginning sfmIdentif
			Element childElement = doc.createElement("d_rec");
			childElement.appendChild(doc.createTextNode(String.valueOf(drec)));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("luna_r");
			childElement.appendChild(doc.createTextNode(String.format("%02d", luna)));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("an_r");
			childElement.appendChild(doc.createTextNode(String.valueOf(an)));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("den");
			childElement.appendChild(doc.createTextNode(societate.getNume()));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("adrFisc");
			childElement.appendChild(doc.createTextNode(societate.getAdresa().getAdresa()));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("telFisc");
			childElement.appendChild(doc.createTextNode(societate.getTelefon()));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("faxFisc");
			childElement.appendChild(doc.createTextNode(societate.getFax()));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("mailFisc");
			childElement.appendChild(doc.createTextNode(societate.getEmail()));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("datCAM");
			childElement.appendChild(doc.createTextNode("1"));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("caen");
			childElement.appendChild(doc.createTextNode(String.valueOf(societate.getIdcaen())));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("cif");
			childElement.appendChild(doc.createTextNode(String.valueOf(societate.getCif()).substring(2)));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("RO");
			childElement.appendChild(doc.createTextNode("RO"));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("Bifa_FdGar");
			childElement.appendChild(doc.createTextNode("1"));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("tRisc");
			childElement.appendChild(doc.createTextNode("0.000"));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("Bifa_UM");
			childElement.appendChild(doc.createTextNode("0"));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("art90");
			childElement.appendChild(doc.createTextNode("0"));
			sfmIdentif.appendChild(childElement);

			// end sfmIdentif

			// beginning sfrmIdentif

			Element sfmIdentif2 = doc.createElement("sfmIdentif2");
			sbfrmPage1Ang.appendChild(sfmIdentif2);

			childElement = doc.createElement("rgCom");
			childElement.appendChild(doc.createTextNode(societate.getRegcom()));
			sfmIdentif2.appendChild(childElement);

			childElement = doc.createElement("adrSoc");
			childElement.appendChild(doc.createTextNode(societate.getAdresa().getAdresa()));
			sfmIdentif2.appendChild(childElement);

			childElement = doc.createElement("telSoc");
			childElement.appendChild(doc.createTextNode(societate.getTelefon()));
			sfmIdentif2.appendChild(childElement);

			childElement = doc.createElement("faxSoc");
			childElement.appendChild(doc.createTextNode(societate.getFax()));
			sfmIdentif2.appendChild(childElement);

			childElement = doc.createElement("mailSoc");
			childElement.appendChild(doc.createTextNode(societate.getEmail()));
			sfmIdentif2.appendChild(childElement);

			childElement = doc.createElement("casaAng");
			childElement.appendChild(doc.createTextNode("_B"));
			sfmIdentif2.appendChild(childElement);

			childElement = doc.createElement("caen");
			childElement.appendChild(doc.createTextNode(String.valueOf(societate.getIdcaen())));
			sfmIdentif2.appendChild(childElement);

			childElement = doc.createElement("datCAM");
			childElement.appendChild(doc.createTextNode("1"));
			sfmIdentif2.appendChild(childElement);

			childElement = doc.createElement("tRisc");
			childElement.appendChild(doc.createTextNode("0.000"));
			sfmIdentif2.appendChild(childElement);

			childElement = doc.createElement("bifa_CAM");
			childElement.appendChild(doc.createTextNode("0"));
			sfmIdentif.appendChild(childElement);

			// end sfmIdentif2

			// beginning sbfrmFooter

			Element sbfrmFooter = doc.createElement("sbfrmFooter");
			sbfrmPage1Ang.appendChild(sbfrmFooter);

			childElement = doc.createElement("nume_declar");
			childElement.appendChild(doc.createTextNode(numeDeclarant));
			sbfrmFooter.appendChild(childElement);

			childElement = doc.createElement("prenume_declar");
			childElement.appendChild(doc.createTextNode(prenumeDeclarant));
			sbfrmFooter.appendChild(childElement);

			childElement = doc.createElement("functie_declar");
			childElement.appendChild(doc.createTextNode(functieDeclarant));
			sbfrmFooter.appendChild(childElement);

			// end sbfrmFooter

			Element sfmSectAVal = doc.createElement("sfmSectAVal");
			sbfrmPage1Ang.appendChild(sfmSectAVal);

			childElement = doc.createElement("nrcrt");
			childElement.appendChild(doc.createTextNode("1"));
			sfmSectAVal.appendChild(childElement);

			childElement = doc.createElement("A_codOblig");
			childElement.appendChild(doc.createTextNode("602"));
			sfmSectAVal.appendChild(childElement);

			childElement = doc.createElement("codbuget");
			childElement.appendChild(doc.createTextNode("5503XXXXXX"));
			sfmSectAVal.appendChild(childElement);

			int impozit_datorat = 0;
			int impozit_scutit = 0;
			int cas = 0;
			int cass = 0;
			int cam = 0;
			for (Angajat angajat : angajati) {
				RealizariRetineri realizariRetineri = (realizariRetineriRepository.findByLunaAndAnAndContract_Id(luna, an, angajat.getContract().getId()));
				if (realizariRetineri == null) {
					erori += "Angajatul " + angajat.getPersoana().getNume() + " " + angajat.getPersoana().getPrenume() + " nu are Realizari/Retineri efectuat in " + lunaNume + " " + an + "\n";
				} else {
					realizariRetineri.checkData();
					impozit_datorat += realizariRetineri.getImpozit();
					impozit_scutit += realizariRetineri.getImpozitscutit();
					cas += realizariRetineri.getCas();
					cass += realizariRetineri.getCass();
					cam += realizariRetineri.getCam();
				}
			}

			if (erori.compareTo("") != 0)
				throw new ResourceNotFoundException(erori);

			childElement = doc.createElement("a_datorat"); // impozit datorat + scutit
			childElement.appendChild(doc.createTextNode(String.valueOf((impozit_datorat + impozit_scutit))));
			sfmSectAVal.appendChild(childElement);

			childElement = doc.createElement("a_deductibil");
			childElement.appendChild(doc.createTextNode("0"));
			sfmSectAVal.appendChild(childElement);

			childElement = doc.createElement("a_scutit"); // impozit scutit
			childElement.appendChild(doc.createTextNode((String.valueOf((impozit_scutit)))));
			sfmSectAVal.appendChild(childElement);

			Element sfmSectAVal2 = doc.createElement("sfmSectAVal");
			sbfrmPage1Ang.appendChild(sfmSectAVal2);

			childElement = doc.createElement("nrcrt");
			childElement.appendChild(doc.createTextNode("2"));
			sfmSectAVal2.appendChild(childElement);

			childElement = doc.createElement("A_codOblig");
			childElement.appendChild(doc.createTextNode("412"));
			sfmSectAVal2.appendChild(childElement);

			childElement = doc.createElement("codbuget");
			childElement.appendChild(doc.createTextNode("5503XXXXXX"));
			sfmSectAVal2.appendChild(childElement);

			childElement = doc.createElement("a_datorat");// cas
			childElement.appendChild(doc.createTextNode(String.valueOf(cas)));
			sfmSectAVal2.appendChild(childElement);

			childElement = doc.createElement("a_deductibil");
			childElement.appendChild(doc.createTextNode("0"));
			sfmSectAVal2.appendChild(childElement);

			childElement = doc.createElement("a_scutit");
			childElement.appendChild(doc.createTextNode("0"));
			sfmSectAVal2.appendChild(childElement);

			Element sfmSectAVal3 = doc.createElement("sfmSectAVal");
			sbfrmPage1Ang.appendChild(sfmSectAVal3);

			childElement = doc.createElement("nrcrt");
			childElement.appendChild(doc.createTextNode("3"));
			sfmSectAVal3.appendChild(childElement);

			childElement = doc.createElement("A_codOblig");
			childElement.appendChild(doc.createTextNode("432"));
			sfmSectAVal3.appendChild(childElement);

			childElement = doc.createElement("codbuget");
			childElement.appendChild(doc.createTextNode("5503XXXXXX"));
			sfmSectAVal3.appendChild(childElement);

			childElement = doc.createElement("a_datorat");// cass
			childElement.appendChild(doc.createTextNode((String.valueOf(cass))));
			sfmSectAVal3.appendChild(childElement);

			childElement = doc.createElement("a_deductibil");
			childElement.appendChild(doc.createTextNode("0"));
			sfmSectAVal3.appendChild(childElement);

			childElement = doc.createElement("a_scutit");
			childElement.appendChild(doc.createTextNode("0"));
			sfmSectAVal3.appendChild(childElement);

			Element sfmSectAVal4 = doc.createElement("sfmSectAVal");
			sbfrmPage1Ang.appendChild(sfmSectAVal4);

			childElement = doc.createElement("nrcrt");
			childElement.appendChild(doc.createTextNode("4"));
			sfmSectAVal4.appendChild(childElement);

			childElement = doc.createElement("A_codOblig");
			childElement.appendChild(doc.createTextNode("480"));
			sfmSectAVal4.appendChild(childElement);

			childElement = doc.createElement("codbuget");
			childElement.appendChild(doc.createTextNode("20470300XX"));
			sfmSectAVal4.appendChild(childElement);

			childElement = doc.createElement("a_datorat");// cam
			childElement.appendChild(doc.createTextNode(String.valueOf(cam)));
			sfmSectAVal4.appendChild(childElement);

			childElement = doc.createElement("a_deductibil");
			childElement.appendChild(doc.createTextNode("0"));
			sfmSectAVal4.appendChild(childElement);

			childElement = doc.createElement("a_scutit");
			childElement.appendChild(doc.createTextNode("0"));
			sfmSectAVal4.appendChild(childElement);

			childElement = doc.createElement("sfmSectATotal");// de completat totalurile din A
			childElement.appendChild(doc.createTextNode(String.valueOf(impozit_datorat + cas + cass + cam)));
			sbfrmPage1Ang.appendChild(childElement);

			// Element sfmSectB = doc.createElement("sfmSectB");
			// sbfrmPage1Ang.appendChild(sfmSectB);

			// childElement = doc.createElement("B_cnp");
			// childElement.appendChild(doc.createTextNode("")); // de completat numar de
			// asigurati somaj
			// sfmSectB.appendChild(childElement);

			// childElement = doc.createElement("B_sanatate");
			// childElement.appendChild(doc.createTextNode("")); // de completat numar de
			// asigurati cm si indemnizatii
			// sfmSectB.appendChild(childElement);

			// childElement = doc.createElement("B_pensie");
			// childElement.appendChild(doc.createTextNode("")); // de completat numar de
			// asigurati care datoreaza cas
			// // (este numar zecimal cu precision 2)
			// sfmSectB.appendChild(childElement);

			// childElement = doc.createElement("B_sal");
			// childElement.appendChild(doc.createTextNode("")); // de completat numar de
			// salariati
			// sfmSectB.appendChild(childElement);

			// childElement = doc.createElement("B1_brut_salarii");
			// childElement.appendChild(doc.createTextNode("")); // de completat total fond
			// de salarii brute
			// sfmSectB.appendChild(childElement);

			// childElement = doc.createElement("T1");
			// childElement.appendChild(doc.createTextNode(""));
			// sfmSectB.appendChild(childElement);

			// childElement = doc.createElement("T2");
			// childElement.appendChild(doc.createTextNode(""));
			// sfmSectB.appendChild(childElement);

			// childElement = doc.createElement("T3");
			// childElement.appendChild(doc.createTextNode(""));
			// sfmSectB.appendChild(childElement);

			// childElement = doc.createElement("T4");
			// childElement.appendChild(doc.createTextNode(""));
			// sfmSectB.appendChild(childElement);

			// beginning sbfrmSectiuneaC

			Element sbfrmSectiuneaC = doc.createElement("sbfrmSectiuneaC");
			sbfrmPage1Ang.appendChild(sbfrmSectiuneaC);

			Element sfmSectC1 = doc.createElement("sfmSectC1");
			sbfrmSectiuneaC.appendChild(sfmSectC1);

			childElement = doc.createElement("c1_7");
			childElement.appendChild(doc.createTextNode("0"));
			sfmSectC1.appendChild(childElement);

			Element sbfrmC345 = doc.createElement("sbfrmC345");
			sbfrmSectiuneaC.appendChild(sbfrmC345);

			childElement = doc.createElement("c3_11");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_12");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_13");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_14");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_21");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_22");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_23");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_24");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_31");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_32");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_33");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_34");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_41");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_42");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_43");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			childElement = doc.createElement("c3_44");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmC345.appendChild(childElement);

			// end sbfrmSectiuneaC

			// beginning sbfrmSectiuneaD

			Element sbfrmSectiuneaD = doc.createElement("sbfrmSectiuneaD");
			sbfrmPage1Ang.appendChild(sbfrmSectiuneaD);

			childElement = doc.createElement("D2");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaD.appendChild(childElement);

			childElement = doc.createElement("D3");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaD.appendChild(childElement);

			// end sbfrmSectiuneaD

			// beginning sbfrmSectiuneaE

			Element sbfrmSectiuneaE = doc.createElement("sbfrmSectiuneaE");
			sbfrmPage1Ang.appendChild(sbfrmSectiuneaE);

			childElement = doc.createElement("E3_11");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaD.appendChild(childElement);

			childElement = doc.createElement("E3_12");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			childElement = doc.createElement("E3_13");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			childElement = doc.createElement("E3_14");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			childElement = doc.createElement("E3_21");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			childElement = doc.createElement("E3_22");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			childElement = doc.createElement("E3_23");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			childElement = doc.createElement("E3_24");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			childElement = doc.createElement("E3_31");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			childElement = doc.createElement("E3_32");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			childElement = doc.createElement("E3_33");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			childElement = doc.createElement("E3_34");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			childElement = doc.createElement("E3_41");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			childElement = doc.createElement("E3_42");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			childElement = doc.createElement("E3_43");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			childElement = doc.createElement("E3_44");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmSectiuneaE.appendChild(childElement);

			// end sbfrmSectiuneaE

			// beginning sbfrmSectiuneaF

			Element sbfrmSectiuneaF = doc.createElement("sbfrmSectiuneaF");
			sbfrmPage1Ang.appendChild(sbfrmSectiuneaF);

			Element sbfrmF1 = doc.createElement("sbfrmF1");
			sbfrmSectiuneaF.appendChild(sbfrmF1);

			childElement = doc.createElement("F1_suma");
			childElement.appendChild(doc.createTextNode(String.valueOf(impozit_datorat + impozit_scutit)));
			sbfrmF1.appendChild(childElement);

			childElement = doc.createElement("F1_suma_ded");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmF1.appendChild(childElement);

			childElement = doc.createElement("F1_suma_scut");
			childElement.appendChild(doc.createTextNode(String.valueOf(impozit_scutit)));
			sbfrmF1.appendChild(childElement);

			// end sbfrmSectiuneaF

			// end sbfrmPage1Ang

			int c = 1;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY");
			for (Angajat angajat : angajati) {
				Contract contract = angajat.getContract();
				contract.checkData();
				Element sbfrmPage1Asig = doc.createElement("sbfrmPage1Asig");
				frmMAIN.appendChild(sbfrmPage1Asig);

				Element sfmDateIdentif = doc.createElement("sfmDateIdentif");
				sbfrmPage1Asig.appendChild(sfmDateIdentif);

				childElement = doc.createElement("idAsig");
				childElement.appendChild(doc.createTextNode(String.valueOf(c)));
				sfmDateIdentif.appendChild(childElement);
				c++;

				childElement = doc.createElement("cnp_asig");
				childElement.appendChild(doc.createTextNode(angajat.getPersoana().getCnp()));
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("Nume_asig");
				childElement.appendChild(doc.createTextNode(angajat.getPersoana().getNume()));
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("Pren_asig");
				childElement.appendChild(doc.createTextNode(angajat.getPersoana().getPrenume()));
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("Data_ang");
				childElement.appendChild(doc.createTextNode(contract.getData() == null ? "" : formatter.format(contract.getData())));
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("Casa_sn");
				String casaSanatate = contract.getCasasanatate();
				if (casaSanatate != null) {
					if (casaSanatate.length() == 0 || casaSanatate.length() == 1) {
						childElement.appendChild(doc.createTextNode(""));
					} else {
						childElement.appendChild(doc.createTextNode(casaSanatate.substring(casaSanatate.length() - 2)));
					}
				} else {
					childElement.appendChild(doc.createTextNode(""));
				}
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("Asig_ci");
				childElement.appendChild(doc.createTextNode("1"));
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("Asig_so");
				childElement.appendChild(doc.createTextNode("1"));
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("asigScu");
				childElement.appendChild(doc.createTextNode(contract.isCalculdeduceri() ? "0" : "2"));
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("an_r");
				childElement.appendChild(doc.createTextNode(String.valueOf(an)));
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("luna_r");
				childElement.appendChild(doc.createTextNode(luna <= 9 ? ("0" + luna) : (String.valueOf(luna))));
				sfmDateIdentif.appendChild(childElement);

				// beginning det1

				Element det1 = doc.createElement("det1");
				sfmDateIdentif.appendChild(det1);

				childElement = doc.createElement("plata_CAM");
				childElement.appendChild(doc.createTextNode(""));
				det1.appendChild(childElement);

				childElement = doc.createElement("plata_CASS");
				childElement.appendChild(doc.createTextNode(""));
				det1.appendChild(childElement);

				childElement = doc.createElement("plata_CAS");
				childElement.appendChild(doc.createTextNode(""));
				det1.appendChild(childElement);

				childElement = doc.createElement("bifa_UE");
				childElement.appendChild(doc.createTextNode(""));
				det1.appendChild(childElement);

				childElement = doc.createElement("bifa_altstat");
				childElement.appendChild(doc.createTextNode(""));
				det1.appendChild(childElement);

				childElement = doc.createElement("acord_NU");
				childElement.appendChild(doc.createTextNode(""));
				det1.appendChild(childElement);

				childElement = doc.createElement("acord_DA");
				childElement.appendChild(doc.createTextNode(""));
				det1.appendChild(childElement);

				// end det1

				int nrParinti = 0;
				for (PersoanaIntretinere persInt : angajat.getPersoaneintretinere()) {
					switch (persInt.getGrad()) {
					case "Soț/Soție":
						childElement = doc.createElement("cnpSot");
						childElement.appendChild(doc.createTextNode(persInt.getCnp()));
						sfmDateIdentif.appendChild(childElement);
						childElement = doc.createElement("prenSot");
						childElement.appendChild(doc.createTextNode(persInt.getPrenume()));
						sfmDateIdentif.appendChild(childElement);
						childElement = doc.createElement("numeSot");
						childElement.appendChild(doc.createTextNode(persInt.getNume()));
						sfmDateIdentif.appendChild(childElement);
						break;
					case "Părinte":
						childElement = doc.createElement("prenParinte" + (nrParinti > 0 + 1));
						childElement.appendChild(doc.createTextNode(persInt.getPrenume()));
						sfmDateIdentif.appendChild(childElement);
						childElement = doc.createElement("numeParinte" + (nrParinti > 0 + 1));
						childElement.appendChild(doc.createTextNode(persInt.getNume()));
						sfmDateIdentif.appendChild(childElement);
						childElement = doc.createElement("cnpParinte" + (nrParinti > 0 + 1));
						childElement.appendChild(doc.createTextNode(persInt.getCnp()));
						sfmDateIdentif.appendChild(childElement);
						break;
					default:
						break;
					}
				}

				// -------SECTIUNEA A------ salariat normal, fara concediu medical in luna
				// respectiva
				RealizariRetineri realizariRetineri = (realizariRetineriRepository.findByLunaAndAnAndContract_Id(luna, an, contract.getId()));

				if (contract.getTip().compareTo("Contract de munca") == 0 && realizariRetineri.getZilecm() == 0) {
					sectiune = "A";
					Element sfmButoaneA = doc.createElement("sfmButoane");
					sbfrmPage1Asig.appendChild(sfmButoaneA);

					childElement = doc.createElement("rbl");
					childElement.appendChild(doc.createTextNode("3"));
					sfmButoaneA.appendChild(childElement);

					childElement = doc.createElement("flag");
					childElement.appendChild(doc.createTextNode("1"));
					sfmButoaneA.appendChild(childElement);

					Element rbl2A = doc.createElement("rbl2");
					sfmButoaneA.appendChild(rbl2A);

					childElement = doc.createElement("rbC");
					childElement.appendChild(doc.createTextNode("0"));
					rbl2A.appendChild(childElement);

					childElement = doc.createElement("rbB");
					childElement.appendChild(doc.createTextNode("0"));
					rbl2A.appendChild(childElement);

					childElement = doc.createElement("rbA");
					childElement.appendChild(doc.createTextNode("1"));
					rbl2A.appendChild(childElement);

					Element sbfrmSectiuneaA = doc.createElement("sbfrmSectiuneaA");
					sbfrmPage1Asig.appendChild(sbfrmSectiuneaA);

					childElement = doc.createElement("VB_A"); // venit brut
					childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getTotaldrepturi() + realizariRetineri.getValoaretichete())));
					sbfrmSectiuneaA.appendChild(childElement);

					childElement = doc.createElement("tichete_A"); // tichete
					childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getValoaretichete())));
					sbfrmSectiuneaA.appendChild(childElement);

					String tip = "";

					if (contract.getTip().compareTo("Contract de administrator") == 0)
						tip = "13";
					if (contract.getTip().compareTo("Contract de munca") == 0) {
						if (contract.isStudiisuperioare())
							tip = "26";
						else
							tip = "1";
					}
					childElement = doc.createElement("A_1"); // tip asigurat
					childElement.appendChild(doc.createTextNode(tip));
					sbfrmSectiuneaA.appendChild(childElement);

					childElement = doc.createElement("A_2"); // pensionar
					childElement.appendChild(doc.createTextNode(contract.isPensionar() ? "1" : "0"));
					sbfrmSectiuneaA.appendChild(childElement);

					childElement = doc.createElement("A_3"); // tip contract
					childElement.appendChild(doc.createTextNode(realizariRetineri.getDuratazilucru() == 8 ? "N" : "P" + realizariRetineri.getDuratazilucru()));

					sbfrmSectiuneaA.appendChild(childElement);

					childElement = doc.createElement("A_4"); // norma ore
					childElement.appendChild(doc.createTextNode("8"));
					sbfrmSectiuneaA.appendChild(childElement);

					childElement = doc.createElement("A_6"); // ore lucrate efectiv in luna
					childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getOrelucrate())));
					sbfrmSectiuneaA.appendChild(childElement);

					childElement = doc.createElement("A_7"); // ore suspendate in luna
					childElement.appendChild(doc.createTextNode("0"));
					sbfrmSectiuneaA.appendChild(childElement);

					childElement = doc.createElement("A_8"); // total zile lucrate
					childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getZilelucrate())));
					sbfrmSectiuneaA.appendChild(childElement);

					childElement = doc.createElement("A_9"); // baza de calcul somaj
					childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getSalariurealizat())));
					sbfrmSectiuneaA.appendChild(childElement);
				}
				// -----------END SECTIUNEA A-------

				// -------SECTIUNEA B------ salariat normal + are concediu medical in luna
				// respectiva
				if (contract.getTip().compareTo("Contract de munca") == 0 && realizariRetineri.getZilecm() != 0) {
					sectiune = "B";
					Element sfmButoaneB = doc.createElement("sfmButoane");
					sbfrmPage1Asig.appendChild(sfmButoaneB);

					childElement = doc.createElement("rbl");
					childElement.appendChild(doc.createTextNode("3"));
					sfmButoaneB.appendChild(childElement);

					childElement = doc.createElement("flag");
					childElement.appendChild(doc.createTextNode("1"));
					sfmButoaneB.appendChild(childElement);

					Element rbl2B = doc.createElement("rbl2");
					sfmButoaneB.appendChild(rbl2B);

					childElement = doc.createElement("rbC");
					childElement.appendChild(doc.createTextNode("0"));
					rbl2B.appendChild(childElement);

					childElement = doc.createElement("rbB");
					childElement.appendChild(doc.createTextNode("2"));
					rbl2B.appendChild(childElement);

					childElement = doc.createElement("rbA");
					childElement.appendChild(doc.createTextNode("0"));
					rbl2B.appendChild(childElement);

					Element sbfrmSectiuneaB = doc.createElement("sbfrmSectiuneaB");
					sbfrmPage1Asig.appendChild(sbfrmSectiuneaB);

					childElement = doc.createElement("calc_aut");
					childElement.appendChild(doc.createTextNode("1"));
					sbfrmSectiuneaB.appendChild(childElement);

					Element sbfrmSectiuneaB1rep = doc.createElement("sbfrmSectiuneaB1rep");
					sbfrmSectiuneaB.appendChild(sbfrmSectiuneaB1rep);

					Element sbfrmSectiuneaB1 = doc.createElement("sbfrmSectiuneaB1");
					sbfrmSectiuneaB1rep.appendChild(sbfrmSectiuneaB1);

					childElement = doc.createElement("VB_B"); // total drepturi
					childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getTotaldrepturi())));
					sbfrmSectiuneaB1.appendChild(childElement);

					childElement = doc.createElement("B1_1");
					childElement.appendChild(doc.createTextNode("1"));
					sbfrmSectiuneaB1.appendChild(childElement);

					childElement = doc.createElement("B1_2"); // pensionar
					childElement.appendChild(doc.createTextNode(contract.isPensionar() ? "1" : "0"));
					sbfrmSectiuneaB1.appendChild(childElement);

					childElement = doc.createElement("B1_3"); // tip contract
					childElement.appendChild(doc.createTextNode(realizariRetineri.getDuratazilucru() == 8 ? "N" : "P" + realizariRetineri.getDuratazilucru()));
					sbfrmSectiuneaB1.appendChild(childElement);

					childElement = doc.createElement("B1_4"); // norma ore
					childElement.appendChild(doc.createTextNode("8"));
					sbfrmSectiuneaB1.appendChild(childElement);

					childElement = doc.createElement("B1_6"); // ore lucrate efectiv in luna
					childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getOrelucrate())));
					sbfrmSectiuneaB1.appendChild(childElement);

					childElement = doc.createElement("B1_7"); // ore suspendate in luna
					childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getZilecm() * realizariRetineri.getDuratazilucru())));
					sbfrmSectiuneaB1.appendChild(childElement);

					childElement = doc.createElement("B1_15"); // total zile lucrate
					childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getZilelucrate())));
					sbfrmSectiuneaB1.appendChild(childElement);

					childElement = doc.createElement("B1_5"); // baza calcul CAM (total drepturi probabil)
					childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getTotaldrepturi())));
					sbfrmSectiuneaB1.appendChild(childElement);

					childElement = doc.createElement("B1_10"); // baza calcul indemnizatie somaj (total drepturi
					// probabil)
					childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getTotaldrepturi())));
					sbfrmSectiuneaB1.appendChild(childElement);

					childElement = doc.createElement("tfNrCrt");
					childElement.appendChild(doc.createTextNode(""));
					sbfrmSectiuneaB1.appendChild(childElement);

					childElement = doc.createElement("tichete_B");
					childElement.appendChild(doc.createTextNode(""));
					sbfrmSectiuneaB1.appendChild(childElement);

					Element SbfrmSectiuneaB2 = doc.createElement("SbfrmSectiuneaB2");
					sbfrmSectiuneaB.appendChild(SbfrmSectiuneaB2);

					childElement = doc.createElement("B2_2"); // total zile lucrate
					childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getZilelucrate())));
					SbfrmSectiuneaB2.appendChild(childElement);

					Double indem_fnuass = 0d;
					Double indem_firma = 0d;
					for (CM cm : cmService.getCMInLunaAnul(luna, an, contract.getId())) {
						indem_fnuass += cm.getIndemnizatiefnuass();
						indem_firma += cm.getIndemnizatiefirma();
					}

					// contributii sociale (totaldrepturi indemnizatii - indemnizatii firma cm)
					childElement = doc.createElement("B2_5");
					childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getTotaldrepturi() - indem_fnuass - indem_firma)));
					SbfrmSectiuneaB2.appendChild(childElement);

					childElement = doc.createElement("B2_6");
					childElement.appendChild(doc.createTextNode(""));
					SbfrmSectiuneaB2.appendChild(childElement);

					childElement = doc.createElement("B2_7");
					childElement.appendChild(doc.createTextNode(""));
					SbfrmSectiuneaB2.appendChild(childElement);

					Element sbfrmSectiuneaB3 = doc.createElement("sbfrmSectiuneaB3");
					sbfrmSectiuneaB.appendChild(sbfrmSectiuneaB3);

					// zile indemnizatie nr de zile lucratoare din timpul CM)
					childElement = doc.createElement("B3_1");
					childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getZilecmlucratoare())));
					sbfrmSectiuneaB3.appendChild(childElement);

					childElement = doc.createElement("B3_6");
					childElement.appendChild(doc.createTextNode(""));
					sbfrmSectiuneaB3.appendChild(childElement);

					childElement = doc.createElement("B3_7");
					childElement.appendChild(doc.createTextNode(""));
					sbfrmSectiuneaB3.appendChild(childElement);

					childElement = doc.createElement("B3_11");
					childElement.appendChild(doc.createTextNode(""));
					sbfrmSectiuneaB3.appendChild(childElement);

					childElement = doc.createElement("B3_12");
					childElement.appendChild(doc.createTextNode(""));
					sbfrmSectiuneaB3.appendChild(childElement);

					childElement = doc.createElement("B3_13");
					childElement.appendChild(doc.createTextNode(""));
					sbfrmSectiuneaB3.appendChild(childElement);

					Element facilitati = doc.createElement("facilitati");
					sbfrmSectiuneaB3.appendChild(facilitati);

					Element sbfrmSectiuneaB4 = doc.createElement("sbfrmSectiuneaB4");
					sbfrmSectiuneaB.appendChild(sbfrmSectiuneaB4);

					childElement = doc.createElement("B4_6");
					childElement.appendChild(doc.createTextNode(""));
					sbfrmSectiuneaB4.appendChild(childElement);

					childElement = doc.createElement("B4_7");
					childElement.appendChild(doc.createTextNode(""));
					sbfrmSectiuneaB4.appendChild(childElement);

					childElement = doc.createElement("B4_8");
					childElement.appendChild(doc.createTextNode(""));
					sbfrmSectiuneaB4.appendChild(childElement);

					childElement = doc.createElement("B4_14");
					childElement.appendChild(doc.createTextNode(""));
					sbfrmSectiuneaB4.appendChild(childElement);

					childElement = doc.createElement("B4_5");
					childElement.appendChild(doc.createTextNode(""));
					sbfrmSectiuneaB4.appendChild(childElement);

					Element facilitati2 = doc.createElement("facilitati");
					sbfrmSectiuneaB4.appendChild(facilitati2);

					childElement = doc.createElement("B4_5f");
					childElement.appendChild(doc.createTextNode(""));
					facilitati2.appendChild(childElement);

					childElement = doc.createElement("B4_6f");
					childElement.appendChild(doc.createTextNode(""));
					facilitati2.appendChild(childElement);

					childElement = doc.createElement("B4_7f");
					childElement.appendChild(doc.createTextNode(""));
					facilitati2.appendChild(childElement);

					childElement = doc.createElement("B4_8f");
					childElement.appendChild(doc.createTextNode(""));
					facilitati2.appendChild(childElement);

					childElement = doc.createElement("B4_14f");
					childElement.appendChild(doc.createTextNode(""));
					facilitati2.appendChild(childElement);

					childElement = doc.createElement("B4_5i");
					childElement.appendChild(doc.createTextNode(""));
					facilitati2.appendChild(childElement);

					childElement = doc.createElement("B4_6i");
					childElement.appendChild(doc.createTextNode(""));
					facilitati2.appendChild(childElement);

					childElement = doc.createElement("B4_7i");
					childElement.appendChild(doc.createTextNode(""));
					facilitati2.appendChild(childElement);

					childElement = doc.createElement("B4_8i");
					childElement.appendChild(doc.createTextNode(""));
					facilitati2.appendChild(childElement);

					childElement = doc.createElement("B4_14i");
					childElement.appendChild(doc.createTextNode(""));
					facilitati2.appendChild(childElement);

					childElement = doc.createElement("SalBrut_B");
					childElement.appendChild(doc.createTextNode(""));
					facilitati2.appendChild(childElement);

					childElement = doc.createElement("B_81");
					childElement.appendChild(doc.createTextNode(""));
					facilitati2.appendChild(childElement);

					childElement = doc.createElement("B_82");
					childElement.appendChild(doc.createTextNode(""));
					facilitati2.appendChild(childElement);

					childElement = doc.createElement("B4_2");
					childElement.appendChild(doc.createTextNode(""));
					sbfrmSectiuneaB4.appendChild(childElement);

					childElement = doc.createElement("B4_1");
					childElement.appendChild(doc.createTextNode(""));
					sbfrmSectiuneaB4.appendChild(childElement);

					// ---------SECTIUNEA D ------- DACA EXISTA SECTIUNEA B
					for (CM cm : cmService.getCMInLunaAnul(luna, an, contract.getId())) {

						Element sbfrmSectiuneaDAsig = doc.createElement("sbfrmSectiuneaD");
						sbfrmPage1Asig.appendChild(sbfrmSectiuneaDAsig);

						Element sbfrmSectiuneaDrep = doc.createElement("sbfrmSectiuneaDrep");
						sbfrmSectiuneaDAsig.appendChild(sbfrmSectiuneaDrep);

						childElement = doc.createElement("D_1"); // seria certificatului medical
						childElement.appendChild(doc.createTextNode(cm.getSerie()));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_2"); // numarul certificatului medical
						childElement.appendChild(doc.createTextNode(cm.getNr()));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_5");// data acordarii certificatului medical
						childElement.appendChild(doc.createTextNode(cm.getDataeliberare() == null ? "" : formatter.format(cm.getDataeliberare())));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_6"); // data inceput valabilitate cm
						childElement.appendChild(doc.createTextNode(cm.getDela() == null ? "" : formatter.format(cm.getDela())));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_7"); // data sfarsit valabilitate cm
						childElement.appendChild(doc.createTextNode(cm.getPanala() == null ? "" : formatter.format(cm.getPanala())));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_8"); // cnp copil
						childElement.appendChild(doc.createTextNode(cm.getCnpcopil() == null ? "" : String.valueOf(cm.getCnpcopil())));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_9"); // codul indemnizatiei (1 -> 15) == tipul de boala
						childElement.appendChild(doc.createTextNode(cm.getCodindemnizatie()));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_10"); // locul de prescriere
						childElement.appendChild(doc.createTextNode(cm.getLocprescriere()));
						// (1->4)
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_11");// codul de urgenta
						childElement.appendChild(doc.createTextNode(cm.getCodurgenta()));
						// medico-chirurgicala
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_12"); // Cod boală infectocontagioasă grupa A
						childElement.appendChild(doc.createTextNode(cm.getCodboalainfcont()));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_14"); // zile lucratoare suportate de angajator
						childElement.appendChild(doc.createTextNode(String.valueOf(cm.getZilefirma())));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_15"); // zile lucratoare suportate de fnuass
						childElement.appendChild(doc.createTextNode(String.valueOf(cm.getZilefnuass())));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_17"); // suma veniturilor in ultimele 6 luni
						childElement.appendChild(doc.createTextNode(String.valueOf(cm.getBazacalcul())));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_18"); // nr zile lucratoare in ultimele 6 luni
						childElement.appendChild(doc.createTextNode(String.valueOf(cm.getZilebazacalcul())));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_19"); // media zilnica a bazei de calcul
						childElement.appendChild(doc.createTextNode(String.valueOf(cm.getMediezilnica())));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_20"); // indemnizatie suportata de angajator
						childElement.appendChild(doc.createTextNode(String.valueOf(cm.getIndemnizatiefirma())));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_21"); // indemnizatie FNUASS
						childElement.appendChild(doc.createTextNode(String.valueOf(cm.getIndemnizatiefnuass())));
						sbfrmSectiuneaDrep.appendChild(childElement);

						childElement = doc.createElement("D_23");// cod boala
						childElement.appendChild(doc.createTextNode(cm.getCodboala()));
						sbfrmSectiuneaDrep.appendChild(childElement);
					}
				}

				// ---------SECTIUNEA C-------------avem salariatii care nu au contract de
				// munca,
				// dar care au venituri assimilate salariilor.
				// Aici pot fi incluse contractele de administrare.
				if (contract.getTip().compareTo("Contract de administrator") == 0) {
					sectiune = "C";
					Element sbfrmSectiuneaCAsig = doc.createElement("sbfrmSectiuneaC");
					sbfrmPage1Asig.appendChild(sbfrmSectiuneaCAsig);

					Element SectiuneaC = doc.createElement("SectiuneaC");
					sbfrmSectiuneaCAsig.appendChild(SectiuneaC);

					childElement = doc.createElement("ID_C");
					childElement.appendChild(doc.createTextNode(""));
					SectiuneaC.appendChild(childElement);

					childElement = doc.createElement("C_2");
					childElement.appendChild(doc.createTextNode(""));
					SectiuneaC.appendChild(childElement);

					childElement = doc.createElement("C_5");
					childElement.appendChild(doc.createTextNode(""));
					SectiuneaC.appendChild(childElement);

					childElement = doc.createElement("C_3");
					childElement.appendChild(doc.createTextNode(""));
					SectiuneaC.appendChild(childElement);

					childElement = doc.createElement("C_17");
					childElement.appendChild(doc.createTextNode(""));
					SectiuneaC.appendChild(childElement);

					childElement = doc.createElement("C_19");
					childElement.appendChild(doc.createTextNode(""));
					SectiuneaC.appendChild(childElement);

					childElement = doc.createElement("C_4");
					childElement.appendChild(doc.createTextNode(""));
					SectiuneaC.appendChild(childElement);

					childElement = doc.createElement("C_18");
					childElement.appendChild(doc.createTextNode(""));
					SectiuneaC.appendChild(childElement);

					childElement = doc.createElement("C_8");
					childElement.appendChild(doc.createTextNode(""));
					SectiuneaC.appendChild(childElement);

					childElement = doc.createElement("C_9");
					childElement.appendChild(doc.createTextNode(""));
					SectiuneaC.appendChild(childElement);

					childElement = doc.createElement("C_10");
					childElement.appendChild(doc.createTextNode(""));
					SectiuneaC.appendChild(childElement);

					childElement = doc.createElement("C_11");
					childElement.appendChild(doc.createTextNode(""));
					SectiuneaC.appendChild(childElement);
				}

				// ------SECTIUNEA E----

				Element sbfrmSectiuneaEAsig = doc.createElement("sbfrmSectiuneaE");
				sbfrmPage1Asig.appendChild(sbfrmSectiuneaEAsig);

				Element sbfrmSectiuneaE3 = doc.createElement("sbfrmSectiuneaE3");
				sbfrmSectiuneaEAsig.appendChild(sbfrmSectiuneaE3);

				childElement = doc.createElement("ID_E");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_1"); // sectiune asigurat
				childElement.appendChild(doc.createTextNode(sectiune));
				sbfrmSectiuneaE3.appendChild(childElement);

				// tip asigurat, daca are studii sup si vechime atunci este 26, altfel este 1
				childElement = doc.createElement("E3_2");
				childElement.appendChild(doc.createTextNode(contract.isStudiisuperioare() ? "26" : "1"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_3"); // functie de baza, daca are atunci este 1, altfel e 2
				childElement.appendChild(doc.createTextNode(contract.isFunctiedebaza() ? "1" : "2"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_4"); // tip venit referitor la perioada de raportare
				childElement.appendChild(doc.createTextNode("P"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_5");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_6");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_37");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_38");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_39");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_40");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_41");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_42");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_8"); // venit brut (probabil total drepturi)
				childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getTotaldrepturi() + realizariRetineri.getValoaretichete())));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_9"); // contributii sociale obligatorii (cas+cass)
				childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getCas() + realizariRetineri.getCass())));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_10"); // tichete de masa acordate
				childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getValoaretichete())));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_20"); // tichete de masa restituite
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_43");
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_43");
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_18");
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_21");
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_23");
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_24");
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_25"); // venit scutit
				childElement.appendChild(doc.createTextNode(!contract.isCalculdeduceri() ? String.valueOf(realizariRetineri.getTotaldrepturi() + realizariRetineri.getValoaretichete() - realizariRetineri.getCas() - realizariRetineri.getCass()) : "0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_26"); // impozit scutit
				childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getImpozitscutit())));
				sbfrmSectiuneaE3.appendChild(childElement);

				///// ?????????????

				childElement = doc.createElement("E3_27");
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_28");
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_29");
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_30");
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_19");
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_31");
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_22");
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_32");
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				///// ?????????????

				childElement = doc.createElement("E3_11"); // nr persoane deducere
				childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getNrpersoaneintretinere())));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_12"); // deduceri personale
				childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getDeducere())));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_13"); // alte deduceri
				childElement.appendChild(doc.createTextNode("0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_14"); // baza calcul impozit (venitBrut + tichete - cas - cass)
				childElement.appendChild(doc.createTextNode(contract.isCalculdeduceri() ? String.valueOf(realizariRetineri.getBazaimpozit()) : "0"));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_15"); // de completat impozit, adica baza calcul impozit
				childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getImpozit())));
				// impozit (procentual/indice)
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_16"); // de completat impozit incasat bazaImpozit-impozit
				childElement.appendChild(doc.createTextNode(String.valueOf(realizariRetineri.getTotaldrepturi() + realizariRetineri.getValoaretichete() - realizariRetineri.getCas() - realizariRetineri.getCass() - realizariRetineri.getImpozit())));
				sbfrmSectiuneaE3.appendChild(childElement);

				Element sbfrmSectiuneaE4_ab = doc.createElement("sbfrmSectiuneaE4_ab");
				sbfrmSectiuneaEAsig.appendChild(sbfrmSectiuneaE4_ab);

				childElement = doc.createElement("ID_E3");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE4_ab.appendChild(childElement);

				childElement = doc.createElement("cota_ctr");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE4_ab.appendChild(childElement);

				childElement = doc.createElement("suma_ctr");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE4_ab.appendChild(childElement);

				childElement = doc.createElement("cota");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE4_ab.appendChild(childElement);

				childElement = doc.createElement("suma");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE4_ab.appendChild(childElement);

				Element sbfrmSectiuneaE4_c = doc.createElement("sbfrmSectiuneaE4_c");
				sbfrmSectiuneaEAsig.appendChild(sbfrmSectiuneaE4_c);

				childElement = doc.createElement("Tcota");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE4_c.appendChild(childElement);

				childElement = doc.createElement("Tsuma");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE4_c.appendChild(childElement);

				childElement = doc.createElement("Timp");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE4_c.appendChild(childElement);

			}
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			Files.createDirectories(Paths.get(homeLocation + "downloads/" + userID));
			String newFileLocation = String.format("%s/downloads/%d/Declaratia 112 - %s - %s %d.xml", homeLocation, userID, societate.getNume(), lunaNume, an);

			StreamResult result = new StreamResult(new File(newFileLocation));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			var pdfReader = new PdfReader(homeLocation + "templates/D112_"+an+".pdf");
			String newFileLocationPDF = String.format("%s/downloads/%d/Declaratia 112 - %s - %s %d.pdf", homeLocation, userID, societate.getNume(), lunaNume, an);
			var outputStream = new FileOutputStream(newFileLocationPDF);

			PdfStamper stamper = new PdfStamper(pdfReader, outputStream, '\0', true);

			stamper.getAcroFields().getXfa().fillXfaForm(new File(newFileLocation));

			stamper.close();

		} catch (

		ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (DocumentException de) {
			de.printStackTrace();
		}

		return true;
	}

}
