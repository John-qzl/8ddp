package com.cssrc.ibms.core.web.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 压缩JS的过滤器
 *
 * <p>detailed comment</p>
 * @author [创建人]  zhulongchao <br/> 
 * 		   [创建时间] 2015-3-11 上午09:25:08 <br/>  
 * 		   [修改时间] 2015-3-11 上午09:25:08
 * @see
 */
public class GzipJsFilter implements Filter {
	Map<String, String> headers = new HashMap<String, String>();

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		if ((req instanceof HttpServletRequest))
			doFilter((HttpServletRequest) req, (HttpServletResponse) res, chain);
		else
			chain.doFilter(req, res);
	}

	public void doFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		for (Iterator<Map.Entry<String,String>> it = this.headers.entrySet().iterator(); 
		     it.hasNext();) {
			Map.Entry<String,String> entry = it.next();
			response.addHeader(entry.getKey(),entry.getValue());
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
		String headersStr = config.getInitParameter("headers");
		String[] headers = headersStr.split(",");
		for (int i = 0; i < headers.length; i++) {
			String[] temp = headers[i].split("=");
			this.headers.put(temp[0].trim(), temp[1].trim());
		}
	}
}
