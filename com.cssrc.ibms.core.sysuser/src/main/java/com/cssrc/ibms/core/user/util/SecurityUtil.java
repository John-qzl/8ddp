package com.cssrc.ibms.core.user.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.cssrc.ibms.core.util.appconf.AppUtil;
/**
 * 账号登陆安全授权
 * 2016-7-19
 * @author Yangbo
 *
 */
public class SecurityUtil {
	protected static Logger logger = LoggerFactory
			.getLogger(SecurityUtil.class);
	
	public static Authentication login(HttpServletRequest request,
			String userName, String pwd, boolean isIgnorePwd) {
		AuthenticationManager authenticationManager = (AuthenticationManager) AppUtil
				.getBean("authenticationManager");
		CustomPwdEncoder.setIngore(isIgnorePwd);
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				userName, pwd);
		authRequest.setDetails(new WebAuthenticationDetails(request));
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = authenticationManager.authenticate(authRequest);
		securityContext.setAuthentication(auth);
		return auth;
	}

	public class FunctionRights {
		private boolean hasFunction = false;

		private Collection<ConfigAttribute> roles = new ArrayList();

		public FunctionRights(boolean hasFunction,
				Collection<ConfigAttribute> roles) {
			this.hasFunction = hasFunction;
			this.roles = roles;
		}

		public boolean isHasFunction() {
			return this.hasFunction;
		}

		public void setHasFunction(boolean hasFunction) {
			this.hasFunction = hasFunction;
		}

		public Collection<ConfigAttribute> getRoles() {
			return this.roles;
		}

		public void setRoles(Collection<ConfigAttribute> roles) {
			this.roles = roles;
		}
	}
}
