package com.cssrc.ibms.core.web.security;
 

import java.util.Collection;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.constant.sysuser.SystemConst;

/**
 * 系统访问权限决策器，系统有权限则直接返回，没有权限则抛出AccessDeniedException错误。
 * 
 * <pre>
 * 系统根据当前页面的权限判断用户是否能够访问系统页面。
 * 		1.首先判断是否匿名访问，如果配置了匿名访问则直接通过。
 * 		2.判断用户是否已经登录，如果没有登录则返回没有权限页面。
 * 		3.如果是超级管理员直接通过。
 * 		4.页面的角色包括公开访问的角色直接通过。
 * 		5.判断是否有权限访问系统。
 * 		6.判断页面是否有角色授权，如果当前用户有访问页面的角色则直接通过。
 * 		7.没有满足的条件，则抛出错误。
 * </pre>
 * 
 * @author zhulongchao
 * 
 */
public class IbmsDecisionManager implements AccessDecisionManager {

	public Logger logger = LoggerFactory.getLogger(IbmsDecisionManager.class);

	// 是否初始化
	private static boolean isInit = false;
	// 是否验证通过
	private static int isValid = -3;

	@Resource
	private Properties configproperties;
 

 
 
	 

	@SuppressWarnings({ "unused", "unchecked" })
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		// 匿名访问
		if (configAttributes.contains(SystemConst.ROLE_CONFIG_ANONYMOUS)) {
			return;
		}
		// 登陆访问
		if (authentication == null) {
			throw new AccessDeniedException("没有登录系统");
		}
		Object principal = authentication.getPrincipal();
		if (principal == null) {
			throw new AccessDeniedException("登录对象为空");
		}

		if (!(principal instanceof ISysUser)) {
			throw new AccessDeniedException("登录对象必须为IUser实现类");
		}
		
		ISysUser user = (ISysUser) principal;
		// 获取当前用户的角色。
		Collection<GrantedAuthority> roles = user.getAuthorities();
	 
		if (configAttributes.contains(SystemConst.ROLE_CONFIG_PUBLIC)) {
			return;
		}
		
		// 授权访问
		// 遍历当前用户的角色，如果当前页面对应的角色包含当前用户的某个角色则有权限访问。
		for (GrantedAuthority hadRole : roles) {
			if (configAttributes.contains(new SecurityConfig(hadRole.getAuthority()))) {
				return;
			}
		}
		throw new AccessDeniedException("对不起,你没有访问该页面的权限!");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {

		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {

		return true;
	}

}

