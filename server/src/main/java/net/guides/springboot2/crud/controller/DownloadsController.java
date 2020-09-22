package net.guides.springboot2.crud.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/download")
public class DownloadsController {

	@GetMapping("/{filename}")
	public void getFile(
		@PathVariable("filename") String fileName,
		HttpServletResponse response)
	{
		try{
			File myFile = new File("src/main/java/net/guides/springboot2/crud/downloads/"+fileName);
			InputStream fileAsIS = new FileInputStream(myFile);

			// response.setContentType("application/pdf");
			IOUtils.copy(fileAsIS, response.getOutputStream());

			response.getOutputStream().flush();

		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}
}
