package com.cssrc.ibms.core.util.common;


import java.util.Locale;

import org.springframework.context.MessageSource;

import com.cssrc.ibms.core.util.appconf.AppUtil;


/**
 * 消息操作类
 * @author zhulongchao
 *
 */
public   class ResourceUtil {
	
	
	  /**
     * 取得资源键值 
     * @param msgKey
     * @param arg
     * @param locale
     * @return
     */
    public static String getText(String msgKey,Object arg,Locale local) {
    	MessageSource messageSource=(MessageSource)AppUtil.getBean(MessageSource.class);
    	return messageSource.getMessage(msgKey,new Object[]{ arg}, local);
    }
    
    /**
     * 取得资源键值 
     * @param msgKey
     * @param args
     * @param locale
     * @return
     */
    public static String getText(String msgKey,Object[] args,Locale local) {
    	MessageSource messageSource=(MessageSource)AppUtil.getBean(MessageSource.class);
    	return messageSource.getMessage(msgKey,args, local);
    }
	
    
   
}

