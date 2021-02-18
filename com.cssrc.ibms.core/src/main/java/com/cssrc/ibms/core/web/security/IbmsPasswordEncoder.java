package com.cssrc.ibms.core.web.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.cssrc.ibms.core.util.encrypt.PasswordUtil;

public class IbmsPasswordEncoder implements PasswordEncoder{

	@Override
	public String encodePassword(String rawPass, Object salt)
			throws DataAccessException {
		String inputPassword = PasswordUtil.generatePassword(rawPass);
		return inputPassword;
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt)
			throws DataAccessException {
		String pass1 = "" + encPass;
	    String pass2 = encodePassword(rawPass, salt);

	    return pass1.equals(pass2);
	}

}
