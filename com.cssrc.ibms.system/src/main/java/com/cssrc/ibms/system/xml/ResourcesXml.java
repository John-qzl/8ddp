package com.cssrc.ibms.system.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.system.model.Resources;
/**
 * 
 * <p>Title:ResourcesXml</p>
 * @author YangBo 
 * @date 2016-8-25上午09:31:18
 */
@XmlRootElement(name = "resources")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResourcesXml {

	@XmlElement(name = "resour", type = Resources.class)
	private Resources resour;

	@XmlElement(name = "resource", type = Resources.class)
	private List<Resources> resList;

	@XmlElement(name = "resourcesList", type = ResourcesXml.class)
	private List<ResourcesXml> resourcesList;

	public Resources getResour() {
		return this.resour;
	}

	public void setResour(Resources resour) {
		this.resour = resour;
	}

	public List<Resources> getResList() {
		return this.resList;
	}

	public void setResList(List<Resources> resList) {
		this.resList = resList;
	}

	public List<ResourcesXml> getResourcesList() {
		if (this.resourcesList == null) {
			return new ArrayList();
		}
		return this.resourcesList;
	}

	public void setResourcesList(List<ResourcesXml> resourcesList) {
		this.resourcesList = resourcesList;
	}
}
