package net.guides.springboot2.crud.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Fisier;
import net.guides.springboot2.crud.repository.FisierRepository;

@Service
public class FisierService {

  @Autowired
  private FisierRepository fisierRepository;

  public Fisier findById(int id) throws ResourceNotFoundException {
    return fisierRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Nu există fișier cu id: "+id));
  }

  public Fisier store(MultipartFile file) throws IOException {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    Fisier fisier = new Fisier(fileName, file.getContentType(), file.getBytes());

    return fisierRepository.saveAndFlush(fisier);
  }

	public byte[] resizeImage(MultipartFile file) {
		InputStream in = new ByteArrayInputStream(file.getBytes());
  	BufferedImage newImage = ImageIO.read(in);
		return (newImage.getScaledInstance(397, 95, java.awt.Image.SCALE_SMOOTH));
	}

  public Fisier getFile(int id) {
    return fisierRepository.findById(id).get();
  }
  
  public Stream<Fisier> getAllFiles() {
    return fisierRepository.findAll().stream();
  }
}
