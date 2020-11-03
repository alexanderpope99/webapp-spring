package net.guides.springboot2.crud.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public List<Bazacalcul> getBazaCalculUltimele6Luni(int luna, int an, int idangajat) {
		int luna6, an6 = an;
		if (luna <= 6) {
			luna6 = 12 - (6 - luna);
			an6--;
		} else
			luna6 = luna - 6;
		boolean areBazacalcul = bazacalculRepository.existsByLunaAndAnAndIdangajat(luna6, an6, idangajat);
		if (!areBazacalcul)
			return null;

		List<Bazacalcul> rv = new ArrayList<>();

		// daca bazacalcul include anul trecut
		if (luna6 > luna && an6 < an) {
			for (int i = luna6; i <= 12; ++i) {
				rv.add(bazacalculRepository.findByLunaAndAnAndIdangajat(i, an6, idangajat));
			}
			for (int i = 1; i < luna; ++i) {
				rv.add(bazacalculRepository.findByLunaAndAnAndIdangajat(i, an, idangajat));
			}
		} // daca cele 6 luni sunt in anul selectat
		else {
			for (int i = luna6; i <= luna; ++i) {
				rv.add(bazacalculRepository.findByLunaAndAnAndIdangajat(i, an, idangajat));
			}
		}

		return rv;
	}

	public float getMediaZilnicaUltimele6Luni(int luna, int an, int idangajat) {
		int luna6 = 0, an6 = an;
		if (luna <= 6) {
			luna6 = 12 - (6 - luna);
			an6--;
		} else
			luna6 = luna - 6;
		boolean areBazacalcul = bazacalculRepository.existsByLunaAndAnAndIdangajat(luna6, an6, idangajat);
		if (!areBazacalcul)
			return 0;

		List<Bazacalcul> bazeCalculUltimele6Luni = new ArrayList<>();

		// daca bazacalcul include anul trecut
		if (luna6 > luna && an6 < an) {
			for (int i = luna6; i <= 12; ++i) {
				areBazacalcul = bazacalculRepository.existsByLunaAndAnAndIdangajat(i, an6, idangajat);
				if (!areBazacalcul)
					return 0;

				bazeCalculUltimele6Luni.add(bazacalculRepository.findByLunaAndAnAndIdangajat(i, an6, idangajat));
			}
			for (int i = 1; i < luna; ++i) {
				areBazacalcul = bazacalculRepository.existsByLunaAndAnAndIdangajat(i, an, idangajat);
				if (!areBazacalcul)
					return 0;

				bazeCalculUltimele6Luni.add(bazacalculRepository.findByLunaAndAnAndIdangajat(i, an, idangajat));
			}
		} // daca cele 6 luni sunt in anul selectat
		else {
			for (int i = luna6; i <= luna - 1; ++i) {
				areBazacalcul = bazacalculRepository.existsByLunaAndAnAndIdangajat(i, an, idangajat);
				if (!areBazacalcul)
					return 0;

				bazeCalculUltimele6Luni.add(bazacalculRepository.findByLunaAndAnAndIdangajat(i, an, idangajat));
			}
		}

		float nrZileLucrate = 0, salariuRealizat = 0;
		for (Bazacalcul bc : bazeCalculUltimele6Luni) {
			nrZileLucrate += bc.getZilelucrate();
			salariuRealizat += bc.getSalariurealizat();
		}

		return salariuRealizat / nrZileLucrate;
	}

	public Bazacalcul saveBazacalcul(RealizariRetineri realizariRetineri) {
		int luna = realizariRetineri.getLuna();
		int an = realizariRetineri.getAn();

		Angajat idangajat = angajatRepository.findPersoanaByIdcontract(realizariRetineri.getIdcontract().getId());

		Bazacalcul bazaCalcul = new Bazacalcul(luna, an, (int) realizariRetineri.getZilelucrate(),
				(int) realizariRetineri.getSalariurealizat(), idangajat);
		return bazacalculRepository.save(bazaCalcul);
	}

	public Bazacalcul updateBazacalcul(RealizariRetineri realizariRetineri) {
		int luna = realizariRetineri.getLuna();
		int an = realizariRetineri.getAn();

		Angajat idangajat = angajatRepository.findPersoanaByIdcontract(realizariRetineri.getIdcontract().getId());

		Bazacalcul oldBazacalcul = bazacalculRepository.findByLunaAndAnAndIdangajat(luna, an,
				idangajat.getIdpersoana());

		Bazacalcul bazaCalcul = new Bazacalcul(luna, an, (int) realizariRetineri.getZilelucrate(),
				(int) realizariRetineri.getSalariurealizat(), idangajat);
		bazaCalcul.setId(oldBazacalcul.getId());
		return bazacalculRepository.save(bazaCalcul);
	}
}
