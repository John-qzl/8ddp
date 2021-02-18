/*package com.cssrc.ibms.core.web.conf;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.Log4jConfigListener;

*//**
 * 用于替代web.xml的web容器配置类
 * 服务器启动入口类
 * 
 * @author zxg
 *
 *//*
public class WebAppInitializer implements WebApplicationInitializer {

	private static final String SERVLET_NAME = "action";

	private static final long MAX_FILE_UPLOAD_SIZE = 1024 * 1024 * 5; // 5 Mb

	private static final int FILE_SIZE_THRESHOLD = 1024 * 1024; // After 1Mb

	private static final long MAX_REQUEST_SIZE = -1L; // No request size limit

	*//**
	 * 服务器启动调用此方法，在这里可以做配置 作用与web.xml相同
	 *//*
	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		//Log4jConfigListener
		servletContext.setInitParameter("log4jConfigLocation", "classpath:config/properties/log4j.properties");
		servletContext.addListener(Log4jConfigListener.class);
	}

	*//**
	 * 注册Spring servlet
	 * @param servletContext
	 *//*
	private void addServlet(ServletContext servletContext) {
		// 构建一个application context
		AnnotationConfigWebApplicationContext webContext = createWebContext(
				MvcConfiguration.class, ViewConfiguration.class);
		// 注册spring mvc 的 servlet
		Dynamic dynamic = servletContext.addServlet(SERVLET_NAME,
				new DispatcherServlet(webContext));
		// 添加springMVC 允许访问的Controller后缀
		//dynamic.addMapping("*.do, *.ajax, *.css, *.js, *.gif, *.jpg, *.png");
		dynamic.addMapping("*.do");
		// 全部通过请用 “/”
		//dynamic.addMapping("/");
		dynamic.setLoadOnStartup(1);
		//文件上传最大文件大小
		dynamic.setMultipartConfig(new MultipartConfigElement(null,
				MAX_FILE_UPLOAD_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD));
	}

	*//**
	 * 通过自定义的配置类来实例化一个Web Application Context
	 * @param annotatedClasses
	 * @return
	 *//*
	private AnnotationConfigWebApplicationContext createWebContext(
			Class<?>... annotatedClasses) {
		AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
		webContext.register(annotatedClasses);
		return webContext;
	}

}*/