package com.cssrc.ibms.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cssrc.ibms.api.system.intf.ISystemInitService;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.core.cache.intf.ICache;
import com.cssrc.ibms.core.db.util.SyncDBUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.system.service.SysFileService;
public class ServerListener implements ServletContextListener {
	
	private Log logger = LogFactory.getLog(ServerListener.class);

	public void contextDestroyed(ServletContextEvent event) {
		ICache icache = (ICache) AppUtil.getBean(ICache.class);
		icache.clearAll();
	}

	public void contextInitialized(ServletContextEvent event) {
		
		try {
			this.logger.debug("---------[contextInitialized]开始同步数据库更新脚本。");
			SyncDBUtil syncDBUtil=(SyncDBUtil)AppUtil.getBean("syncDBUtil");
			syncDBUtil.syncDb();
			this.logger.debug("---------[contextInitialized]同步数据库更新脚本成功。");
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.logger.debug("---------[contextInitialized]开始初始化表单模版。");
		ISystemInitService systemInitService= (ISystemInitService) AppUtil.getBean(ISystemInitService.class);
		systemInitService.initDataTemplate();
		this.logger.debug("--------[contextInitialized]初始化表单模版成功。");

		//初始化时将文件密级加入静态变量
		this.logger.debug("---------[contextInitialized]开始将文件密级加入静态变量。");
		SysFileService sysFileService = 
				(SysFileService)AppUtil.getBean("sysFileService");
		sysFileService.addFileSecurity();
		this.logger.debug("---------[contextInitialized]将文件密级加入静态变量成功，值为："+ISysFile.SECURITY_CHINESE_MAP+"。");
		
		this.logger.debug("[contextInitialized]开始初始化首页栏目。");
		systemInitService.initIndex();
		this.logger.debug("[contextInitialized]初始化首页栏目成功。");
	}
}
