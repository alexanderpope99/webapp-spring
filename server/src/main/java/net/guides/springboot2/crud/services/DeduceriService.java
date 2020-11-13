package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.model.Deduceri;
import net.guides.springboot2.crud.repository.DeduceriRepository;

@Service
public class DeduceriService {
	@Autowired
	private DeduceriRepository deduceriRepository;

	public float getDeducereBySalariu(int salariu, int nrNrPersoaneIntretinere) {
		Deduceri deduceri = deduceriRepository.getDeducereBySalariu(salariu);
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
}
