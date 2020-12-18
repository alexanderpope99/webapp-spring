package net.guides.springboot2.crud.services;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CursValutarService {

	public String getCursValutarFromBNR() {
		Document doc;
		try {
			doc = Jsoup.connect("https://www.cursbnr.ro/").get();
		} catch (IOException e) {
			return "Eroare la conexiunea la site-ul BNR";
		}
		return doc.select("#from-currency").select("[selected]").attr("value");
	}

	public String getWikipediaArticle() {
		Document doc;
		try {
			doc = Jsoup.connect("https://en.wikipedia.org/").get();
		} catch (IOException e) {
			return "Eroare la conexiunea la site";
		}
		Elements newsHeadlines = doc.select("#mp-itn b a");
		String result = "";
		for (Element headline : newsHeadlines) {
			result += (headline.attr("title") + "\n" + headline.absUrl("href"));
		}
		return result;
	}
}
