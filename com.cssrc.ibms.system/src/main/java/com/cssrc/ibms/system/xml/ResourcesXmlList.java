package com.cssrc.ibms.system.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 
 * <p>Title:ResourcesXmlList</p>
 * @author Yangbo 
 * @date 2016-8-25上午09:31:22
 */
@XmlRootElement(name = "res")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResourcesXmlList {

	@XmlElements( { @javax.xml.bind.annotation.XmlElement(name = "resources", type = ResourcesXml.class) })
	private List<ResourcesXml> resourcesXmlList;

	public List<ResourcesXml> getResourcesXmlList() {
		if (this.resourcesXmlList == null) {
			return new ArrayList();
		}
		return this.resourcesXmlList;
	}

	public void setResourcesXmlList(List<ResourcesXml> resourcesXmlList) {
		this.resourcesXmlList = resourcesXmlList;
	}
}
