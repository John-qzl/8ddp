package com.cssrc.ibms.core.util.appconf;

import java.util.Properties;

public class AppConfigUtil {

	/**
	 * 从配置文件中读取配置属性
	 * 
	 * @param propertyKey
	 *            属性key
	 * @return
	 */
	public static String get(String propertyKey) {
		Properties properties = (Properties) AppUtil
				.getBean("configproperties");
		return properties.getProperty(propertyKey);
	}

	/**
	 * 从多个配置文件中获取
	 * 
	 * @param propertyKey
	 *            属性key
	 * @return
	 */
	public static String get(String beanName, String propertyKey) {

		Properties properties = (Properties) AppUtil.getBean(beanName);
		return properties.getProperty(propertyKey);

	}

}
