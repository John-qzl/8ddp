package com.cssrc.ibms.core.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
/**
 * 实现http response编码
 *
 * <p>detailed comment</p>
 * @author [创建人]  zhulongchao <br/> 
 * 		   [创建时间] 2015-3-11 上午09:17:01 <br/>  
 * 		   [修改时间] 2015-3-11 上午09:17:01
 * @see
 */
public class ResponseEncodeFilter implements Filter{

	private String encoding = "UTF-8";
	private String contentType = "text/html;charset=UTF-8";

	@Override
	public void destroy()
	{

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse httpresponse,
			FilterChain chain) throws IOException, ServletException
	{
		HttpServletResponse response=(HttpServletResponse)httpresponse; 
		response.setCharacterEncoding(encoding);
		
		response.setContentType(contentType);
		
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException
	{
		String _encoding = config.getInitParameter("encoding");
		String _contentType = config.getInitParameter("contentType");
		// String ext=config.getInitParameter("ext");
		if (_encoding != null)
			encoding = _encoding;
		if (_contentType != null)
			contentType = _contentType;

	}

}
