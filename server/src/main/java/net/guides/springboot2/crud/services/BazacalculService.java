package net.guides.springboot2.crud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Bazacalcul;
import net.guides.springboot2.crud.repository.BazacalculRepository;

@Service
public class BazacalculService {
	@Autowired
	private BazacalculRepository bazacalculRepository;

	public List<Bazacalcul> getBazaCalculUltimele6Luni(int luna, int an, long idangajat) {
		int luna6, an6 = an;
		if(luna <= 6) {
			luna6 = 12 - (6 - luna);
			an6--;
		}
		else luna6 = luna - 6;
		boolean areBazacalcul = bazacalculRepository.existsByLunaAndAnAndIdangajat(luna6, an6, idangajat);
		if(!areBazacalcul) return null;

		List<Bazacalcul> rv = new ArrayList<>();

		// daca bazacalcul include anul trecut
		if(luna6 > luna && an6 < an) {
			for(int i = luna6; i <= 12; ++i) {
				rv.add(bazacalculRepository.findByLunaAndAnAndIdangajat(i, an6, idangajat));
			}
			for(int i = 1; i < luna; ++i) {
				rv.add(bazacalculRepository.findByLunaAndAnAndIdangajat(i, an, idangajat));
			}
		} // daca cele 6 luni sunt in anul selectat
		else {
			for(int i = luna6; i <= luna; ++i) {
				rv.add(bazacalculRepository.findByLunaAndAnAndIdangajat(i, an, idangajat));
			}
		}

		return rv;
	}

	public float getMediaZilnicaUltimele6Luni(int luna, int an, long idangajat) {
		int luna6 = 0, an6 = an;
		if(luna <= 6) {
			luna6 = 12 - (6 - luna);
			an6--;
		}
		else luna6 = luna - 6;
		boolean areBazacalcul = bazacalculRepository.existsByLunaAndAnAndIdangajat(luna6, an6, idangajat);
		if(!areBazacalcul) return 0;

		List<Bazacalcul> bazeCalculUltimele6Luni = new ArrayList<>();

		// daca bazacalcul include anul trecut
		if(luna6 > luna && an6 < an) {
			for(int i = luna6; i <= 12; ++i) {
				bazeCalculUltimele6Luni.add(bazacalculRepository.findByLunaAndAnAndIdangajat(i, an6, idangajat));
			}
			for(int i = 1; i < luna; ++i) {
				bazeCalculUltimele6Luni.add(bazacalculRepository.findByLunaAndAnAndIdangajat(i, an, idangajat));
			}
		} // daca cele 6 luni sunt in anul selectat
		else {
			for(int i = luna6; i <= luna; ++i) {
				bazeCalculUltimele6Luni.add(bazacalculRepository.findByLunaAndAnAndIdangajat(i, an, idangajat));
			}
		}

		int nrZileLucrate = 0, salariuRealizat = 0;
		for(Bazacalcul bc : bazeCalculUltimele6Luni) {
			nrZileLucrate += bc.getZilelucrate();
			salariuRealizat += bc.getSalariurealizat();
		}

		return (nrZileLucrate / salariuRealizat);
	}

	public Bazacalcul updateBazacalcul(Bazacalcul newBazaCalcul, int luna, int an, long idangajat) {
		boolean areBC = bazacalculRepository.existsByLunaAndAnAndIdangajat(luna, an, idangajat);
		
		Bazacalcul oldBazacalcul = bazacalculRepository.findByLunaAndAnAndIdangajat(luna, an, idangajat);
		if(areBC) {
			newBazaCalcul.setId(oldBazacalcul.getId());
		}
		return bazacalculRepository.save(newBazaCalcul);
	}

}
