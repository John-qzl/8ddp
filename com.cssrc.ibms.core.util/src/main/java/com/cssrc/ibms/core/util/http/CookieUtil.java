package com.cssrc.ibms.core.util.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

public class CookieUtil {
	public static boolean isExistByName(String name, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		Cookie sCookie = null;
		String sname = null;
		boolean isExist = false;
		if (cookies == null)
			return false;
		for (int i = 0; i < cookies.length; i++) {
			sCookie = cookies[i];
			sname = sCookie.getName();
			if (sname.equals(name)) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}

	public static String getValueByName(String name, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		Cookie sCookie = null;
		String svalue = null;
		String sname = null;

		if (cookies == null)
			return null;
		for (int i = 0; i < cookies.length; i++) {
			sCookie = cookies[i];
			sname = sCookie.getName();
			if (sname.equals(name)) {
				svalue = sCookie.getValue();
				break;
			}
		}
		return svalue;
	}

	public static void delCookie(String name, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, "");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	public static void addCookie(String name, String value, int maxAge,
			HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	public static void addCookie(long expiryTime, String tokenValueBase64,
			HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = new Cookie("SPRING_SECURITY_REMEMBER_ME_COOKIE",
				tokenValueBase64);
		cookie.setMaxAge((int) expiryTime);
		cookie.setPath(StringUtils.hasLength(request.getContextPath()) ? request
						.getContextPath()
						: "/");
		response.addCookie(cookie);
	}
}
