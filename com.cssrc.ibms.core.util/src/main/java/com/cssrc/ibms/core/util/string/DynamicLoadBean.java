package com.cssrc.ibms.core.util.string;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 
 * This class used to dynamic loan bean to spring context during runtime
 * 
 *
 */
public class DynamicLoadBean implements ApplicationContextAware {

	private ConfigurableApplicationContext applicationContext = null;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

	public ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 向spring的beanFactory动态地装载bean
	 * 
	 * @param configLocationString
	 *            要装载的bean所在的xml配置文件位置。 spring配置中的contextConfigLocation，同样支持诸如
	 *            "/WEB-INF/ApplicationContext-*.xml"的写法。
	 */
	public void loadBean(String configLocationString) {
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(
				(BeanDefinitionRegistry) getApplicationContext()
						.getBeanFactory());
		beanDefinitionReader.setResourceLoader(getApplicationContext());
		beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(
				getApplicationContext()));
		try {
			String[] configLocations = new String[] { configLocationString };
			for (int i = 0; i < configLocations.length; i++)
				beanDefinitionReader
						.loadBeanDefinitions(getApplicationContext()
								.getResources(configLocations[i]));
		} catch (BeansException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
