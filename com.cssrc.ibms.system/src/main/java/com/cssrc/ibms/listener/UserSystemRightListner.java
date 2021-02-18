package com.cssrc.ibms.listener;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import com.cssrc.ibms.api.sysuser.intf.IResourcesService;
import com.cssrc.ibms.core.util.appconf.AppUtil;


/** 
* @ClassName: UserSystemRightListner 
* @Description: 判断用户对各个子系统的权限。并且将数据放置到redis
* @author zxg 
* @date 2017年5月2日 下午5:00:22 
*  
*/
public class UserSystemRightListner implements ServletContextListener {
    private static Logger logger = Logger.getLogger(UserSystemRightListner.class);
    @Override
    public void contextDestroyed(ServletContextEvent arg0)
    {
        
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0)
    {
        IResourcesService resourcesService=AppUtil.getBean(IResourcesService.class);
        resourcesService.setUserMenuToRedis();
    }

}
