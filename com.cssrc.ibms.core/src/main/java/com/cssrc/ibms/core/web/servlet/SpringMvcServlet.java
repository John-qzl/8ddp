package com.cssrc.ibms.core.web.servlet;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.util.string.StringUtil;


/**
 * 重写Spring Mvc Servlet，处理输入URL没有requestMapping处理的情况。<br>
 * <pre>
 * 让它直接跳至其url对应的jsp。
 * 跳转规则分为两种：
 * 1.输入地址符合下列规则的情况的处理方法。
 *  /{1}/{2}/{3}/{4}.do
 *  对应的jsp为 /{1}/{2}/{3}{4}.jsp ,注意这里需要将{4}首字母修改为大写。
 * 	 如/oa/system/appRole/add.do 其对应的jsp路径则为:
 *   /oa/system/appRoleAdd.jsp，
 *   /oa/system/appRoleGrant.do,其对应的jsp路径则为
 *   /oa/system/appRoleGrant.jsp
 * 2.输入的地址不符合上面的规则，那么就把do直接换成jsp。
 *   例如：
 *   /oa/system.do -->/oa/system.jsp
 *   /oa.do -->/oa.jsp
 *   这些jsp均放在/WEB-INF/view目录下
 *   
 *   在web.xml配置如下：
 *   &lt;servlet>
 *       &lt;servlet-name>action&lt;/servlet-name>
 *        &lt;servlet-class>com.ibms.core.web.servlet.SpringMvcServlet&lt;/servlet-class>
 *       &lt;init-param>
 *			&lt;param-name>contextConfigLocation&lt;/param-name>
 *			&lt;param-value>classpath:conf/app-action.xml&lt;/param-value>
 *		&lt;/init-param>
 *       &lt;load-on-startup>2&lt;/load-on-startup>
 *   &lt;/servlet>
 *  </pre>
 *   
 * @author zhulongchao
 */
public class SpringMvcServlet extends DispatcherServlet
{
    

    @Override
    public String getContextConfigLocation() {
        return "file:"+SysConfConstant.CONF_ROOT+"/conf/app-action.xml";
    }
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		String requestURI=request.getRequestURI();
		logger.debug("not foud handle mapping for url: " + requestURI);
		//处理RequestURI
		String contextPath=request.getContextPath();
		
		requestURI=requestURI.replace(".do", "");
		int cxtIndex=requestURI.indexOf(contextPath);
		if(cxtIndex!=-1)
		{
			requestURI=requestURI.substring(cxtIndex+contextPath.length());
		}
		String[]paths=requestURI.split("[/]");
		String jspPath=null;
		if(paths!=null && paths.length==5){
			jspPath="/"+paths[1] + "/" + paths[2] + "/" +paths[3] +  StringUtil.makeFirstLetterUpperCase(paths[4]) + ".jsp";
		}else{
			jspPath=requestURI + ".jsp";
		}
		logger.debug("requestURI:" + request.getRequestURI() + " and forward to /WEB-INF/view"+jspPath );
		try{
			if(requestURI.indexOf("/js/")>=0){//ext路径特殊处理
				request.setAttribute("requestURI", requestURI);
				request.getRequestDispatcher("/oa/console/seaJSShow.do").forward(request, response);
			}else{
				request.getRequestDispatcher("/WEB-INF/view"+jspPath).forward(request, response);
			}
		}catch(Exception e){
			System.out.println(e.getCause());
			logger.debug("Error:" + request.getRequestURI() + " and forward to /WEB-INF/view"+jspPath +": "+e.getCause());
			}
		
		//super.noHandlerFound(request, response);
	}
}
