package com.cssrc.ibms.core.rpc;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/** 
* @ClassName: RpcStartUpListner 
* @Description: rpc dubbo服务提供者消费者，注册中心动态注入监听类
* @author zxg 
* @date 2017年5月4日 下午3:07:35 
*  
*/
public class RpcStartUpListner implements ServletContextListener
{

    @Override
    public void contextDestroyed(ServletContextEvent arg0)
    {
        RpcRegistryUtil.destroyed();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0)
    {
        RpcRegistryUtil.init();
        
    }

}
