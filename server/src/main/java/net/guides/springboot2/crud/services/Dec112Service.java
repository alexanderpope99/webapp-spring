package net.guides.springboot2.crud.services;

import java.io.IOException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

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
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Persoana;
import net.guides.springboot2.crud.model.PersoanaIntretinere;
import net.guides.springboot2.crud.model.Societate;
import net.guides.springboot2.crud.repository.PersoanaRepository;
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
		Societate societate = societateRepository.findById(idsocietate)
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

			Element childElement = doc.createElement("d_rec");
			childElement.appendChild(doc.createTextNode(String.valueOf(drec)));
			sfmIdentif.appendChild(childElement);

			childElement = doc.createElement("luna_r");
			childElement.appendChild(doc.createTextNode(lunaNume));
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
			childElement.appendChild(doc.createTextNode(""));
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
			childElement.appendChild(doc.createTextNode(String.valueOf(societate.getCif())));
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

			childElement = doc.createElement("art90");
			childElement.appendChild(doc.createTextNode(societate.getRegcom()));
			sfmIdentif2.appendChild(childElement);

			childElement = doc.createElement("adrSoc");
			childElement.appendChild(doc.createTextNode(societate.getAdresa().getAdresa()));
			sfmIdentif2.appendChild(childElement);

			childElement = doc.createElement("telSoc");
			childElement.appendChild(doc.createTextNode(societate.getTelefon()));
			sfmIdentif2.appendChild(childElement);

			childElement = doc.createElement("faxSoc");
			childElement.appendChild(doc.createTextNode(""));
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

			childElement = doc.createElement("a_datorat");
			childElement.appendChild(doc.createTextNode(""));// ----------------de completat datorat impozit + impozit
			sfmSectAVal.appendChild(childElement);

			childElement = doc.createElement("a_deductibil");
			childElement.appendChild(doc.createTextNode("0"));
			sfmSectAVal.appendChild(childElement);

			childElement = doc.createElement("a_scutit");
			childElement.appendChild(doc.createTextNode(""));// ----------------de completat scutit
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

			childElement = doc.createElement("a_datorat");
			childElement.appendChild(doc.createTextNode(""));// ----------------de completat datorat cas
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

			childElement = doc.createElement("a_datorat");
			childElement.appendChild(doc.createTextNode(""));// ----------------de completat datorat cass
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

			childElement = doc.createElement("a_datorat");
			childElement.appendChild(doc.createTextNode(""));// ----------------de completat datorat cam
			sfmSectAVal4.appendChild(childElement);

			childElement = doc.createElement("a_deductibil");
			childElement.appendChild(doc.createTextNode("0"));
			sfmSectAVal4.appendChild(childElement);

			childElement = doc.createElement("a_scutit");
			childElement.appendChild(doc.createTextNode("0"));
			sfmSectAVal4.appendChild(childElement);

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
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmF1.appendChild(childElement);

			childElement = doc.createElement("F1_suma_ded");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmF1.appendChild(childElement);

			childElement = doc.createElement("F1_suma_scut");
			childElement.appendChild(doc.createTextNode("0"));
			sbfrmF1.appendChild(childElement);

			// end sbfrmSectiuneaF

			// end sbfrmPage1Ang

			List<Angajat> angajati = societate.getAngajat().stream().filter((angajat) -> angajat.getContract() != null)
					.collect(Collectors.toList());

			int c = 1;
			DateFormat dateFormat = new SimpleDateFormat("dd.mm.YYYY");
			for (Angajat angajat : angajati) {
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

				childElement = doc.createElement("Pren_asig");
				childElement.appendChild(doc.createTextNode(dateFormat.format(angajat.getContract().getData())));
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("Casa_sn");
				if (angajat.getContract().getCasasanatate().length() == 0
						|| angajat.getContract().getCasasanatate().length() == 1) {
					childElement.appendChild(doc.createTextNode(""));
				} else {
					String s = angajat.getContract().getCasasanatate();
					childElement.appendChild(doc.createTextNode(s.substring(s.length() - 2)));
				}
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("Asig_ci");
				childElement.appendChild(doc.createTextNode("1"));
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("Asig_so");
				childElement.appendChild(doc.createTextNode("1"));
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("asigScu");
				childElement.appendChild(doc.createTextNode(angajat.getContract().isCalculdeduceri() ? "2" : "0"));
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("an_r");
				childElement.appendChild(doc.createTextNode(String.valueOf(an)));
				sfmDateIdentif.appendChild(childElement);

				childElement = doc.createElement("luna_r");
				childElement.appendChild(
						doc.createTextNode(luna <= 9 ? ("0" + String.valueOf(luna)) : (String.valueOf(luna))));
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
				for (PersoanaIntretinere persInt : angajat.getPersoaneIntretinere()) {
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
							childElement = doc.createElement("prenParinte" + String.valueOf(nrParinti > 0 + 1));
							childElement.appendChild(doc.createTextNode(persInt.getPrenume()));
							sfmDateIdentif.appendChild(childElement);
							childElement = doc.createElement("numeParinte" + String.valueOf(nrParinti > 0 + 1));
							childElement.appendChild(doc.createTextNode(persInt.getNume()));
							sfmDateIdentif.appendChild(childElement);
							childElement = doc.createElement("cnpParinte" + String.valueOf(nrParinti > 0 + 1));
							childElement.appendChild(doc.createTextNode(persInt.getCnp()));
							sfmDateIdentif.appendChild(childElement);
							break;
						default:
							break;
					}
				}

				// -------SECTIUNEA A------ salariat normal, fara concediu medical in luna
				// respectiva

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

				childElement = doc.createElement("VB_A");
				childElement.appendChild(doc.createTextNode("")); // de completat venit brut
				sbfrmSectiuneaA.appendChild(childElement);

				childElement = doc.createElement("tichete_A");
				childElement.appendChild(doc.createTextNode("")); // de completat tichete
				sbfrmSectiuneaA.appendChild(childElement);

				childElement = doc.createElement("A_1");
				childElement.appendChild(doc.createTextNode("1")); // tip asigurat
				sbfrmSectiuneaA.appendChild(childElement);

				childElement = doc.createElement("A_2");
				childElement.appendChild(doc.createTextNode("")); // de completat pensionar sau nu
				sbfrmSectiuneaA.appendChild(childElement);

				childElement = doc.createElement("A_3");
				childElement.appendChild(doc.createTextNode("")); // de completat tip contract (daca durata zi lucru
																	// este 8 atunci punem N, daca este diferti de 8
																	// punem P urmat de nr de ore dintr-o zi de lucru)
				sbfrmSectiuneaA.appendChild(childElement);

				childElement = doc.createElement("A_4");
				childElement.appendChild(doc.createTextNode("8")); // norma ore
				sbfrmSectiuneaA.appendChild(childElement);

				childElement = doc.createElement("A_6");
				childElement.appendChild(doc.createTextNode("")); // de completat ore lucrate efectiv in luna
				sbfrmSectiuneaA.appendChild(childElement);

				childElement = doc.createElement("A_7");
				childElement.appendChild(doc.createTextNode("0")); // ore suspendate in luna
				sbfrmSectiuneaA.appendChild(childElement);

				childElement = doc.createElement("A_8");
				childElement.appendChild(doc.createTextNode("")); // de completat total zile lucrate
				sbfrmSectiuneaA.appendChild(childElement);

				childElement = doc.createElement("A_9");
				childElement.appendChild(doc.createTextNode("")); // baza de calcul somaj
				sbfrmSectiuneaA.appendChild(childElement);

				// -----------END SECTIUNEA A-------

				// -------SECTIUNEA B------ salariat normal + are concediu medical in luna
				// respectiva

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

				childElement = doc.createElement("VB_B");
				childElement.appendChild(doc.createTextNode("")); // de completat total drepturi
				sbfrmSectiuneaB1.appendChild(childElement);

				childElement = doc.createElement("B1_1");
				childElement.appendChild(doc.createTextNode("1")); // de completat total drepturi
				sbfrmSectiuneaB1.appendChild(childElement);

				childElement = doc.createElement("B1_2");
				childElement.appendChild(doc.createTextNode("")); // de completat pensionar sau nu
				sbfrmSectiuneaB1.appendChild(childElement);

				childElement = doc.createElement("B1_3");
				childElement.appendChild(doc.createTextNode("")); // de completat tip contract (daca durata zi lucru
																	// este 8 atunci punem N, daca este diferti de 8
																	// punem P urmat de nr de ore dintr-o zi de lucru)
				sbfrmSectiuneaB1.appendChild(childElement);

				childElement = doc.createElement("B1_4");
				childElement.appendChild(doc.createTextNode("8")); // norma ore
				sbfrmSectiuneaB1.appendChild(childElement);

				childElement = doc.createElement("B1_6");
				childElement.appendChild(doc.createTextNode("")); // de completat ore lucrate efectiv in luna
				sbfrmSectiuneaB1.appendChild(childElement);

				childElement = doc.createElement("B1_7");
				childElement.appendChild(doc.createTextNode("0")); // ore suspendate in luna
				sbfrmSectiuneaB1.appendChild(childElement);

				childElement = doc.createElement("B1_15");
				childElement.appendChild(doc.createTextNode("")); // de completat total zile lucrate
				sbfrmSectiuneaB1.appendChild(childElement);

				childElement = doc.createElement("B1_5");
				childElement.appendChild(doc.createTextNode("")); // de completat baza calcul CAM (total drepturi
																	// probabil)
				sbfrmSectiuneaB1.appendChild(childElement);

				childElement = doc.createElement("B1_10");
				childElement.appendChild(doc.createTextNode("")); // de completat baza calcul indemnizatie somaj (total
																	// drepturi probabil)
				sbfrmSectiuneaB1.appendChild(childElement);

				childElement = doc.createElement("tfNrCrt");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaB1.appendChild(childElement);

				childElement = doc.createElement("tichete_B");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaB1.appendChild(childElement);

				Element SbfrmSectiuneaB2 = doc.createElement("SbfrmSectiuneaB2");
				sbfrmSectiuneaB.appendChild(SbfrmSectiuneaB2);

				childElement = doc.createElement("B2_2");
				childElement.appendChild(doc.createTextNode("")); // de completat total zile lucrate
				SbfrmSectiuneaB2.appendChild(childElement);

				childElement = doc.createElement("B2_5");
				childElement.appendChild(doc.createTextNode("")); // de completat contributii sociale (totaldrepturi -
																	// indemnizatii fnuass cm - indemnizatii firma cm)
				SbfrmSectiuneaB2.appendChild(childElement);

				childElement = doc.createElement("B2_6");
				childElement.appendChild(doc.createTextNode(""));
				SbfrmSectiuneaB2.appendChild(childElement);

				childElement = doc.createElement("B2_7");
				childElement.appendChild(doc.createTextNode(""));
				SbfrmSectiuneaB2.appendChild(childElement);

				Element sbfrmSectiuneaB3 = doc.createElement("sbfrmSectiuneaB3");
				sbfrmSectiuneaB.appendChild(sbfrmSectiuneaB3);

				childElement = doc.createElement("B3_1");
				childElement.appendChild(doc.createTextNode("")); // de completat zile indemnizatie (adica nr de zile
																	// lucratoare din timpul CM)
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

				Element sbfrmSectiuneaDAsig = doc.createElement("sbfrmSectiuneaD");
				sbfrmPage1Asig.appendChild(sbfrmSectiuneaDAsig);

				Element sbfrmSectiuneaDrep = doc.createElement("sbfrmSectiuneaDrep");
				sbfrmSectiuneaDAsig.appendChild(sbfrmSectiuneaDrep);

				childElement = doc.createElement("D_1");
				childElement.appendChild(doc.createTextNode("")); // de completat seria certificatului medical
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_2");
				childElement.appendChild(doc.createTextNode("")); // de completat numarul certificatului medical
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_5");
				childElement.appendChild(doc.createTextNode("")); // de completat data acordarii certificatului medical
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_6");
				childElement.appendChild(doc.createTextNode("")); // de completat data inceput valabilitate cm
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_7");
				childElement.appendChild(doc.createTextNode("")); // de completat data sfarsit valabilitate cm
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_8");
				childElement.appendChild(doc.createTextNode("")); // de completat cnp copil
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_9");
				childElement.appendChild(doc.createTextNode("")); // de completat codul indemnizatiei (1 -> 15) == tipul
																	// de boala
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_10");
				childElement.appendChild(doc.createTextNode("")); // de completat locul de prescriere (1->4)
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_11");
				childElement.appendChild(doc.createTextNode("")); // de completat codul de urgenta medico-chirurgicala
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_12");
				childElement.appendChild(doc.createTextNode("")); // Cod boală infectocontagioasă grupa A
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_14");
				childElement.appendChild(doc.createTextNode("")); // de completat zile lucratoare suportate de angajator
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_15");
				childElement.appendChild(doc.createTextNode("")); // de completat zile lucratoare suportate de fnuass
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_17");
				childElement.appendChild(doc.createTextNode("")); // de completat suma veniturilor in ultimele 6 luni
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_18");
				childElement.appendChild(doc.createTextNode("")); // de completat nr zile lucratoare in ultimele 6 luni
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_19");
				childElement.appendChild(doc.createTextNode("")); // de completat media zilnica a bazei de calcul
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_20");
				childElement.appendChild(doc.createTextNode("")); // de completat indemnizatie suportata de angajator
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_21");
				childElement.appendChild(doc.createTextNode("")); // de completat indemnizatie FNUASS
				sbfrmSectiuneaDrep.appendChild(childElement);

				childElement = doc.createElement("D_23");
				childElement.appendChild(doc.createTextNode("")); // de completat cod boala
				sbfrmSectiuneaDrep.appendChild(childElement);

				// ---------SECTIUNEA C-------------avem salariatii care nu au contract de
				// munca,
				// dar care au venituri assimilate salariilor.
				// Aici pot fi incluse contractele de administrare.

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

				// ------SECTIUNEA E----

				Element sbfrmSectiuneaEAsig = doc.createElement("sbfrmSectiuneaE");
				sbfrmPage1Asig.appendChild(sbfrmSectiuneaEAsig);

				Element sbfrmSectiuneaE3 = doc.createElement("sbfrmSectiuneaE3");
				sbfrmSectiuneaEAsig.appendChild(sbfrmSectiuneaE3);

				childElement = doc.createElement("ID_E");
				childElement.appendChild(doc.createTextNode(""));
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_1");
				childElement.appendChild(doc.createTextNode("")); // de completat sectiune asigurat
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_2");
				childElement.appendChild(doc.createTextNode("")); // de completat tip asigurat, daca are studii sup si
																	// vechime atunci este 26, altfel este 1
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_3");
				childElement.appendChild(doc.createTextNode("")); // de completat functie de baza, daca are atunci este
																	// 1, altfel e 2
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_4");
				childElement.appendChild(doc.createTextNode("P")); // tip venit referitor la perioada de raportare
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_8");
				childElement.appendChild(doc.createTextNode("")); // de completat venit brut (probabil total drepturi)
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_9");
				childElement.appendChild(doc.createTextNode("")); // de completat contributii sociale obligatorii
																	// (cas+cass)
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_10");
				childElement.appendChild(doc.createTextNode("")); // de completat tichete de masa acordate
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_20");
				childElement.appendChild(doc.createTextNode("0")); // tichete de masa restituite
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

				childElement = doc.createElement("E3_25");
				childElement.appendChild(doc.createTextNode("")); // de completat venit scutit
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_26");
				childElement.appendChild(doc.createTextNode("")); // de completat impozit scutit
				sbfrmSectiuneaE3.appendChild(childElement);

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

				childElement = doc.createElement("E3_11");
				childElement.appendChild(doc.createTextNode("")); // de completat nr persoane deducere
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_12");
				childElement.appendChild(doc.createTextNode("")); // de completat deduceri persoanle
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_13");
				childElement.appendChild(doc.createTextNode("0")); // alte deduceri
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_14");
				childElement.appendChild(doc.createTextNode("")); // de completat baza calcul impozit (venitBrut +
																	// tichete - cas - cass)
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_15");
				childElement.appendChild(doc.createTextNode("")); // de completat impozit, adica baza calcul impozit *
																	// impozit (procentual/indice)
				sbfrmSectiuneaE3.appendChild(childElement);

				childElement = doc.createElement("E3_16");
				childElement.appendChild(doc.createTextNode("")); // de completat impozit incasat bazaImpozit-impozit
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

			Files.createDirectories(Paths.get(homeLocation + "downloads\\" + userID));
			String newFileLocation = String.format("%s\\downloads\\%d\\Declaratia 112 - %s - %s %d.xml", homeLocation,
					userID, societate.getNume(), lunaNume, an);

			StreamResult result = new StreamResult(new File(newFileLocation));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (

		ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
		return true;
	}

}
