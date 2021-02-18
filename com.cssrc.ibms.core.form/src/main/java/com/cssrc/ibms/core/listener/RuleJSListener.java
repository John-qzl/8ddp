package com.cssrc.ibms.core.listener;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.cssrc.ibms.api.form.intf.IFormRuleService;
import com.cssrc.ibms.core.util.appconf.AppUtil;

import freemarker.template.TemplateException;

public class RuleJSListener  implements ServletContextListener {
    private static Logger logger = Logger.getLogger(RuleJSListener.class);
    @Override
    public void contextDestroyed(ServletContextEvent arg0)
    {
        
    }
    @Override
    public void contextInitialized(ServletContextEvent arg0)
    {
    	try{
    		logger.debug("-----------根据数据库中的验证规则生成rule.js文件");
            IFormRuleService formRuleService = AppUtil.getBean(IFormRuleService.class);
            formRuleService.generateJS();
    	}catch(IOException ioe){
    		System.err.println("rule.js文件操作失败！");   
    		ioe.printStackTrace();
    	}catch(TemplateException te){
    		System.err.println("FreeMark引擎解析rulejs.ftl异常！");   
    		te.printStackTrace();
    	}
        
    }
}
