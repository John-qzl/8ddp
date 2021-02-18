package com.cssrc.ibms.system.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.system.model.SysDataSourceDef;

/**
 * SysDataSourceDefXml
 * @author liubo
 * @date 2017年4月15日
 */
@XmlRootElement(name="dataSources")
@XmlAccessorType(XmlAccessType.FIELD)
public class SysDataSourceDefXml {
	@XmlElement(name="sysDataSourceDef", type=SysDataSourceDef.class)
	private SysDataSourceDef sysDataSourceDef;
	     
	public SysDataSourceDef getSysDataSourceDef() {
		return this.sysDataSourceDef;
	}
	     
	public void setSysDataSourceDef(SysDataSourceDef sysDataSourceDef) {
		this.sysDataSourceDef = sysDataSourceDef;
	}
}
