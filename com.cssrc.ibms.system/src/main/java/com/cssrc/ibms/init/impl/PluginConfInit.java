package com.cssrc.ibms.init.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.util.string.StringUtil;

public class PluginConfInit
{
    /*
     * E:/workspace/ibms/com.cssrc.ibms/ibms.conf/conf/app-security-cas.xml
    E:/workspace/ibms/com.cssrc.ibms/ibms.conf/conf/app-mdmjms.xml
    E:/workspace/ibms/com.cssrc.ibms/ibms.conf/interfaces/app-rpc.xml
    E:/workspace/ibms/com.cssrc.ibms/ibms.conf/conf/app-jms.xml
    E:/workspace/ibms/com.cssrc.ibms/ibms.conf/conf/app-sync.xml
    
    */
    private static Logger logger = Logger.getLogger(PluginConfInit.class);
    
    private static String plugin_pre = "plugin.";
    
    private static String plugin_use = ".use";
    
    private static String default_relativepath = "/conf/";
    
    private List<String> confs = new ArrayList<String>();
    
    public PluginConfInit()
    {
        Properties properties = new Properties();
        try
        {
            String pluginPath = SysConfConstant.CONF_ROOT + "/properties/plugin.properties";
            properties.load(new FileInputStream(new File(pluginPath)));
            Enumeration<Object> plugins = properties.keys();
            while (plugins.hasMoreElements())
            {
                String key = plugins.nextElement().toString();
                Object val = properties.get(key);
                if (val != null && key.startsWith(plugin_pre) && key.endsWith(plugin_use))
                {
                    String pluginname = key.split("\\.")[1];
                    String[] vals = val.toString().split(",");
                    String use = "0";
                    String relativepath = null;
                    String useconf = null;
                    String nouseconf = null;
                    
                    for (int i = 0; i < vals.length; i++)
                    {
                        switch (i)
                        {
                            case 0:
                                use = vals[0];
                                break;
                            case 1:
                                relativepath = vals[1];
                                break;
                            case 2:
                                useconf = vals[2];
                                break;
                            case 3:
                                nouseconf = vals[3];
                                break;
                        }
                    }
                    
                    if ("1".equals(use.trim()))
                    {
                        addConf(pluginname, relativepath, useconf);
                    }
                    else
                    {
                        if(StringUtil.isNotEmpty(nouseconf)) {
                            addConf(pluginname, relativepath, nouseconf);
                        }
                    }
                    
                }
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    private void addConf(String pluginname, String relativepath, String conf)
    {
       
        if (StringUtil.isNotEmpty(conf))
        {
            String[] confs = conf.split("\\|");
            for (String c : confs)
            {
                String confpath = getPrePath(relativepath);
                confpath += c;
                addFile(confpath);
            }
            
        }
        else
        {
            String confpath = getPrePath(relativepath);
            confpath += "app-" + pluginname + ".xml";
            addFile(confpath);
        }
    }

    private String getPrePath(String relativepath)
    {
        String confpath = SysConfConstant.CONF_ROOT;
        if (StringUtil.isNotEmpty(relativepath))
        {
            confpath += relativepath;
        }
        else
        {
            confpath += default_relativepath;
        }
        return confpath;
    }
    
    private void addFile(String confpath)
    {
        File file = new File(confpath);
        if (file.exists()&&file.isFile())
        {
            logger.info("add plugin [" + confpath + "]......");
            confs.add("file:"+confpath);
        }
        else
        {
            logger.warn("can not find plugin [" + confpath + "]......");
        }
    }
    
    public List<String> getConfs()
    {
        return confs;
    }
    
    public void setConfs(List<String> confs)
    {
        this.confs = confs;
    }
    
}
