package com.cssrc.ibms.init.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.support.XmlWebApplicationContext;

import com.cssrc.ibms.init.ConfInit;

public class SpringXmlInit implements ConfInit<XmlWebApplicationContext>
{

    
    @Override
    public void init(XmlWebApplicationContext applicationContext)
    {
        List<String> confs=new ArrayList<String>();

        SpringConfInit sconf=new SpringConfInit();
        confs.addAll(sconf.getConfs());
        PluginConfInit pconf=new PluginConfInit();
        confs.addAll(pconf.getConfs());
        
        applicationContext.setConfigLocations(confs.toArray(new String[0]));

    }
    
}
