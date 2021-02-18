package com.cssrc.ibms.core.web.filter;
 

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.cssrc.ibms.api.core.util.ContextUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;

/**
 * 用于拦截请求以获取HttpSevletRequest对象，以供后续Aop类使用,以获取当前用户请求的IP等信息。
 *  用于相同线程间共享Request对象 。
 * 
 * @author zhulongchao
 * 
 */
public class AopFilter implements Filter
{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException{
		try{ 
			UserContextUtil.clearAll();
			RequestUtil.setHttpServletRequest((HttpServletRequest) request);
			RequestUtil.setHttpServletResponse((HttpServletResponse)response);
			
			SessionLocaleResolver sessionResolver=(SessionLocaleResolver)AppUtil.getBean(SessionLocaleResolver.class);
			Locale local= sessionResolver.resolveLocale((HttpServletRequest) request);
			ContextUtil.setLocale(local);
			
			chain.doFilter(request, response);
		}
		finally{
			UserContextUtil.clearAll();
		}
	}
	

	@Override
	public void destroy()
	{
	}

}

