package com.cssrc.ibms.system.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SysDataSourceDefXmlList
 * @author liubo
 * @date 2017年4月15日
 */
@XmlRootElement(name="system")
@XmlAccessorType(XmlAccessType.FIELD)
public class SysDataSourceDefXmlList {

	@XmlElements({@javax.xml.bind.annotation.XmlElement(name="dataSources",type=SysDataSourceDefXml.class)})
	private List<SysDataSourceDefXml> sysDataSourceDefXmlList;

	public List<SysDataSourceDefXml> getSysDataSourceDefXmlList() {
		return sysDataSourceDefXmlList;
	}

	public void setSysDataSourceDefXmlList(
			List<SysDataSourceDefXml> sysDataSourceDefXmlList) {
		this.sysDataSourceDefXmlList = sysDataSourceDefXmlList;
	}
	
	
}
