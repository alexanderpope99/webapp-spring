package net.guides.springboot2.crud.controller;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.services.CookieService;

@RestController
@RequestMapping("/cookie")
public class CookieController {

	@Autowired
	private CookieService cookieService;

	@GetMapping("/add/{key}/{value}")
	public String setCookie(@PathVariable("key") String key, @PathVariable("value") String value,
			HttpServletResponse response) {
		response = cookieService.setCookie(key, value, response);
		return "Cookie adăugat";
	}

	@GetMapping("/{key}")
	public Optional<String> readCookies(HttpServletRequest request, @PathVariable("key") String key) {
		return Arrays.stream(request.getCookies()).filter(c -> key.equals(c.getName())).map(Cookie::getValue).findAny();
	}

	@GetMapping("/delete/{key}")
	public String deleteCookie(@PathVariable("key") String key, HttpServletResponse response) {
		response = cookieService.deleteCookie(key, response);
		return "Cookie șters";
	}

}
