package com.cssrc.ibms.core.resources.ioOld2New.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sign")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignIOModel{
	
	@XmlAttribute
	private String tempid;
	@XmlAttribute
	private String order;
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTempid() {
		return tempid;
	}
	public void setTempid(String tempid) {
		this.tempid = tempid;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
}
