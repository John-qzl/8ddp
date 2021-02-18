package com.cssrc.ibms.init.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cssrc.ibms.core.constant.system.SysConfConstant;

public class SpringConfInit
{
    private static Logger logger = Logger.getLogger(SpringConfInit.class);
    
    private List<String> confs = new ArrayList<String>();
    
    public SpringConfInit()
    {
        // 属性配置文件
        logger.info("add conf ["+SysConfConstant.CONF_ROOT+"/conf/app-properties.xml"+"]...");
        confs.add("file:${conf.root}/conf/app-properties.xml");
        // resources 数据库等
        logger.info("add conf ["+SysConfConstant.CONF_ROOT+"/conf/app-resources.xml"+"]...");
        confs.add("file:${conf.root}/conf/app-resources.xml");
        // 需要手动注入的bean
        logger.info("add conf ["+SysConfConstant.CONF_ROOT+"/conf/app-beans.xml"+"]...");
        confs.add("file:${conf.root}/conf/app-beans.xml");
        // activiti 引擎
        logger.info("add conf ["+SysConfConstant.CONF_ROOT+"/conf/app-activiti.xml"+"]...");
        confs.add("file:${conf.root}/conf/app-activiti.xml");
        // quartz job引擎
        logger.info("add conf ["+SysConfConstant.CONF_ROOT+"/conf/app-quartz.xml"+"]...");
        confs.add("file:${conf.root}/conf/app-quartz.xml");
        
        logger.info("add ext conf ["+"classpath:com/ibms/*/conf/app-*.xml"+"]...");
        confs.add("classpath:com/ibms/*/conf/app-*.xml");
        logger.info("add ext conf ["+"classpath*:/com/ibms/*/conf/app-*.xml"+"]...");
        confs.add("classpath*:/com/ibms/*/conf/app-*.xml");
        
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
