package com.cssrc.ibms.index.service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.index.model.PortalConfig;

/**
 * 解析首页栏目模板方法
 * @author YangBo
 *
 */
@Service
public class PortalConfigService implements InitializingBean{

	private static Map<String, PortalConfig> portalConfigsMap = new LinkedHashMap<String, PortalConfig>();
	
	@SuppressWarnings("unchecked")
	public void initPortalConfigs() throws IOException
	{	
		ResourcePropertySource propertySource = new ResourcePropertySource("classpath:sys.properties");
		String FTL_ROOT=(String) propertySource.getProperty("ftl.root");
		String path=FTL_ROOT + "portal" + File.separator + "portal-config.xml";
		Document doc = Dom4jUtil.load(path,"");
		List<Node> nodes = doc.getRootElement().selectNodes("/templates/template");
		for (Node template : nodes) {
			Element el = (Element)template;
			String id = el.attributeValue("id");
			String name = el.attributeValue("name");
			String service = el.attributeValue("service");
			PortalConfig config = new PortalConfig(id, name, service);
			portalConfigsMap.put(id, config);
		}
	}

	public Map<String, PortalConfig> getPortalConfigMap() {
		return portalConfigsMap;
	}

	public void afterPropertiesSet() throws Exception
	{
		initPortalConfigs();
	}

}
