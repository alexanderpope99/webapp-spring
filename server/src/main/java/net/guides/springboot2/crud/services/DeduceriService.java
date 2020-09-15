package net.guides.springboot2.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.crud.model.Deduceri;
import net.guides.springboot2.crud.repository.DeduceriRepository;

@Service
public class DeduceriService {
  @Autowired
  private DeduceriRepository deduceriRepository;

  public float getDeducereBySalariu(float salariu, String strNrPersoaneIntretinere) {
    Deduceri deduceri = deduceriRepository.getDeducereBySalariu(salariu);
    switch (strNrPersoaneIntretinere) {
      case "zero":
        return deduceri.getZero();
      case "una":
        return deduceri.getUna();
      case "doua":
        return deduceri.getDoua();
      case "trei":
        return deduceri.getDoua();
      case "patru":
        return deduceri.getPatru();
      default:
        return deduceri.getPatru();
    }
  }
  
}
