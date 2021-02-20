package net.guides.springboot2.crud.services;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

@Service
public class CookieService {

	public HttpServletResponse setCookie(String key, String value, HttpServletResponse response) {
		Cookie cookie = new Cookie(key, value);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		return response;
	}

	public HttpServletResponse deleteCookie(String key, HttpServletResponse response) {
		Cookie cookie = new Cookie(key, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		return response;
	}
}
