package com.cssrc.ibms.listener;
     
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cssrc.ibms.core.cache.intf.ICache;
import com.cssrc.ibms.core.db.datasource.DataSourceUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.system.model.SysDataSourceDef;
import com.cssrc.ibms.system.service.SysDataSourceDefService;
      
/**
 * DataSourceInitListener
 * 对配置的外部数据源进行初始化加载，读取表ibms_sys_data_source_def的数据
 * @author liubo
 * @date 2017年5月6日
 */
public class DataSourceListener implements ServletContextListener {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(DataSourceListener.class);
	public void contextDestroyed(ServletContextEvent event) {
		ICache icache = (ICache) AppUtil.getBean(ICache.class);
		icache.clearAll();
	}

	public void contextInitialized(ServletContextEvent event) {
		loadFromDb();
	      
		connectTest();
	}  
      
	private void loadFromDb() {
		SysDataSourceDefService sysDataSourceDefService = 
				(SysDataSourceDefService)AppUtil.getBean("sysDataSourceDefService");
		List<SysDataSourceDef> sysDataSourceDefs = sysDataSourceDefService.getAll();
		for (SysDataSourceDef sysDataSourceDef : sysDataSourceDefs) {
			if ((sysDataSourceDef.getIsEnabled()!=1) || (sysDataSourceDef.getInitContainer()!=1)) 
				continue;
			try {
				DataSourceUtil.addDataSource(sysDataSourceDef.getAlias(), 
						sysDataSourceDefService.getDsFromSysSource(sysDataSourceDef));
            } catch (Exception e) {
            	LOGGER.debug("add datasource " + sysDataSourceDef.getAlias());
            }
		}
	}
      
	private void connectTest() {
		LOGGER.debug("目前容器里的数据源--------------->");
		Map map = null;
		try {
			map = DataSourceUtil.getDataSources();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (map == null) return;
		for (Iterator localIterator = map.keySet().iterator(); localIterator.hasNext(); ) { 
			Object object = localIterator.next();
			boolean isConnect = false;
			try {
				Connection connection = ((DataSource)map.get(object)).getConnection();
				connection.close();
				isConnect = true;
			} catch (Exception localException1) {
				
			}
			LOGGER.debug("alias:" + object + "--className:" + map.get(object).getClass().getName() + "--connectable:" + isConnect);
		}
		LOGGER.debug("<----------------------");
	}
}
