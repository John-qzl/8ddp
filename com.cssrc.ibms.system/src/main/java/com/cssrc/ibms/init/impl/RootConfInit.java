package com.cssrc.ibms.init.impl;

import java.io.IOException;

import org.apache.ibatis.builder.xml.IRootConfigBuilder;
import org.apache.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.init.ConfInit;

public class RootConfInit implements ConfInit<XmlWebApplicationContext>
{
    private static Logger logger = Logger.getLogger(RootConfInit.class);
    
    @Override
    public void init(XmlWebApplicationContext applicationContext)
    {
        ResourcePropertySource propertySource = null;
        try
        {
            // 获取配置文件的路径
            propertySource = new ResourcePropertySource("classpath:sys.properties");
            if (propertySource.getProperty("conf.root") != null)
            {
                SysConfConstant.CONF_ROOT = (String)propertySource.getProperty("conf.root");
                IRootConfigBuilder.CONF_ROOT = SysConfConstant.CONF_ROOT;
            }
            else
            {
                logger.error("file sys.properties key 'conf.root' is not exists");
            }
            if (propertySource.getProperty("ftl.root") != null)
            {
                SysConfConstant.FTL_ROOT = (String)propertySource.getProperty("ftl.root");
            }
            else
            {
                logger.warn("file sys.properties key 'log4j.root' is not exists");
            }
            if (propertySource.getProperty("mvc.view") != null)
            {
                SysConfConstant.MVC_VIEW = (String)propertySource.getProperty("mvc.view");
            }
            else
            {
                logger.warn("file sys.properties key 'mvc.view' is not exists");
            }
        }
        catch (IOException e)
        {
            
            logger.error("sys.properties is not exists");
            
        }
        applicationContext.getEnvironment().getPropertySources().addFirst(propertySource);
    }
    
}
