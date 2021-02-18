package com.cssrc.ibms.listener;

import javax.servlet.ServletContextEvent;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.init.ConfInit;
import com.cssrc.ibms.init.SpringContextInit;
import com.cssrc.ibms.init.impl.Log4jConfInit;

/**
 * Spring启动监听器。<br/>
 * 用于注入servletContext和applicationContext。
 * <pre>
 * 在webxml配置如下：
 * &lt;listener>
 *       &lt;listener-class>com.ibms.core.web.listener.StartupListner&lt;/listener-class>
 *   &lt;/listener>
 *  <pre>
 * @author zhulongchao
 *
 */

public class StartupListner extends ContextLoaderListener
{
    Logger logger = Logger.getLogger(Log4jConfInit.class);
    
    @Override
    public void contextInitialized(ServletContextEvent event)
    {
        
        //log4j 必须保证最先启动
        ConfInit<ServletContextEvent> log4j=new Log4jConfInit();
        log4j.init(event);
        // 通过 CONTEXT_INITIALIZER_CLASSES_PARAM 启动spring
        event.getServletContext().setInitParameter(CONTEXT_INITIALIZER_CLASSES_PARAM,SpringContextInit.class.getName());
        super.contextInitialized(event);
        // 将启动完成的 ServletContext 赋值给一个静态变量
        AppUtil.init(event.getServletContext());
    }
}
