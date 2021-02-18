package com.cssrc.ibms.core.web.security; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 多用户入口实现
 * by zhulongchao
 */
public class MultipleAuthenticationLoginEntry implements
		AuthenticationEntryPoint {

    private String defaultLoginUrl="/login.jsp";  
    private List<DirectUrlResolver> directUrlResolvers = new ArrayList<DirectUrlResolver>();  
  

  
    public void setDefaultLoginUrl(String defaultLoginUrl) {  
    	
        this.defaultLoginUrl = defaultLoginUrl;  
    }  
  
    public void setDirectUrlResolvers(List<DirectUrlResolver> directUrlResolvers) {  
        this.directUrlResolvers = directUrlResolvers;  
    }
    
    @Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException) 
	    throws IOException, ServletException {
		String ctxPath = request.getContextPath();
    	for (DirectUrlResolver directUrlResolver : directUrlResolvers) {  
            if (directUrlResolver.support(request)) {  
                String loginUrl = directUrlResolver.directUrl();  
                response.sendRedirect(ctxPath+ loginUrl);  
                return;  
            }  
        }  
        response.sendRedirect(ctxPath+defaultLoginUrl);  
		
	}  

}

