package net.guides.springboot2.crud.controller;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cookie")
public class CookieController {

	@GetMapping("/add/{key}/{value}")
	public String setCookie(@PathVariable("key") String key, @PathVariable("value") String value,
			HttpServletResponse response) {
		Cookie cookie = new Cookie(key, value);
		cookie.setPath("/cookie/key=" + key);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		return "Cookie adăugat";
	}

	@GetMapping
	public String readAllCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			return Arrays.stream(cookies).map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(", "));
		return "No cookies";
	}

	@GetMapping("/key={key}")
	public Optional<String> readCookies(HttpServletRequest request, @PathVariable("key") String key) {
		return Arrays.stream(request.getCookies()).filter(c -> key.equals(c.getName())).map(Cookie::getValue).findAny();
	}

	@GetMapping("/delete/{key}")
	public String deleteCookie(@PathVariable("key") String key, HttpServletResponse response) {
		Cookie cookie = new Cookie(key, null);
		cookie.setMaxAge(0);
		cookie.setPath("/cookie/key=" + key);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		return "Cookie șters";
	}

}
