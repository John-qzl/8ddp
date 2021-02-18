package com.cssrc.ibms.core.db.datasource;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.cssrc.ibms.core.util.appconf.AppUtil;
/**
 * 
 * <p>Title:DataSourceUtil</p>
 * @author Yangbo 
 * @date 2016-8-8下午02:44:44
 */
public class DataSourceUtil {
	public static final String GLOBAL_DATASOURCE = "dataSource";
	public static final String DEFAULT_DATASOURCE = "dataSource_Default";
	public static final String TARGET_DATASOURCES = "targetDataSources";

	public static void addDataSource(String key, DataSource dataSource)
			throws IllegalAccessException, NoSuchFieldException {
		DynamicDataSource dynamicDataSource = (DynamicDataSource) AppUtil
				.getBean("dataSource");
		dynamicDataSource.addDataSource(key, dataSource);
	}

	public static void addDataSource(String alias, String driverClassName,
			String url, String username, String password)
			throws IllegalAccessException, NoSuchFieldException {
		BasicDataSource ds = createDataSource(driverClassName, url, username,
				password);
		addDataSource(alias, ds);
	}

	public static void removeDataSource(String key)
			throws IllegalAccessException, NoSuchFieldException {
		DynamicDataSource dynamicDataSource = (DynamicDataSource) AppUtil
				.getBean("dataSource");
		dynamicDataSource.removeDataSource(key);
	}

	public static Map<Object, Object> getDataSources()
			throws IllegalAccessException, NoSuchFieldException {
		DynamicDataSource dynamicDataSource = (DynamicDataSource) AppUtil
				.getBean("dataSource");
		return dynamicDataSource.getDataSource();
	}

	public static BasicDataSource createDataSource(String driverClassName,
			String url, String username, String password) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setTestWhileIdle(true);
		return dataSource;
	}

	public static DataSource getDataSourceByAlias(String alias)
			throws Exception {
		Map map = getDataSources();
		DataSource ds = (DataSource) map.get(alias);
		if (ds == null)
			return (DataSource) map.get("dataSource_Default");
		return ds;
	}
}
