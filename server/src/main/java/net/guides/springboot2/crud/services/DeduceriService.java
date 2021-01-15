package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Deduceri;
import net.guides.springboot2.crud.repository.DeduceriRepository;

@Service
public class DeduceriService {
	@Autowired
	private DeduceriRepository deduceriRepository;

	public float getDeducereBySalariu(int salariu, int nrNrPersoaneIntretinere) throws ResourceNotFoundException {
		Deduceri deduceri = deduceriRepository.getDeducereBySalariu(salariu);
		if (deduceri == null)
			throw new ResourceNotFoundException("Nu există deduceri pentru salariul " + salariu);
		switch (nrNrPersoaneIntretinere) {
			case 0:
				return deduceri.getZero();
			case 1:
				return deduceri.getUna();
			case 2:
				return deduceri.getDoua();
			case 3:
				return deduceri.getDoua();
			case 4:
				return deduceri.getPatru();
			default:
				return deduceri.getPatru();
		}
	}

	public void init() {
		// daca deja exista nu face nimic
		if (deduceriRepository.count() > 0)
			return;

		Deduceri deducere = new Deduceri(1, 1950, 510, 670, 830, 990, 1310);
		deduceriRepository.save(deducere);
		int zero = 495, una = 655, doua = 815, trei = 975, patru = 1295;
		for (int i = 1951; i <= 3600; i += 50) {
			deducere = new Deduceri(i, i + 49, zero, una, doua, trei, patru);
			deduceriRepository.save(deducere);
			zero -= 15;
			una -= 15;
			doua -= 15;
			trei -= 15;
			patru -= 15;
		}
	}
}
