package com.cssrc.ibms.core.web.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 实现http缓存过滤
 *
 * <p>detailed comment</p>
 * @author [创建人]  zhulongchao <br/> 
 * 		   [创建时间] 2015-3-11 上午09:17:01 <br/>  
 * 		   [修改时间] 2015-3-11 上午09:17:01
 * @see
 */
public class ResponseHeaderFilter implements Filter {
	private FilterConfig fc;

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		for (Enumeration e = this.fc.getInitParameterNames(); e
				.hasMoreElements();) {
			String headerName = (String) e.nextElement();
			response.addHeader(headerName, this.fc.getInitParameter(headerName));
		}
		chain.doFilter(req, response);
	}

	public void init(FilterConfig filterConfig) {
		this.fc = filterConfig;
	}

	public void destroy() {
		this.fc = null;
	}
}
