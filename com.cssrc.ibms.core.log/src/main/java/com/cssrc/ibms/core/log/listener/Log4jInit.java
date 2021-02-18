package com.cssrc.ibms.core.log.listener;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.core.io.support.ResourcePropertySource;

public class Log4jInit implements ServletContextListener{
	
	Logger log = Logger.getLogger(Log4jInit.class);
	
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("Log4jInit contextDestroyed!");
	}

	public void contextInitialized(ServletContextEvent sce) {
		try {
			ResourcePropertySource propertySource = new ResourcePropertySource(
					"classpath:sys.properties");
			String CONF_ROOT=(String) propertySource.getProperty("conf.root");
			String path=CONF_ROOT+File.separator+"conf"+File.separator+"app-log4j.xml";
			DOMConfigurator.configure(path);
	        //sce.getServletContext().setInitParameter(Log4jWebConfigurer.CONFIG_LOCATION_PARAM, "file:"+path);
			//启动服务器的时候加载日志的配置文件
			log.info(path+" init success");
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	
}