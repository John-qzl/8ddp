package com.cssrc.ibms.core.user.util;

import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPwdEncoder implements PasswordEncoder {
	private static ThreadLocal<Boolean> ingorePwd = new ThreadLocal();

	public static void setIngore(boolean ingore) {
		ingorePwd.set(Boolean.valueOf(ingore));
	}

	public String encode(CharSequence rawPassword) {
		String pwd = rawPassword.toString();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(pwd.getBytes("UTF-8"));
			return new String(Base64.encodeBase64(digest));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if ((ingorePwd.get() == null)
				|| (!((Boolean) ingorePwd.get()).booleanValue())) {
			String enc = encode(rawPassword);
			return enc.equals(encodedPassword);
		}
		return true;
	}
}
