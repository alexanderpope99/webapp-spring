package net.guides.springboot2.crud.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.dto.BazaCalculCMDTO;
import net.guides.springboot2.crud.dto.BazacalculDTO;
import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Angajat;
import net.guides.springboot2.crud.model.Bazacalcul;
import net.guides.springboot2.crud.model.RealizariRetineri;
import net.guides.springboot2.crud.repository.AngajatRepository;
import net.guides.springboot2.crud.repository.BazacalculRepository;

@Service
public class BazacalculService {
	@Autowired
	private BazacalculRepository bazacalculRepository;
	@Autowired
	private AngajatRepository angajatRepository;

	@Autowired
	private ModelMapper modelMapper;

	public BazacalculDTO save(BazacalculDTO bazacalculDTO) throws ResourceNotFoundException {
		Bazacalcul bc = modelMapper.map(bazacalculDTO, Bazacalcul.class);
		Angajat angajat = angajatRepository.findById(bazacalculDTO.getIdangajat()).orElseThrow(() -> new ResourceNotFoundException("Nu există angajat cu id: " + bazacalculDTO.getIdangajat()));

		bc.setAngajat(angajat);
		bazacalculRepository.save(bc);
		return bazacalculDTO;
	}

	public BazacalculDTO update(int id, BazacalculDTO bazacalculDTO) throws ResourceNotFoundException {
		bazacalculDTO.setId(id);
		return save(bazacalculDTO);
	}

	public List<Bazacalcul> getBazaCalculUltimele6Luni(int luna, int an, int idangajat) {
		int luna6, an6 = an;
		if (luna <= 6) {
			luna6 = 12 - (6 - luna);
			an6--;
		} else
			luna6 = luna - 6;
		// boolean areBazacalcul = bazacalculRepository.existsByLunaAndAnAndAngajat_Idpersoana(luna6, an6, idangajat);
		// if (!areBazacalcul)
		// 	return new ArrayList<>();

		List<Bazacalcul> rv = new ArrayList<>();

		// daca bazacalcul include anul trecut
		Bazacalcul bc;
		if (luna6 > luna && an6 < an) {
			for (int i = luna-1; i >= 1; --i) {
				bc = bazacalculRepository.findByLunaAndAnAndAngajat_Idpersoana(i, an, idangajat);
				if (bc == null)
					return rv;
				rv.add(bc);
			}
			for (int i = 12; i >= luna6; --i) {
				bc = bazacalculRepository.findByLunaAndAnAndAngajat_Idpersoana(i, an6, idangajat);
				if (bc == null)
					return rv;
				rv.add(bc);
			}
		} // daca cele 6 luni sunt in anul selectat
		else {
			for (int i = luna-1; i >= luna6; --i) {
				bc = bazacalculRepository.findByLunaAndAnAndAngajat_Idpersoana(i, an, idangajat);
				if (bc == null)
					return rv;
				rv.add(bc);
			}
		}

		return rv;
	}

	public Bazacalcul saveBazacalcul(RealizariRetineri realizariRetineri) {
		// contractul inca nu a inceput, deci o baza de calcul nu are sens
		if (realizariRetineri.getZilecontract() == 0)
			return null;

		int luna = realizariRetineri.getLuna();
		int an = realizariRetineri.getAn();

		// Angajat angajat =
		// angajatRepository.findByContract_Id(realizariRetineri.getContract().getId());
		Angajat angajat = realizariRetineri.getContract().getAngajat();

		if (bazacalculRepository.existsByLunaAndAnAndAngajat_Idpersoana(luna, an, angajat.getPersoana().getId())) {
			return null;
		}

		Bazacalcul bazaCalcul = new Bazacalcul(luna, an, (int) realizariRetineri.getZilelucrate(), (int) realizariRetineri.getSalariurealizat(), angajat);
		return bazacalculRepository.save(bazaCalcul);
	}

	public Bazacalcul updateBazacalcul(RealizariRetineri realizariRetineri) throws ResourceNotFoundException {
		int luna = realizariRetineri.getLuna();
		int an = realizariRetineri.getAn();

		Angajat angajat = angajatRepository.findByContract_Id(realizariRetineri.getContract().getId());
		if (angajat == null)
			throw new ResourceNotFoundException("Nu există angajat cu idcontract " + realizariRetineri.getContract().getId());

		Bazacalcul oldBazacalcul = bazacalculRepository.findByLunaAndAnAndAngajat_Idpersoana(luna, an, angajat.getPersoana().getId());

		if (oldBazacalcul == null)
			return this.saveBazacalcul(realizariRetineri);

		Bazacalcul bazaCalcul = new Bazacalcul(luna, an, realizariRetineri.getZilelucrate(), realizariRetineri.getSalariurealizat(), angajat);
		bazaCalcul.setId(oldBazacalcul.getId());
		return bazacalculRepository.save(bazaCalcul);
	}

	public BazaCalculCMDTO getBazaCalculCMDTO(int luna, int an, int idangajat, String codboala) {

		List<Bazacalcul> bazeUltimele6Luni = this.getBazaCalculUltimele6Luni(luna, an, idangajat);

		boolean special = "51,07,15,06,".contains(codboala.substring(0, 2));

		if (bazeUltimele6Luni.size() < 6 && !special)
			return new BazaCalculCMDTO(0, 0, 0);

		float bazaCalcul = 0;
		int zilebazacalcul = 0;
		for (Bazacalcul bc : bazeUltimele6Luni) {
			bazaCalcul += bc.getSalariurealizat();
			zilebazacalcul += bc.getZilelucrate();
		}

		float medieZilnica = zilebazacalcul > 0 ? (bazaCalcul / zilebazacalcul) : 0;
		return new BazaCalculCMDTO(bazaCalcul, zilebazacalcul, medieZilnica);
	}
}
