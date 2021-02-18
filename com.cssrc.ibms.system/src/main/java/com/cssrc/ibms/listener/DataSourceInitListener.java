package com.cssrc.ibms.listener;
     
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import com.cssrc.ibms.core.db.datasource.DynamicDataSource;
      
/**
 * DataSourceInitListener
 * 通过继承ApplicationListener实现对xml文件中配置的数据源进行初始化加载
 * @author liubo
 * @date 2017年4月14日
 */
public class DataSourceInitListener implements ApplicationListener<ContextRefreshedEvent> {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(DataSourceInitListener.class);
      
	public void onApplicationEvent(ContextRefreshedEvent ev) {
		if (ev.getApplicationContext().getParent() == null) {
			ApplicationContext context = ev.getApplicationContext();
      
			loadFromXml(context);
		}
	}
      
	private void loadFromXml(ApplicationContext context) {
		DynamicDataSource dynamicDataSource = (DynamicDataSource)context.getBean("dataSource");
      
		Map mapDataSource = context.getBeansOfType(DataSource.class);
      
		Set dsSet = mapDataSource.entrySet();
		for (Iterator it = dsSet.iterator(); it.hasNext(); ) {
			Map.Entry ent = (Map.Entry)it.next();
			String key = (String)ent.getKey();
			if ((key.equals("dataSource")) || (key.equals("dataSource_Default")))
				continue;
			try {
				dynamicDataSource.addDataSource(key, ent.getValue());
            } catch (Exception e) {
            	LOGGER.debug(e.getMessage());
            	e.printStackTrace();
            }
			LOGGER.debug("add datasource " + (String)ent.getKey());
		}
	}
}
