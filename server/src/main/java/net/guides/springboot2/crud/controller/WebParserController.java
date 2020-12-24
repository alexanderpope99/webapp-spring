package net.guides.springboot2.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;

import net.guides.springboot2.crud.services.WebParserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("webparse")
public class WebParserController {
	@Autowired
	public WebParserService webParserService;

	@GetMapping("/cursbnr")
	public String returnCursValutar() {
		return webParserService.getCursValutarFromBNR();
	}

	@GetMapping("/wikipedia")
	public String returnWikipedia() {
		return webParserService.getWikipediaArticle();
	}
}
