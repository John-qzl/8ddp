package com.cssrc.ibms.core.db.datasource;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/*
 * 
 * <p>Title:DynamicDataSource</p>
 * @author Yangbo 
 * @date 2016-8-8下午02:43:45
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	
	protected Object determineCurrentLookupKey() {
		return DbContextHolder.getDataSource();
	}

	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		super.setTargetDataSources(targetDataSources);
		super.afterPropertiesSet();
	}

	private static Object getValue(Object instance, String fieldName)
			throws IllegalAccessException, NoSuchFieldException {
		Field field = AbstractRoutingDataSource.class
				.getDeclaredField(fieldName);

		field.setAccessible(true);
		return field.get(instance);
	}

	public void addDataSource(String key, Object dataSource)
			throws IllegalAccessException, NoSuchFieldException {
		Map<Object,Object> targetDataSources = (Map<Object,Object>) getValue(this, "targetDataSources");
		boolean rtn = isDataSourceExist(key);
		logger.info("curent ds:"+DbContextHolder.getDataSource());
		if (rtn) {
			this.logger.info("datasource name :" + key + " 数据源成功更新");
		} else{
			this.logger.info("datasource name :" + key + " 成功加入数据源");
		}
		targetDataSources.put(key, dataSource);
		setTargetDataSources(targetDataSources);
	}

	public boolean isDataSourceExist(String key) throws IllegalAccessException,
			NoSuchFieldException {
		Map targetDataSources = (Map) getValue(this, "targetDataSources");

		return targetDataSources.containsKey(key);
	}

	public void removeDataSource(String key) throws IllegalAccessException,
			NoSuchFieldException {
		Map targetDataSources = (Map) getValue(this, "targetDataSources");

		if ((key.equals("dataSource")) || (key.equals("dataSource_Default"))) {
			throw new DataSourceException("datasource name :" + key
					+ " can't be removed!");
		}
		targetDataSources.remove(key);
		setTargetDataSources(targetDataSources);
	}

	public Map<Object, Object> getDataSource() throws IllegalAccessException,
			NoSuchFieldException {
		Map targetDataSources = (Map) getValue(this, "targetDataSources");
		return targetDataSources;
	}

	public Logger getParentLogger() {
		return null;
	}
}
