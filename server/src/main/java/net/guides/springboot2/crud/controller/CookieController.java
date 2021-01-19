package net.guides.springboot2.crud.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.CookieValue;

@RestController
@RequestMapping("/cookie")
public class CookieController {

	@GetMapping("/set/{key}/{value}")
	public String setCookie(@PathVariable("key") String key, @PathVariable("value") String value,
			HttpServletResponse response) {
		Cookie cookie = new Cookie(key, value);
		response.addCookie(cookie);
		return "Cookie adăugat";
	}

	@GetMapping
	public String readCookie(@CookieValue(value = "username", defaultValue = "Atta") String username) {
		return "Hey! My username is " + username;
	}

	@GetMapping("/all")
	public String readAllCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			return Arrays.stream(cookies).map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(", "));
		return "No cookies";
	}

	@GetMapping("/delete/{name}")
	public String deleteCookie(@PathVariable("name") String name, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		return "Gata";
	}

}
