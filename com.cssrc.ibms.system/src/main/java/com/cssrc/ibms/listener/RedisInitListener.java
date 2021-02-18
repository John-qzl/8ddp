package com.cssrc.ibms.listener;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.IResourcesService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.intf.IUserCustomService;
import com.cssrc.ibms.core.util.appconf.AppUtil;


/**
 * Description:redis数据初始化
 * <p>UserCustomListner.java</p>
 * @author dengwenjie 
 * @date 2017年6月12日
 */
public class RedisInitListener implements ServletContextListener {
    private static Logger logger = Logger.getLogger(RedisInitListener.class);
    @Override
    public void contextDestroyed(ServletContextEvent arg0)
    {
        
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0)
    {
        logger.debug("1.---------[contextInitialized]加载用户个性化设置");
        IUserCustomService userCustomService = AppUtil.getBean(IUserCustomService.class);
        userCustomService.setUserCustomToRedis();
        
        logger.debug("2.---------[contextInitialized]加载用户 各个系统菜单权限。");       
        IResourcesService resourcesService=AppUtil.getBean(IResourcesService.class);
        resourcesService.setUserMenuToRedis();
        
        logger.debug("3.---------[contextInitialized]加载所有用户信息。");       
        ISysUserService sysUserService=AppUtil.getBean(ISysUserService.class);
        sysUserService.setAllSysUserToRedis();
        
        logger.debug("3.---------[contextInitialized]加载所有组织信息。");       
        ISysOrgService sysOrgService=AppUtil.getBean(ISysOrgService.class);
        sysOrgService.setAllSysOrgToRedis();
        
        logger.debug("3.---------[contextInitialized]加载所有岗位信息。");       
        IPositionService positionService=AppUtil.getBean(IPositionService.class);
        positionService.setAllPositionToRedis();
        
        logger.debug("3.---------[contextInitialized]加载所有角色信息。");       
        ISysRoleService sysRoleService=AppUtil.getBean(ISysRoleService.class);
        sysRoleService.setAllSysRoleToRedis();
        
    }

}
