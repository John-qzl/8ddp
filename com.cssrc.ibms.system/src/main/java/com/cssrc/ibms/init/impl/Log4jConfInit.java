package com.cssrc.ibms.init.impl;

import java.io.File;
import javax.servlet.ServletContextEvent;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.core.io.support.ResourcePropertySource;
import com.cssrc.ibms.init.ConfInit;
public class Log4jConfInit implements ConfInit<ServletContextEvent>
{
    Logger logger = Logger.getLogger(Log4jConfInit.class);

    @Override
    public void init(ServletContextEvent event)
    {
        try
        {
            ResourcePropertySource propertySource = new ResourcePropertySource("classpath:sys.properties");
            String CONF_ROOT = (String)propertySource.getProperty("conf.root");
            String path = CONF_ROOT + File.separator + "conf" + File.separator + "app-log4j.xml";
            System.out.println("waiting for '"+path+"' init ......");

            DOMConfigurator.configure(path);
            // sce.getServletContext().setInitParameter(Log4jWebConfigurer.CONFIG_LOCATION_PARAM, "file:"+path);
            // 启动服务器的时候加载日志的配置文件
            logger.info(path + " init success");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
}
