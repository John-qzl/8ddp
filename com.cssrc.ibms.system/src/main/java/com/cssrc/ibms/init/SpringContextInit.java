package com.cssrc.ibms.init;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.web.context.support.XmlWebApplicationContext;
import com.cssrc.ibms.init.impl.RootConfInit;
import com.cssrc.ibms.init.impl.SpringXmlInit;

/**
 * 优先加载properties 文件。
 * @author Administrator
 *
 */
public class SpringContextInit implements ApplicationContextInitializer<XmlWebApplicationContext>
{
    private static List<ConfInit<XmlWebApplicationContext>> initMap = new ArrayList<ConfInit<XmlWebApplicationContext>>();
    static
    {
        initMap.add(new RootConfInit());
        initMap.add(new SpringXmlInit());
    }
    
    @Override
    public void initialize(XmlWebApplicationContext applicationContext)
    {
        
        for (ConfInit<XmlWebApplicationContext> init : initMap)
        {
            init.init(applicationContext);
        }
    }
    
}
